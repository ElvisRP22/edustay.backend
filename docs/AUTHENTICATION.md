# 🔐 Autenticación JWT

## Descripción General

EduStay implementa autenticación stateless basada en JWT (JSON Web Tokens) con encriptación BCrypt para contraseñas.

## Endpoints

### POST `/api/auth/login`

Autentica un usuario y devuelve un JWT token.

**Request:**
```json
{
  "email": "usuario@example.com",
  "password": "contraseña123"
}
```

**Response (200):**
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "usuario@example.com",
  "telefono": "+34 912345678",
  "fotoUrl": null,
  "rol": "ESTUDIANTE",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login exitoso"
}
```

---

### POST `/api/auth/register`

Registra un nuevo usuario y devuelve un JWT token.

**Request:**
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "nuevo@example.com",
  "password": "contraseña123",
  "confirmPassword": "contraseña123",
  "telefono": "+34 912345678"
}
```

**Response (201):**
```json
{
  "id": 2,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "nuevo@example.com",
  "telefono": "+34 912345678",
  "fotoUrl": null,
  "rol": "ESTUDIANTE",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Registro exitoso"
}
```

---

### GET `/api/auth/validate`

Valida si un token JWT es válido.

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200):**
```
Token válido
```

---

## Cómo Usar el Token

Todos los endpoints protegidos requieren el header `Authorization`:

```bash
curl -H "Authorization: Bearer <TOKEN>" https://api.edustay.com/api/usuarios
```

---

## Frontend Integration

### React con Axios

```typescript
import axios from 'axios';

const api = axios.create({ baseURL: 'http://localhost:8080' });

// Interceptor para agregar token automáticamente
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Login
const login = async (email: string, password: string) => {
  const { data } = await api.post('/api/auth/login', { email, password });
  localStorage.setItem('token', data.token);
  localStorage.setItem('user', JSON.stringify(data));
  return data;
};

// Logout
const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};

export default { api, login, logout };
```

### React Hook useAuth

```typescript
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const useAuth = () => {
  const [user, setUser] = useState<any>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const user = localStorage.getItem('user');
    if (user) setUser(JSON.parse(user));
  }, []);

  const login = async (email: string, password: string) => {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    const data = await response.json();
    localStorage.setItem('token', data.token);
    setUser(data);
    navigate('/'); // Redirige a inicio
    return data;
  };

  return { user, login };
};
```

### Rutas Protegidas en React Router

```typescript
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }: any) => {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" />;
};

// Uso:
// <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
```

---

## Seguridad

| Aspecto | Implementación |
|--------|---|
| **Encriptación de contraseñas** | BCrypt (10 rounds) |
| **Algoritmo JWT** | HS256 |
| **Expiración** | 24 horas |
| **CORS** | Configurado para desarrollo |
| **CSRF** | Deshabilitado (stateless) |

---

## Manejo de Errores

| Código | Mensaje | Solución |
|--------|---------|----------|
| 400 | Datos inválidos | Verifica que todos los campos sean válidos |
| 401 | Credenciales incorrectas | Email o contraseña incorrecta |
| 409 | Email ya registrado | Usa otro email |
| 500 | Error del servidor | Contacta soporte |

---

## Variables de Entorno

```properties
JWT_SECRET=tunombreClaveSuperSeguraDeAlMenos32Caracteres
JWT_EXPIRATION=86400000
```


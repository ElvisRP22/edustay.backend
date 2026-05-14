# 📚 Referencia de API

## Base URL

```
http://localhost:8080
```

## Autenticación

Todos los endpoints excepto `/api/auth/*` requieren:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## Endpoints de Autenticación

### POST /api/auth/login

Autentica un usuario.

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|----------|-------------|
| `email` | string | ✅ | Email del usuario |
| `password` | string | ✅ | Contraseña |

**Status Codes:**
- `200` - Login exitoso
- `401` - Credenciales inválidas
- `500` - Error del servidor

---

### POST /api/auth/register

Registra un nuevo usuario.

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|----------|-------------|
| `nombre` | string | ✅ | Nombre del usuario |
| `apellido` | string | ✅ | Apellido del usuario |
| `email` | string | ✅ | Email único |
| `password` | string | ✅ | Contraseña (mín. 6 caracteres) |
| `confirmPassword` | string | ✅ | Confirmación de contraseña |
| `telefono` | string | ❌ | Número de teléfono |

**Status Codes:**
- `201` - Registro exitoso
- `400` - Datos inválidos
- `409` - Email ya existe
- `500` - Error del servidor

---

### GET /api/auth/validate

Valida si un token es válido.

**Status Codes:**
- `200` - Token válido
- `401` - Token inválido o expirado

---

## Estructura de Respuestas

### Respuesta Exitosa (Auth)

```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "telefono": "+34 912345678",
  "fotoUrl": null,
  "rol": "ESTUDIANTE",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login exitoso"
}
```

### Respuesta de Error

```json
{
  "status": 400,
  "message": "Descripción del error",
  "code": "ERROR_CODE",
  "timestamp": "2025-05-08T10:30:00",
  "path": "/api/auth/login"
}
```

---

## Códigos de Error

| Code | HTTP | Descripción |
|------|------|-------------|
| `INVALID_CREDENTIALS` | 401 | Email o contraseña incorrecta |
| `NOT_FOUND` | 404 | Usuario no encontrado |
| `CONFLICT` | 409 | Recurso ya existe (email duplicado) |
| `VALIDATION_ERROR` | 400 | Datos inválidos |
| `INTERNAL_ERROR` | 500 | Error del servidor |

---

## Roles de Usuario

| Rol | Descripción |
|-----|-------------|
| `ESTUDIANTE` | Usuario regular |
| `PROPIETARIO` | Dueño de habitaciones |
| `ADMIN` | Administrador del sistema |

---

## Campos de Usuario

```json
{
  "id": "Long - ID único",
  "nombre": "String - Nombre del usuario",
  "apellido": "String - Apellido del usuario",
  "email": "String - Email único",
  "password": "String - Password (solo escritura, nunca se devuelve)",
  "telefono": "String - Teléfono opcional",
  "rol": "UserRole - Rol del usuario",
  "fotoUrl": "String - URL de la foto de perfil",
  "emailVerificado": "Boolean - Si el email fue verificado",
  "identidadVerificada": "VerificationStatus - Estado de verificación de identidad",
  "fechaRegistro": "LocalDateTime - Fecha de registro"
}
```

---

## Validaciones

### Email
- Formato válido de email
- Debe ser único en el sistema

### Contraseña
- Mínimo 6 caracteres
- Máximo 100 caracteres
- Se encripta con BCrypt

### Nombre/Apellido
- Máximo 50 caracteres
- No puede estar vacío

---

## Rate Limiting

Actualmente no hay límite de requests. Se recomienda implementar:
- Máximo 5 intentos de login fallidos
- Ban temporal de 15 minutos


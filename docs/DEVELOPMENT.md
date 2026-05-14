# 🛠️ Desarrollo Local

## Requisitos Previos

- Java 21+
- Maven 3.8.1+ (incluido con `mvnw`)
- MySQL 8.0+
- Node.js 18+ (solo si trabajas en frontend)

---

## 1. Configurar Base de Datos

### Crear archivo `.env.properties`

Copia el archivo de ejemplo:
```bash
cp .env.example .env.properties
```

Edita y configura:
```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=edustay_db
DB_USER=root
DB_PASS=tu_contraseña_aqui
JWT_SECRET=unaClaveMuySeguraConAlMenos32Caracteres
JWT_EXPIRATION=86400000
```

### Crear base de datos (opcional)

```sql
CREATE DATABASE IF NOT EXISTS edustay_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

---

## 2. Compilar el Proyecto

```bash
# Windows
.\mvnw clean compile

# macOS/Linux
./mvnw clean compile
```

---

## 3. Ejecutar en Desarrollo

```bash
# Windows
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# macOS/Linux
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 4. Swagger/OpenAPI Documentation

Una vez ejecutada la aplicación, accede a:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 5. Probar Endpoints

### Registrar usuario

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre":"Juan",
    "apellido":"Pérez",
    "email":"juan@test.com",
    "password":"password123",
    "confirmPassword":"password123",
    "telefono":"+34 912345678"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"juan@test.com",
    "password":"password123"
  }'
```

Copia el `token` devuelto para el siguiente comando.

### Usar token en request

```bash
curl -X GET http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer <PEGA_EL_TOKEN_AQUI>"
```

---

## 6. Estructura del Proyecto

```
src/main/
├── java/com/edustay/backend/
│   ├── Application.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controllers/
│   │   └── AuthController.java
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── AuthResponse.java
│   ├── exceptions/
│   │   ├── AuthException.java
│   │   ├── ErrorResponse.java
│   │   └── GlobalExceptionHandler.java
│   ├── models/
│   │   └── Usuario.java
│   ├── repositories/
│   │   └── UsuarioRepository.java
│   ├── security/
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── CustomUserDetailsService.java
│   └── services/
│       ├── AuthService.java
│       └── AuthServiceImpl.java
└── resources/
    ├── application.properties
    ├── application-dev.properties
    └── application-prod.properties
```

---

## 7. Perfiles de Spring

### Desarrollo (`-Dspring-boot.run.arguments="--spring.profiles.active=dev"`)

- SQL logging habilitado
- `ddl-auto: update` (crea/actualiza tablas automáticamente)
- JWT secret por defecto

### Producción

```bash
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

- SQL logging deshabilitado
- `ddl-auto: validate` (solo valida estructura)
- Requiere `JWT_SECRET` en variables de entorno

---

## 8. Troubleshooting

### Error: "Cannot find symbol class JwtTokenProvider"

Ejecuta `.\mvnw clean compile` nuevamente.

### Error: "B cannot be resolved"

Asegúrate de tener Java 21+ instalado.

### Base de datos no se conecta

Verifica las credenciales en `.env.properties` y que MySQL está corriendo.

---

## 9. Próximos Pasos

1. Configura el frontend con las credenciales de la API
2. Implementa el AuthInterceptor (ver `docs/AUTHENTICATION.md`)
3. Protege las rutas del frontend


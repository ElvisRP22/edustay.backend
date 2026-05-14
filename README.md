# 🎓 EduStay - Backend

Sistema backend para la plataforma de alquiler de habitaciones para estudiantes.

## 🚀 Quick Start

### Requisitos
- Java 21+
- MySQL 8.0+

### 1. Configurar variables de entorno

```bash
cp .env.example .env.properties
# Edita .env.properties con tus credenciales
```

### 2. Ejecutar en desarrollo

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

La API estará disponible en: `http://localhost:8080`

### 3. Swagger Documentation

Una vez ejecutada la aplicación:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📚 Documentación

- **[DEVELOPMENT.md](docs/DEVELOPMENT.md)** - Setup local, compilación, troubleshooting
- **[AUTHENTICATION.md](docs/AUTHENTICATION.md)** - Guía JWT, integración frontend
- **[API.md](docs/API.md)** - Referencia completa de endpoints

---

## 🏗️ Arquitectura

### Stack Tecnológico

- **Spring Boot 4.0.6** - Framework web
- **Spring Security** - Autenticación y autorización
- **JWT (JJWT)** - Token based authentication
- **MySQL 8.0** - Base de datos
- **Hibernate/JPA** - ORM

### Estructura del Proyecto

```
src/main/
├── java/com/edustay/backend/
│   ├── config/        # Configuración (Security, etc)
│   ├── controllers/   # REST endpoints
│   ├── dto/           # Objetos de transferencia de datos
│   ├── exceptions/    # Manejo de excepciones
│   ├── models/        # Entidades JPA
│   ├── repositories/  # Acceso a BD
│   ├── security/      # Seguridad (JWT, filters)
│   └── services/      # Lógica de negocio
└── resources/
    ├── application.properties
    ├── application-dev.properties
    └── application-prod.properties
```

---

## 🔐 Autenticación

El proyecto implementa autenticación con JWT:

- **Endpoints públicos:** `/api/auth/login`, `/api/auth/register`
- **Endpoints protegidos:** Requieren header `Authorization: Bearer <token>`
- **Expiración:** 24 horas
- **Encriptación:** BCrypt para contraseñas

Ver [AUTHENTICATION.md](docs/AUTHENTICATION.md) para guía completa.

---

## 🛠️ Configuración Base (application.properties)

El proyecto usa perfiles de Spring para diferenciar ambientes:

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=edustay_db
DB_USER=root
DB_PASS=tu_contraseña
```

---

# ▶️ 5. Ejecutar el Proyecto

Debes activar el perfil `dev`.

---

## 🔹 Opción A: Variable de entorno (recomendada)

```bash
SPRING_PROFILES_ACTIVE=dev
```

---

## 🔹 Opción B: IntelliJ IDEA

1. Ve a **Run > Edit Configurations**
2. Selecciona `EdustayApplication`
3. En **Environment variables** agrega:

```text
SPRING_PROFILES_ACTIVE=dev
```

---

## 🔹 Opción C: VS Code

En `.vscode/launch.json`:

```json
{
  "configurations": [
    {
      "type": "java",
      "name": "Launch EdustayApplication",
      "request": "launch",
      "mainClass": "com.edustay.backend.Application",
      "env": {
        "SPRING_PROFILES_ACTIVE": "dev"
      }
    }
  ]
}
```

---

# 📘 6. Diccionario de Variables

| Variable | Descripción         | Ejemplo    |
| -------- | ------------------- | ---------- |
| DB_HOST  | Host de MySQL       | localhost  |
| DB_PORT  | Puerto de MySQL     | 3306       |
| DB_NAME  | Nombre de la BD     | edustay_db |
| DB_USER  | Usuario de MySQL    | root       |
| DB_PASS  | Contraseña de MySQL | ******     |

---

# 🧩 Flujo de Trabajo del Equipo

1. Clonar el repositorio
2. Copiar `.env.example` → `.env.properties`
3. Configurar credenciales locales
4. Ejecutar con perfil `dev`

---

# 🏁 Resumen

| Elemento                      | Responsabilidad             |
| ----------------------------- | --------------------------- |
| `.env.properties`             | Credenciales                |
| `application.properties`      | Configuración base segura   |
| `application-dev.properties`  | Configuración de desarrollo |
| `application-prod.properties` | Configuración de producción |

---
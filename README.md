# 🛠️ Configuración del Entorno de Desarrollo - EduStay

Este proyecto sigue buenas prácticas profesionales separando:

* **Perfiles de Spring (`dev`, `prod`)** → controlan el comportamiento de la aplicación
* **Variables de entorno (`.env`)** → gestionan credenciales y configuración sensible

Esto permite trabajar en equipo sin exponer datos sensibles y sin afectar otros entornos.

---

# 🧠 Arquitectura de Configuración

```text
src/main/resources/
 ├── application.properties        # Configuración base (segura)
 ├── application-dev.properties    # Configuración de desarrollo
 ├── application-prod.properties   # Configuración de producción

.env.properties   # Variables locales (NO versionado)
.env.example      # Plantilla (SÍ versionado)
```

---

# ⚙️ 1. Configuración Base (application.properties)

Archivo base seguro que se aplica a todos los entornos:

```properties
spring.application.name=edustay.backend

# =========================
# BASE DE DATOS (usa .env)
# =========================
spring.config.import=optional:file:.env.properties

spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:edustay_db}?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASS}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# =========================
# JPA BASE (seguro)
# =========================
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy

# Configuración segura por defecto
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=none

# Dialecto MySQL
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

---

# 🔧 2. Perfil de Desarrollo (application-dev.properties)

Configuración usada solo en desarrollo:

```properties
# Mostrar SQL en consola
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Actualiza el esquema automáticamente (solo dev)
spring.jpa.hibernate.ddl-auto=update
```

---

# 🚀 3. Perfil de Producción (application-prod.properties)

Configuración segura para producción:

```properties
spring.jpa.show-sql=false

# No modifica la BD, solo válida estructura
spring.jpa.hibernate.ddl-auto=validate
```

---

# 🔐 4. Variables de Entorno (.env.properties)

## 📌 Paso 1: Crear archivo local

Copia el archivo de ejemplo:

```bash
cp .env.example .env.properties
```

---

## 📌 Paso 2: Configurar tus credenciales

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
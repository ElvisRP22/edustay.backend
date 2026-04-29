# 🛠️ Configuración del Entorno de Desarrollo - EduStay

Para que el proyecto funcione correctamente en tu máquina local, seguimos el estándar de **Variables de Entorno**. Esto permite que cada uno use sus propias credenciales de MySQL sin afectar el código de los demás ni exponer contraseñas en el repositorio.

---

## 1. Requisitos Previos

- **MySQL** (v8.0 o superior).
- **Base de Datos:** No es necesario crearla manualmente, el sistema la creará por ti (`edustay_db`) si tienes los permisos correctos en tu usuario de MySQL.

---

## 2. Configuración de Variables en el IDE

> **IMPORTANTE:**  
> No modifiques el archivo `application.properties`.  
> Debes inyectar las variables directamente en la configuración de ejecución de tu IDE.

---

### 🔹 Si usas IntelliJ IDEA (Recomendado)

1. Ve al menú superior: **Run > Edit Configurations...**
2. Selecciona la aplicación principal: `EdustayApplication`
3. Busca el campo **Environment variables**
4. Haz clic en el icono de la derecha (📁) y pega lo siguiente (ajustando a tus datos reales):

```text
DB_HOST=localhost;DB_PORT=3306;DB_NAME=edustay_db;DB_USER=tu_usuario;DB_PASS=tu_contraseña
```

5. Dale a **Apply** y luego a **OK**

---

### 🔹 Si usas Visual Studio Code

1. Abre el archivo `.vscode/launch.json`  
   (si no existe, créalo)

2. Dentro de la configuración de `"configurations"`, añade la propiedad `"env"`:

```json
{
  "configurations": [
    {
      "type": "java",
      "name": "Launch EdustayApplication",
      "request": "launch",
      "mainClass": "com.edustay.backend.EdustayApplication",
      "env": {
        "DB_HOST": "localhost",
        "DB_PORT": "3306",
        "DB_NAME": "edustay_db",
        "DB_USER": "root",
        "DB_PASS": "tu_contraseña"
      }
    }
  ]
}
```

---

## 3. Diccionario de Variables Requeridas

| Variable   | Descripción                     | Valor sugerido |
|------------|---------------------------------|----------------|
| DB_HOST    | Dirección del servidor de BD    | localhost      |
| DB_PORT    | Puerto de conexión MySQL        | 3306           |
| DB_NAME    | Nombre de la base de datos      | edustay_db     |
| DB_USER    | Tu usuario local de MySQL       | root           |
| DB_PASS    | Tu contraseña local de MySQL    | (la que uses)  |

---

## ⚠️ Advertencia

> El archivo `.env` o cualquier configuración que contenga contraseñas reales **NO debe subirse al repositorio**.  
> Asegúrate de que estén ignorados en el `.gitignore`.
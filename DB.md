# edustay documentation
## Summary

- [Introduction](#introduction)
- [Database Type](#database-type)
- [Table Structure](#table-structure)
	- [usuarios](#usuarios)
	- [codigos_otp](#codigos_otp)
	- [perfiles_estudiantes](#perfiles_estudiantes)
	- [documentos_verificacion](#documentos_verificacion)
	- [habitaciones](#habitaciones)
	- [fotos_habitacion](#fotos_habitacion)
	- [servicios](#servicios)
	- [habitacion_servicios](#habitacion_servicios)
	- [reglas](#reglas)
	- [habitacion_reglas](#habitacion_reglas)
	- [alquileres_activos](#alquileres_activos)
	- [favoritos](#favoritos)
	- [resenas](#resenas)
	- [mensajes](#mensajes)
	- [reportes](#reportes)
	- [logs_auditoria](#logs_auditoria)
- [Relationships](#relationships)
- [Database Diagram](#database-diagram)

## Introduction

## Database type

- **Database system:** MySQL
## Table structure

### usuarios

| Name                     | Type                | Settings                               | References | Note |
| ------------------------ | ------------------- | -------------------------------------- | ---------- | ---- |
| **id**                   | INTEGER             | 🔑 PK, not null, unique, autoincrement |            |      |
| **nombre**               | VARCHAR             | not null                               |            |      |
| **apellido**             | VARCHAR             | not null                               |            |      |
| **email**                | VARCHAR             | not null                               |            |      |
| **password**             | VARCHAR             | not null                               |            |      |
| **telefono**             | VARCHAR             | null                                   |            |      |
| **dni**                  | VARCHAR             | null                                   |            |      |
| **rol**                  | USER_ROLE           | not null                               |            |      |
| **foto_url**             | VARCHAR             | not null                               |            |      |
| **email_verificado**     | BOOLEAN             | not null, default: false               |            |      |
| **identidad_verificada** | VERIFICATION_STATUS | not null, default: PENDIENTE           |            |      |
| **fecha_registro**       | TIMESTAMP           | not null, default: now()               |            |      | 


### codigos_otp

| Name           | Type       | Settings                               | References                         | Note |
| -------------- | ---------- | -------------------------------------- | ---------------------------------- | ---- |
| **id**         | INTEGER    | 🔑 PK, not null, unique, autoincrement |                                    |      |
| **usuario_id** | INTEGER    | not null                               | fk_codigos_otp_usuario_id_usuarios |      |
| **codigo**     | VARCHAR(6) | not null                               |                                    |      |
| **expiracion** | TIMESTAMP  | not null                               |                                    |      |
| **usado**      | BOOLEAN    | not null, default: false               |                                    |      | 


### perfiles_estudiantes

| Name                         | Type    | Settings                | References                                  | Note |
| ---------------------------- | ------- | ----------------------- | ------------------------------------------- | ---- |
| **usuario_id**               | INTEGER | 🔑 PK, not null, unique | fk_perfiles_estudiantes_usuario_id_usuarios |      |
| **carrera**                  | VARCHAR | null                    |                                             |      |
| **ciclo**                    | INTEGER | null                    |                                             |      |
| **preferencias_convivencia** | TEXT    | null                    |                                             |      | 


### documentos_verificacion

| Name                 | Type                | Settings                               | References                                     | Note |
| -------------------- | ------------------- | -------------------------------------- | ---------------------------------------------- | ---- |
| **id**               | INTEGER             | 🔑 PK, not null, unique, autoincrement |                                                |      |
| **usuario_id**       | INTEGER             | not null                               | fk_documentos_verificacion_usuario_id_usuarios |      |
| **tipo**             | DOC_TYPE            | not null                               |                                                |      |
| **archivo_url**      | VARCHAR             | not null                               |                                                |      |
| **fecha_subida**     | TIMESTAMP           | not null, default: now()               |                                                |      |
| **estado**           | VERIFICATION_STATUS | not null, default: PENDIENTE           |                                                |      |
| **comentario_admin** | TEXT                | null                                   |                                                |      | 


### habitaciones

| Name                      | Type        | Settings                               | References                             | Note |
| ------------------------- | ----------- | -------------------------------------- | -------------------------------------- | ---- |
| **id**                    | INTEGER     | 🔑 PK, not null, unique, autoincrement |                                        |      |
| **arrendador_id**         | INTEGER     | not null                               | fk_habitaciones_arrendador_id_usuarios |      |
| **titulo**                | VARCHAR     | not null                               |                                        |      |
| **descripcion**           | TEXT        | null                                   |                                        |      |
| **precio**                | DECIMAL     | not null                               |                                        |      |
| **direccion**             | VARCHAR     | not null                               |                                        |      |
| **distancia_utp_minutos** | INTEGER     | null                                   |                                        |      |
| **latitud**               | DECIMAL     | null                                   |                                        |      |
| **longitud**              | DECIMAL     | null                                   |                                        |      |
| **estado**                | ROOM_STATUS | not null, default: DISPONIBLE          |                                        |      |
| **fecha_publicacion**     | TIMESTAMP   | not null, default: now()               |                                        |      | 


### fotos_habitacion

| Name              | Type    | Settings                               | References                                     | Note |
| ----------------- | ------- | -------------------------------------- | ---------------------------------------------- | ---- |
| **id**            | INTEGER | 🔑 PK, not null, unique, autoincrement |                                                |      |
| **habitacion_id** | INTEGER | not null                               | fk_fotos_habitacion_habitacion_id_habitaciones |      |
| **url**           | VARCHAR | not null                               |                                                |      |
| **es_principal**  | BOOLEAN | not null, default: false               |                                                |      | 


### servicios

| Name       | Type    | Settings                               | References | Note |
| ---------- | ------- | -------------------------------------- | ---------- | ---- |
| **id**     | INTEGER | 🔑 PK, not null, unique, autoincrement |            |      |
| **nombre** | VARCHAR | not null                               |            |      | 


### habitacion_servicios

| Name              | Type                           | Settings | References                                         | Note |
| ----------------- | ------------------------------ | -------- | -------------------------------------------------- | ---- |
| **habitacion_id** | INTEGER                        | not null | fk_habitacion_servicios_habitacion_id_habitaciones |      |
| **servicio_id**   | INTEGER                        | not null | fk_habitacion_servicios_servicio_id_servicios      |      |
| **primary**       | KEY(HABITACION_ID,SERVICIO_ID) | not null |                                                    |      | 


### reglas

| Name            | Type    | Settings                               | References | Note |
| --------------- | ------- | -------------------------------------- | ---------- | ---- |
| **id**          | INTEGER | 🔑 PK, not null, unique, autoincrement |            |      |
| **descripcion** | VARCHAR | not null                               |            |      | 


### habitacion_reglas

| Name              | Type                        | Settings | References                                      | Note |
| ----------------- | --------------------------- | -------- | ----------------------------------------------- | ---- |
| **habitacion_id** | INTEGER                     | not null | fk_habitacion_reglas_habitacion_id_habitaciones |      |
| **regla_id**      | INTEGER                     | not null | fk_habitacion_reglas_regla_id_reglas            |      |
| **primary**       | KEY(HABITACION_ID,REGLA_ID) | not null |                                                 |      | 


### alquileres_activos

| Name              | Type      | Settings                               | References                                       | Note |
| ----------------- | --------- | -------------------------------------- | ------------------------------------------------ | ---- |
| **id**            | INTEGER   | 🔑 PK, not null, unique, autoincrement |                                                  |      |
| **habitacion_id** | INTEGER   | not null                               | fk_alquileres_activos_habitacion_id_habitaciones |      |
| **estudiante_id** | INTEGER   | not null                               | fk_alquileres_activos_estudiante_id_usuarios     |      |
| **fecha_inicio**  | TIMESTAMP | not null, default: now()               |                                                  |      |
| **monto_pactado** | DECIMAL   | not null                               |                                                  |      | 


### favoritos

| Name              | Type                             | Settings | References                              | Note |
| ----------------- | -------------------------------- | -------- | --------------------------------------- | ---- |
| **estudiante_id** | INTEGER                          | not null | fk_favoritos_estudiante_id_usuarios     |      |
| **habitacion_id** | INTEGER                          | not null | fk_favoritos_habitacion_id_habitaciones |      |
| **primary**       | KEY(ESTUDIANTE_ID,HABITACION_ID) | not null |                                         |      | 


### resenas

| Name              | Type      | Settings                               | References                            | Note |
| ----------------- | --------- | -------------------------------------- | ------------------------------------- | ---- |
| **id**            | INTEGER   | 🔑 PK, not null, unique, autoincrement |                                       |      |
| **estudiante_id** | INTEGER   | not null                               | fk_resenas_estudiante_id_usuarios     |      |
| **habitacion_id** | INTEGER   | not null                               | fk_resenas_habitacion_id_habitaciones |      |
| **calificacion**  | INTEGER   | not null                               |                                       |      |
| **comentario**    | TEXT      | null                                   |                                       |      |
| **fecha**         | TIMESTAMP | not null, default: now()               |                                       |      | 


### mensajes

| Name              | Type      | Settings                               | References                             | Note |
| ----------------- | --------- | -------------------------------------- | -------------------------------------- | ---- |
| **id**            | INTEGER   | 🔑 PK, not null, unique, autoincrement |                                        |      |
| **emisor_id**     | INTEGER   | not null                               | fk_mensajes_emisor_id_usuarios         |      |
| **receptor_id**   | INTEGER   | not null                               | fk_mensajes_receptor_id_usuarios       |      |
| **habitacion_id** | INTEGER   | not null                               | fk_mensajes_habitacion_id_habitaciones |      |
| **contenido**     | TEXT      | not null                               |                                        |      |
| **leido**         | BOOLEAN   | not null, default: false               |                                        |      |
| **fecha_envio**   | TIMESTAMP | not null, default: now()               |                                        |      | 


### reportes

| Name              | Type      | Settings                               | References                             | Note |
| ----------------- | --------- | -------------------------------------- | -------------------------------------- | ---- |
| **id**            | INTEGER   | 🔑 PK, not null, unique, autoincrement |                                        |      |
| **emisor_id**     | INTEGER   | not null                               | fk_reportes_emisor_id_usuarios         |      |
| **habitacion_id** | INTEGER   | not null                               | fk_reportes_habitacion_id_habitaciones |      |
| **motivo**        | VARCHAR   | not null                               |                                        |      |
| **descripcion**   | TEXT      | not null                               |                                        |      |
| **estado**        | VARCHAR   | not null, default: ABIERTO             |                                        |      |
| **fecha**         | TIMESTAMP | not null, default: now()               |                                        |      | 


### logs_auditoria

| Name           | Type      | Settings                               | References                            | Note |
| -------------- | --------- | -------------------------------------- | ------------------------------------- | ---- |
| **id**         | INTEGER   | 🔑 PK, not null, unique, autoincrement |                                       |      |
| **usuario_id** | INTEGER   | not null                               | fk_logs_auditoria_usuario_id_usuarios |      |
| **accion**     | VARCHAR   | not null                               |                                       |      |
| **fecha**      | TIMESTAMP | not null, default: now()               |                                       |      | 


## Relationships

- **codigos_otp to usuarios**: many_to_one
- **documentos_verificacion to usuarios**: many_to_one
- **perfiles_estudiantes to usuarios**: one_to_one
- **habitaciones to usuarios**: many_to_one
- **fotos_habitacion to habitaciones**: many_to_one
- **habitacion_servicios to habitaciones**: many_to_one
- **habitacion_servicios to servicios**: many_to_one
- **habitacion_reglas to habitaciones**: many_to_one
- **habitacion_reglas to reglas**: many_to_one
- **alquileres_activos to habitaciones**: one_to_one
- **alquileres_activos to usuarios**: many_to_one
- **favoritos to usuarios**: many_to_one
- **favoritos to habitaciones**: many_to_one
- **resenas to usuarios**: many_to_one
- **resenas to habitaciones**: many_to_one
- **mensajes to usuarios**: many_to_one
- **mensajes to usuarios**: many_to_one
- **mensajes to habitaciones**: many_to_one
- **reportes to usuarios**: many_to_one
- **reportes to habitaciones**: many_to_one
- **logs_auditoria to usuarios**: many_to_one

## Database Diagram

```mermaid
erDiagram
	codigos_otp }o--|| usuarios : references
	documentos_verificacion }o--|| usuarios : references
	perfiles_estudiantes ||--|| usuarios : references
	habitaciones }o--|| usuarios : references
	fotos_habitacion }o--|| habitaciones : references
	habitacion_servicios }o--|| habitaciones : references
	habitacion_servicios }o--|| servicios : references
	habitacion_reglas }o--|| habitaciones : references
	habitacion_reglas }o--|| reglas : references
	alquileres_activos ||--|| habitaciones : references
	alquileres_activos }o--|| usuarios : references
	favoritos }o--|| usuarios : references
	favoritos }o--|| habitaciones : references
	resenas }o--|| usuarios : references
	resenas }o--|| habitaciones : references
	mensajes }o--|| usuarios : references
	mensajes }o--|| usuarios : references
	mensajes }o--|| habitaciones : references
	reportes }o--|| usuarios : references
	reportes }o--|| habitaciones : references
	logs_auditoria }o--|| usuarios : references

	usuarios {
		INTEGER id
		VARCHAR nombre
		VARCHAR apellido
		VARCHAR email
		VARCHAR password
		VARCHAR telefono
		VARCHAR dni
		USER_ROLE rol
		VARCHAR foto_url
		BOOLEAN email_verificado
		VERIFICATION_STATUS identidad_verificada
		TIMESTAMP fecha_registro
	}

	codigos_otp {
		INTEGER id
		INTEGER usuario_id
		VARCHAR(6) codigo
		TIMESTAMP expiracion
		BOOLEAN usado
	}

	perfiles_estudiantes {
		INTEGER usuario_id
		VARCHAR carrera
		INTEGER ciclo
		TEXT preferencias_convivencia
	}

	documentos_verificacion {
		INTEGER id
		INTEGER usuario_id
		DOC_TYPE tipo
		VARCHAR archivo_url
		TIMESTAMP fecha_subida
		VERIFICATION_STATUS estado
		TEXT comentario_admin
	}

	habitaciones {
		INTEGER id
		INTEGER arrendador_id
		VARCHAR titulo
		TEXT descripcion
		DECIMAL precio
		VARCHAR direccion
		INTEGER distancia_utp_minutos
		DECIMAL latitud
		DECIMAL longitud
		ROOM_STATUS estado
		TIMESTAMP fecha_publicacion
	}

	fotos_habitacion {
		INTEGER id
		INTEGER habitacion_id
		VARCHAR url
		BOOLEAN es_principal
	}

	servicios {
		INTEGER id
		VARCHAR nombre
	}

	habitacion_servicios {
		INTEGER habitacion_id
		INTEGER servicio_id
		KEY(HABITACION_ID,SERVICIO_ID) primary
	}

	reglas {
		INTEGER id
		VARCHAR descripcion
	}

	habitacion_reglas {
		INTEGER habitacion_id
		INTEGER regla_id
		KEY(HABITACION_ID,REGLA_ID) primary
	}

	alquileres_activos {
		INTEGER id
		INTEGER habitacion_id
		INTEGER estudiante_id
		TIMESTAMP fecha_inicio
		DECIMAL monto_pactado
	}

	favoritos {
		INTEGER estudiante_id
		INTEGER habitacion_id
		KEY(ESTUDIANTE_ID,HABITACION_ID) primary
	}

	resenas {
		INTEGER id
		INTEGER estudiante_id
		INTEGER habitacion_id
		INTEGER calificacion
		TEXT comentario
		TIMESTAMP fecha
	}

	mensajes {
		INTEGER id
		INTEGER emisor_id
		INTEGER receptor_id
		INTEGER habitacion_id
		TEXT contenido
		BOOLEAN leido
		TIMESTAMP fecha_envio
	}

	reportes {
		INTEGER id
		INTEGER emisor_id
		INTEGER habitacion_id
		VARCHAR motivo
		TEXT descripcion
		VARCHAR estado
		TIMESTAMP fecha
	}

	logs_auditoria {
		INTEGER id
		INTEGER usuario_id
		VARCHAR accion
		TIMESTAMP fecha
	}
```
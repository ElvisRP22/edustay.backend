package com.edustay.backend.dto;

import com.edustay.backend.models.enums.DocType;
import com.edustay.backend.models.enums.VerificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un documento de verificación.
 * Incluye datos del usuario propietario del documento.
 */
@Schema(description = "Respuesta con los datos de un documento de verificación")
public class VerificacionResponse {

    @Schema(description = "ID del documento")
    private Long id;

    @Schema(description = "ID del usuario propietario")
    private Long usuarioId;

    @Schema(description = "Nombre completo del usuario")
    private String usuarioNombre;

    @Schema(description = "Email del usuario")
    private String usuarioEmail;

    @Schema(description = "Tipo de documento")
    private DocType tipo;

    @Schema(description = "URL del archivo subido")
    private String archivoUrl;

    @Schema(description = "Fecha en que se subió el documento")
    private LocalDateTime fechaSubida;

    @Schema(description = "Estado actual del documento")
    private VerificationStatus estado;

    @Schema(description = "Comentario del administrador")
    private String comentarioAdmin;

    @Schema(description = "Estado de identidad verificada del usuario en tabla usuarios")
    private VerificationStatus identidadVerificadaUsuario;

    public VerificacionResponse() {
    }

    public VerificacionResponse(Long id, Long usuarioId, String usuarioNombre, String usuarioEmail,
                                 DocType tipo, String archivoUrl, LocalDateTime fechaSubida,
                                 VerificationStatus estado, String comentarioAdmin,
                                 VerificationStatus identidadVerificadaUsuario) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.usuarioEmail = usuarioEmail;
        this.tipo = tipo;
        this.archivoUrl = archivoUrl;
        this.fechaSubida = fechaSubida;
        this.estado = estado;
        this.comentarioAdmin = comentarioAdmin;
        this.identidadVerificadaUsuario = identidadVerificadaUsuario;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public DocType getTipo() {
        return tipo;
    }

    public void setTipo(DocType tipo) {
        this.tipo = tipo;
    }

    public String getArchivoUrl() {
        return archivoUrl;
    }

    public void setArchivoUrl(String archivoUrl) {
        this.archivoUrl = archivoUrl;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public VerificationStatus getEstado() {
        return estado;
    }

    public void setEstado(VerificationStatus estado) {
        this.estado = estado;
    }

    public String getComentarioAdmin() {
        return comentarioAdmin;
    }

    public void setComentarioAdmin(String comentarioAdmin) {
        this.comentarioAdmin = comentarioAdmin;
    }

    public VerificationStatus getIdentidadVerificadaUsuario() {
        return identidadVerificadaUsuario;
    }

    public void setIdentidadVerificadaUsuario(VerificationStatus identidadVerificadaUsuario) {
        this.identidadVerificadaUsuario = identidadVerificadaUsuario;
    }
}

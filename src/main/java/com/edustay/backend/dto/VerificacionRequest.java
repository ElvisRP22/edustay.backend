package com.edustay.backend.dto;

import com.edustay.backend.models.enums.VerificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para cambiar el estado de un documento de verificación.
 * Solo un administrador puede usar este endpoint.
 */
@Schema(description = "Petición para actualizar el estado de un documento de verificación")
public class VerificacionRequest {

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Nuevo estado del documento", allowableValues = {"VERIFICADO", "RECHAZADO"}, example = "VERIFICADO")
    private VerificationStatus estado;

    @Schema(description = "Comentario del administrador (requerido al rechazar)", example = "El documento no es legible")
    private String comentarioAdmin;

    public VerificacionRequest() {
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
}

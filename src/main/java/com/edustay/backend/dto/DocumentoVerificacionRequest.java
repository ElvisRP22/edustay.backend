package com.edustay.backend.dto;

import com.edustay.backend.models.enums.DocType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para registrar la subida de un documento de verificación por parte del usuario
 */
@Schema(description = "Datos del documento de verificación a subir")
public class DocumentoVerificacionRequest {

    @NotNull(message = "El tipo de documento es obligatorio")
    @Schema(description = "Tipo de documento", example = "DNI")
    private DocType tipo;

    @NotBlank(message = "La URL del archivo es obligatoria")
    @Schema(description = "URL del archivo subido (Cloudinary, S3, etc.)", example = "https://cdn.ejemplo.com/dni.jpg")
    private String archivoUrl;

    public DocumentoVerificacionRequest() {}

    public DocType getTipo() { return tipo; }
    public void setTipo(DocType tipo) { this.tipo = tipo; }

    public String getArchivoUrl() { return archivoUrl; }
    public void setArchivoUrl(String archivoUrl) { this.archivoUrl = archivoUrl; }
}

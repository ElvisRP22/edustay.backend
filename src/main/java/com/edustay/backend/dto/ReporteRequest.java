package com.edustay.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un reporte de habitación
 */
@Schema(description = "Datos para reportar una habitación")
public class ReporteRequest {

    @NotNull(message = "La habitación es obligatoria")
    @Schema(description = "ID de la habitación a reportar", example = "5")
    private Long habitacionId;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 100, message = "El motivo no puede exceder 100 caracteres")
    @Schema(description = "Motivo breve del reporte", example = "Fotos engañosas")
    private String motivo;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripción detallada del problema")
    private String descripcion;

    public ReporteRequest() {}

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

package com.edustay.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * DTO para crear un alquiler activo
 */
@Schema(description = "Datos para registrar un nuevo alquiler")
public class AlquilerRequest {

    @NotNull(message = "La habitación es obligatoria")
    @Schema(description = "ID de la habitación a alquilar", example = "5")
    private Long habitacionId;

    @NotNull(message = "El monto pactado es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Schema(description = "Monto mensual acordado", example = "450.00")
    private Double montoPactado;

    @Schema(description = "Fecha de inicio del alquiler (opcional, se usa la fecha actual si no se proporciona)")
    private LocalDateTime fechaInicio;

    @Schema(description = "URL pública del contrato de alquiler en formato PDF", example = "https://storage.edustay.com/contratos/c1.pdf")
    private String contratoUrl;

    public AlquilerRequest() {}

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public Double getMontoPactado() { return montoPactado; }
    public void setMontoPactado(Double montoPactado) { this.montoPactado = montoPactado; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getContratoUrl() { return contratoUrl; }
    public void setContratoUrl(String contratoUrl) { this.contratoUrl = contratoUrl; }
}

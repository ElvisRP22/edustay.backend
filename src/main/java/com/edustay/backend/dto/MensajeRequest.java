package com.edustay.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para enviar un mensaje
 */
@Schema(description = "Datos para enviar un mensaje")
public class MensajeRequest {

    @NotNull(message = "El receptor es obligatorio")
    @Schema(description = "ID del usuario receptor", example = "3")
    private Long receptorId;

    @NotNull(message = "La habitación es obligatoria")
    @Schema(description = "ID de la habitación sobre la que se conversa", example = "7")
    private Long habitacionId;

    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    @Schema(description = "Contenido del mensaje", example = "Hola, ¿sigue disponible la habitación?")
    private String contenido;

    public MensajeRequest() {}

    public Long getReceptorId() { return receptorId; }
    public void setReceptorId(Long receptorId) { this.receptorId = receptorId; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}

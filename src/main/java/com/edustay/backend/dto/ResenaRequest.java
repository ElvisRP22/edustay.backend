package com.edustay.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para crear o actualizar una reseña
 */
public class ResenaRequest {
    
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1 estrella")
    @Max(value = 5, message = "La calificación máxima es 5 estrellas")
    private Integer calificacion;
    
    private String comentario;
    
    @NotNull(message = "El ID de la habitación es obligatorio")
    private Long habitacionId;

    // Constructores
    public ResenaRequest() {}

    public ResenaRequest(Integer calificacion, String comentario, Long habitacionId) {
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.habitacionId = habitacionId;
    }

    // Getters y Setters
    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }
}

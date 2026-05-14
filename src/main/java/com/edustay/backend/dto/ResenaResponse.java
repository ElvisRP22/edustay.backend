package com.edustay.backend.dto;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de reseñas
 */
public class ResenaResponse {
    
    private Long id;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime fecha;
    private Long estudianteId;
    private String estudianteNombre;
    private Long habitacionId;
    private String habitacionTitulo;

    // Constructores
    public ResenaResponse() {}

    public ResenaResponse(Long id, Integer calificacion, String comentario, LocalDateTime fecha, 
                         Long estudianteId, String estudianteNombre, Long habitacionId, String habitacionTitulo) {
        this.id = id;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = fecha;
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.habitacionId = habitacionId;
        this.habitacionTitulo = habitacionTitulo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public String getHabitacionTitulo() { return habitacionTitulo; }
    public void setHabitacionTitulo(String habitacionTitulo) { this.habitacionTitulo = habitacionTitulo; }
}

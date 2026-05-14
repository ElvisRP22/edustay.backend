package com.edustay.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un mensaje
 */
@Schema(description = "Datos de un mensaje")
public class MensajeResponse {

    private Long id;
    private Long emisorId;
    private String emisorNombre;
    private Long receptorId;
    private String receptorNombre;
    private Long habitacionId;
    private String habitacionTitulo;
    private String contenido;
    private boolean leido;
    private LocalDateTime fechaEnvio;

    public MensajeResponse() {}

    public MensajeResponse(Long id, Long emisorId, String emisorNombre,
                            Long receptorId, String receptorNombre,
                            Long habitacionId, String habitacionTitulo,
                            String contenido, boolean leido, LocalDateTime fechaEnvio) {
        this.id = id;
        this.emisorId = emisorId;
        this.emisorNombre = emisorNombre;
        this.receptorId = receptorId;
        this.receptorNombre = receptorNombre;
        this.habitacionId = habitacionId;
        this.habitacionTitulo = habitacionTitulo;
        this.contenido = contenido;
        this.leido = leido;
        this.fechaEnvio = fechaEnvio;
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmisorId() { return emisorId; }
    public void setEmisorId(Long emisorId) { this.emisorId = emisorId; }

    public String getEmisorNombre() { return emisorNombre; }
    public void setEmisorNombre(String emisorNombre) { this.emisorNombre = emisorNombre; }

    public Long getReceptorId() { return receptorId; }
    public void setReceptorId(Long receptorId) { this.receptorId = receptorId; }

    public String getReceptorNombre() { return receptorNombre; }
    public void setReceptorNombre(String receptorNombre) { this.receptorNombre = receptorNombre; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public String getHabitacionTitulo() { return habitacionTitulo; }
    public void setHabitacionTitulo(String habitacionTitulo) { this.habitacionTitulo = habitacionTitulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
}

package com.edustay.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para un alquiler activo
 */
@Schema(description = "Datos de un alquiler activo")
public class AlquilerResponse {

    private Long id;
    private Long habitacionId;
    private String habitacionTitulo;
    private String habitacionDireccion;
    private Long estudianteId;
    private String estudianteNombre;
    private Long arrendadorId;
    private String arrendadorNombre;
    private Double montoPactado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaRegistro;
    private String contratoUrl;

    public AlquilerResponse() {}

    public AlquilerResponse(Long id, Long habitacionId, String habitacionTitulo, String habitacionDireccion,
                            Long estudianteId, String estudianteNombre, Long arrendadorId, String arrendadorNombre,
                            Double montoPactado, LocalDateTime fechaInicio, LocalDateTime fechaRegistro) {
        this.id = id;
        this.habitacionId = habitacionId;
        this.habitacionTitulo = habitacionTitulo;
        this.habitacionDireccion = habitacionDireccion;
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.arrendadorId = arrendadorId;
        this.arrendadorNombre = arrendadorNombre;
        this.montoPactado = montoPactado;
        this.fechaInicio = fechaInicio;
        this.fechaRegistro = fechaRegistro;
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public String getHabitacionTitulo() { return habitacionTitulo; }
    public void setHabitacionTitulo(String habitacionTitulo) { this.habitacionTitulo = habitacionTitulo; }

    public String getHabitacionDireccion() { return habitacionDireccion; }
    public void setHabitacionDireccion(String habitacionDireccion) { this.habitacionDireccion = habitacionDireccion; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public Long getArrendadorId() { return arrendadorId; }
    public void setArrendadorId(Long arrendadorId) { this.arrendadorId = arrendadorId; }

    public String getArrendadorNombre() { return arrendadorNombre; }
    public void setArrendadorNombre(String arrendadorNombre) { this.arrendadorNombre = arrendadorNombre; }

    public Double getMontoPactado() { return montoPactado; }
    public void setMontoPactado(Double montoPactado) { this.montoPactado = montoPactado; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getContratoUrl() { return contratoUrl; }
    public void setContratoUrl(String contratoUrl) { this.contratoUrl = contratoUrl; }
}

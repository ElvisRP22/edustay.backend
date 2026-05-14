package com.edustay.backend.dto;

import com.edustay.backend.models.enums.ReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un reporte de habitación
 */
@Schema(description = "Datos de un reporte de habitación")
public class ReporteResponse {

    private Long id;
    private Long emisorId;
    private String emisorNombre;
    private Long habitacionId;
    private String habitacionTitulo;
    private String motivo;
    private String descripcion;
    private ReportStatus estado;
    private LocalDateTime fecha;

    public ReporteResponse() {}

    public ReporteResponse(Long id, Long emisorId, String emisorNombre,
                            Long habitacionId, String habitacionTitulo,
                            String motivo, String descripcion,
                            ReportStatus estado, LocalDateTime fecha) {
        this.id = id;
        this.emisorId = emisorId;
        this.emisorNombre = emisorNombre;
        this.habitacionId = habitacionId;
        this.habitacionTitulo = habitacionTitulo;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha = fecha;
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmisorId() { return emisorId; }
    public void setEmisorId(Long emisorId) { this.emisorId = emisorId; }

    public String getEmisorNombre() { return emisorNombre; }
    public void setEmisorNombre(String emisorNombre) { this.emisorNombre = emisorNombre; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public String getHabitacionTitulo() { return habitacionTitulo; }
    public void setHabitacionTitulo(String habitacionTitulo) { this.habitacionTitulo = habitacionTitulo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public ReportStatus getEstado() { return estado; }
    public void setEstado(ReportStatus estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

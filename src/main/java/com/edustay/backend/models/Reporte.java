package com.edustay.backend.models;

import com.edustay.backend.models.enums.ReportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad para gestionar las denuncias o reportes sobre habitaciones.
 * Vital para la seguridad y confianza de la comunidad EduStay.
 */
@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id", nullable = false)
    @NotNull(message = "El emisor del reporte es obligatorio")
    private Usuario emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    @NotNull(message = "Se debe especificar la habitación reportada")
    private Habitacion habitacion;

    @NotBlank(message = "Debe indicar un motivo breve")
    @Column(nullable = false, length = 100)
    private String motivo;

    @NotBlank(message = "Debe proporcionar una descripción detallada del problema")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus estado = ReportStatus.ABIERTO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    public Reporte() {
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getEmisor() { return emisor; }
    public void setEmisor(Usuario emisor) { this.emisor = emisor; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public ReportStatus getEstado() { return estado; }
    public void setEstado(ReportStatus estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
}
package com.edustay.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa un contrato de alquiler vigente.
 * Garantiza que una habitación no sea alquilada por dos personas simultáneamente.
 */
@Entity
@Table(name = "alquileres_activos")
public class AlquilerActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false, unique = true)
    @NotNull(message = "La habitación es obligatoria")
    private Habitacion habitacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    @NotNull(message = "El estudiante es obligatorio")
    private Usuario estudiante;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaInicio;

    @NotNull(message = "El monto pactado es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false)
    private Double montoPactado;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public AlquilerActivo() {
    }

    /**
     * Al persistir, si la fecha de inicio no fue enviada manualmente,
     * se asume que el alquiler comienza en el momento del registro.
     */
    @PrePersist
    protected void onCreate() {
        if (this.fechaInicio == null) {
            this.fechaInicio = LocalDateTime.now();
        }
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public Double getMontoPactado() { return montoPactado; }
    public void setMontoPactado(Double montoPactado) { this.montoPactado = montoPactado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}
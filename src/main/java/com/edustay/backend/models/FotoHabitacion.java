package com.edustay.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad que maneja las habitaciones que los estudiantes marcan
 * como favoritas o a fin con sus intereses
 */
@Entity
@Table(name = "fotos_habitacion")
public class FotoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @NotBlank
    @Column(nullable = false)
    private String url;

    private boolean esPrincipal = false;

    public FotoHabitacion() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public boolean isEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(boolean esPrincipal) { this.esPrincipal = esPrincipal; }
}
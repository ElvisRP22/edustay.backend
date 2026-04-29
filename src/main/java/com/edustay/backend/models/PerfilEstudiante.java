package com.edustay.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad que extiende la información específica para usuarios con rol ESTUDIANTE.
 * Implementa una relación One-to-One compartiendo la Primary Key con la tabla usuarios.
 */
@Entity
@Table(name = "perfiles_estudiantes")
public class PerfilEstudiante {

    @Id
    private Long id; // Este ID será el mismo valor que el ID del Usuario

    @OneToOne
    @MapsId // Esta anotación indica que el ID de esta entidad se copia del ID de Usuario
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank
    private String carrera;

    @Min(1)
    @Max(10)
    private Integer ciclo;

    private String universidad = "UTP Piura";

    @Column(columnDefinition = "TEXT")
    private String preferenciasConvivencia;

    private String fotoCarnetUrl;

    public PerfilEstudiante() {
    }

    public PerfilEstudiante(Usuario usuario) {
        this.usuario = usuario;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getPreferenciasConvivencia() {
        return preferenciasConvivencia;
    }

    public void setPreferenciasConvivencia(String preferenciasConvivencia) {
        this.preferenciasConvivencia = preferenciasConvivencia;
    }

    public String getFotoCarnetUrl() {
        return fotoCarnetUrl;
    }

    public void setFotoCarnetUrl(String fotoCarnetUrl) {
        this.fotoCarnetUrl = fotoCarnetUrl;
    }
}
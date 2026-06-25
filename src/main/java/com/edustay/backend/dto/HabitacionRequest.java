package com.edustay.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

/**
 * DTO para crear o actualizar una habitación
 */
public class HabitacionRequest {
    
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    private Double precio;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;
    
    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    private java.util.Set<Long> servicioIds;
    private java.util.Set<Long> reglaIds;
    private java.util.List<String> fotos;

    // Constructores
    public HabitacionRequest() {}

    public HabitacionRequest(String titulo, String descripcion, Double precio, String direccion, Double latitud, Double longitud) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Set<Long> getServicioIds() {
        return servicioIds;
    }

    public void setServicioIds(Set<Long> servicioIds) {
        this.servicioIds = servicioIds;
    }

    public Set<Long> getReglaIds() {
        return reglaIds;
    }

    public void setReglaIds(java.util.Set<Long> reglaIds) {
        this.reglaIds = reglaIds;
    }

    public java.util.List<String> getFotos() {
        return fotos;
    }

    public void setFotos(java.util.List<String> fotos) {
        this.fotos = fotos;
    }
}

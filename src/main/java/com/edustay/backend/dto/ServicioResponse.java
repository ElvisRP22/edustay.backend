package com.edustay.backend.dto;

/**
 * DTO simple para exponer servicios disponibles y seleccionados en habitaciones.
 */
public class ServicioResponse {

    private Long id;
    private String nombre;

    public ServicioResponse() {
    }

    public ServicioResponse(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
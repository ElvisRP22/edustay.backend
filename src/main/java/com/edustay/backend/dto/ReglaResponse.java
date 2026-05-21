package com.edustay.backend.dto;

/**
 * DTO simple para exponer reglas disponibles y seleccionadas en habitaciones.
 */
public class ReglaResponse {

    private Long id;
    private String descripcion;

    public ReglaResponse() {
    }

    public ReglaResponse(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
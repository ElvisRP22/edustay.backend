package com.edustay.backend.models;

import jakarta.persistence.*;

import java.util.Set;

/**
* Entidad que representa las reglas/restricciones que el Arrendador
* puede definir para el alquiler de sus habitaciones/departamentos.
*/
@Entity
@Table(name="reglas")
public class Regla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String descripcion;

    @ManyToMany(mappedBy = "reglas")
    private Set<Habitacion> habitaciones;

    public Regla() {
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

    public Set<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(Set<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }
}

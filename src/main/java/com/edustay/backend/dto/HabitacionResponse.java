package com.edustay.backend.dto;

import com.edustay.backend.models.enums.RoomStatus;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para respuestas de habitación
 */
public class HabitacionResponse {
    
    private Long id;
    private String titulo;
    private String descripcion;
    private Double precio;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Integer distanciaUtpMinutos;
    private Double distanciaKm;
    private RoomStatus estado;
    private LocalDateTime fechaPublicacion;
    private Long arrendadorId;
    private String arrendadorNombre;
    private java.util.Set<ServicioResponse> servicios;
    private java.util.Set<ReglaResponse> reglas;
    private java.util.List<String> fotos;

    // Constructores
    public HabitacionResponse() {}

    public HabitacionResponse(Long id, String titulo, String descripcion, Double precio, String direccion, 
                             Double latitud, Double longitud, Integer distanciaUtpMinutos, RoomStatus estado, 
                             LocalDateTime fechaPublicacion, Long arrendadorId, String arrendadorNombre,
                             Set<ServicioResponse> servicios, Set<ReglaResponse> reglas) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distanciaUtpMinutos = distanciaUtpMinutos;
        this.estado = estado;
        this.fechaPublicacion = fechaPublicacion;
        this.arrendadorId = arrendadorId;
        this.arrendadorNombre = arrendadorNombre;
        this.servicios = servicios;
        this.reglas = reglas;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Integer getDistanciaUtpMinutos() { return distanciaUtpMinutos; }
    public void setDistanciaUtpMinutos(Integer distanciaUtpMinutos) { this.distanciaUtpMinutos = distanciaUtpMinutos; }

    public RoomStatus getEstado() { return estado; }
    public void setEstado(RoomStatus estado) { this.estado = estado; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public Long getArrendadorId() { return arrendadorId; }
    public void setArrendadorId(Long arrendadorId) { this.arrendadorId = arrendadorId; }

    public String getArrendadorNombre() { return arrendadorNombre; }
    public void setArrendadorNombre(String arrendadorNombre) { this.arrendadorNombre = arrendadorNombre; }

    public Set<ServicioResponse> getServicios() {
        return servicios;
    }

    public void setServicios(Set<ServicioResponse> servicios) {
        this.servicios = servicios;
    }

    public Set<ReglaResponse> getReglas() {
        return reglas;
    }

    public void setReglas(Set<ReglaResponse> reglas) {
        this.reglas = reglas;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public java.util.List<String> getFotos() {
        return fotos;
    }

    public void setFotos(java.util.List<String> fotos) {
        this.fotos = fotos;
    }
}

package com.edustay.backend.models;

import com.edustay.backend.models.enums.RoomStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Entidad que representa una habitación para alquiler en EduStay.
 * Implementa validaciones en capa de aplicación y base de datos.
 */
@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrendador_id", nullable = false)
    @NotNull(message = "La habitación debe tener un arrendador asociado")
    private Usuario arrendador;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    @Column(nullable = false)
    private Double precio;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    private Integer distanciaUtpMinutos;

    private Double latitud;
    private Double longitud;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus estado = RoomStatus.DISPONIBLE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoHabitacion> fotos;

    @ManyToMany
    @JoinTable(
            name = "habitacion_servicios",
            joinColumns = @JoinColumn(name = "habitacion_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private Set<Servicio> servicios;

    @ManyToMany
    @JoinTable(
            name = "habitacion_reglas",
            joinColumns = @JoinColumn(name = "habitacion_id"),
            inverseJoinColumns = @JoinColumn(name = "regla_id")
    )
    private Set<Regla> reglas;



    public Habitacion() {
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getArrendador() { return arrendador; }
    public void setArrendador(Usuario arrendador) { this.arrendador = arrendador; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Integer getDistanciaUtpMinutos() { return distanciaUtpMinutos; }
    public void setDistanciaUtpMinutos(Integer distanciaUtpMinutos) { this.distanciaUtpMinutos = distanciaUtpMinutos; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public RoomStatus getEstado() { return estado; }
    public void setEstado(RoomStatus estado) { this.estado = estado; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }

    public List<FotoHabitacion> getFotos() { return fotos; }
    public void setFotos(List<FotoHabitacion> fotos) { this.fotos = fotos; }

    public Set<Servicio> getServicios() { return servicios; }
    public void setServicios(Set<Servicio> servicios) { this.servicios = servicios; }

    public Set<Regla> getReglas() { return reglas; }
    public void setReglas(Set<Regla> reglas) { this.reglas = reglas; }

}
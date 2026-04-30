package com.edustay.backend.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.edustay.backend.models.enums.UserRole;
import com.edustay.backend.models.enums.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

/**
 * Entidad que representa a los usuarios del sistema EduStay.
 * Se utiliza una estrategia de nombrado CamelCase a Underscores en properties.
 */
@Entity
@Table(name = "usuarios") // Forzamos el plural para el estándar de BD
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String apellido;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // El password nunca sale en los JSON de respuesta
    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String telefono;

    @Column(unique = true)
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole rol;

    private String fotoUrl;

    @Column(nullable = false)
    private boolean emailVerificado = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus identidadVerificada = VerificationStatus.PENDIENTE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // Relación 1:1 con PerfilEstudiante (se define en el otro lado con @JoinColumn)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PerfilEstudiante perfilEstudiante;

    @ManyToMany
    @JoinTable(
            name = "favoritos", // Nombre de la tabla intermedia en la BD
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "habitacion_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "habitacion_id"})
    )
    private Set<Habitacion> habitacionesFavoritas = new HashSet<>();

    public Usuario() {
    }

    // --- Getters y Setters ---

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public UserRole getRol() {
        return rol;
    }

    public void setRol(UserRole rol) {
        this.rol = rol;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public boolean isEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public VerificationStatus getIdentidadVerificada() {
        return identidadVerificada;
    }

    public void setIdentidadVerificada(VerificationStatus identidadVerificada) {
        this.identidadVerificada = identidadVerificada;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    // No incluimos setFechaRegistro para evitar alteraciones manuales

    public PerfilEstudiante getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(PerfilEstudiante perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public Set<Habitacion> getHabitacionesFavoritas() {
        return habitacionesFavoritas;
    }

    public void setHabitacionesFavoritas(Set<Habitacion> habitacionesFavoritas) {
        this.habitacionesFavoritas = habitacionesFavoritas;
    }
}
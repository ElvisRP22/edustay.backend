package com.edustay.backend.dto;

import com.edustay.backend.models.enums.UserRole;
import com.edustay.backend.models.enums.VerificationStatus;
import java.time.LocalDateTime;

/**
 * DTO para representar el resumen de usuario en el panel administrativo.
 */
public class UsuarioAdminResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String dni;
    private UserRole rol;
    private boolean emailVerificado;
    private VerificationStatus identidadVerificada;
    private LocalDateTime fechaRegistro;

    public UsuarioAdminResponse() {}

    public UsuarioAdminResponse(Long id, String nombre, String apellido, String email, String telefono, 
                                String dni, UserRole rol, boolean emailVerificado, 
                                VerificationStatus identidadVerificada, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
        this.rol = rol;
        this.emailVerificado = emailVerificado;
        this.identidadVerificada = identidadVerificada;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public UserRole getRol() { return rol; }
    public void setRol(UserRole rol) { this.rol = rol; }

    public boolean isEmailVerificado() { return emailVerificado; }
    public void setEmailVerificado(boolean emailVerificado) { this.emailVerificado = emailVerificado; }

    public VerificationStatus getIdentidadVerificada() { return identidadVerificada; }
    public void setIdentidadVerificada(VerificationStatus identidadVerificada) { this.identidadVerificada = identidadVerificada; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}

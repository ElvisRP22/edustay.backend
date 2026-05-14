package com.edustay.backend.dto;

import com.edustay.backend.models.enums.VerificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de respuesta para el perfil de un estudiante
 */
@Schema(description = "Perfil completo de un estudiante")
public class PerfilEstudianteResponse {

    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String dni;
    private String fotoUrl;
    private boolean emailVerificado;
    private VerificationStatus identidadVerificada;
    private String carrera;
    private Integer ciclo;
    private String universidad;
    private String preferenciasConvivencia;
    private String fotoCarnetUrl;

    public PerfilEstudianteResponse() {}

    public PerfilEstudianteResponse(Long usuarioId, String nombre, String apellido, String email,
                                     String telefono, String dni, String fotoUrl,
                                     boolean emailVerificado, VerificationStatus identidadVerificada,
                                     String carrera, Integer ciclo, String universidad,
                                     String preferenciasConvivencia, String fotoCarnetUrl) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
        this.fotoUrl = fotoUrl;
        this.emailVerificado = emailVerificado;
        this.identidadVerificada = identidadVerificada;
        this.carrera = carrera;
        this.ciclo = ciclo;
        this.universidad = universidad;
        this.preferenciasConvivencia = preferenciasConvivencia;
        this.fotoCarnetUrl = fotoCarnetUrl;
    }

    // --- Getters y Setters ---

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

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

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public boolean isEmailVerificado() { return emailVerificado; }
    public void setEmailVerificado(boolean emailVerificado) { this.emailVerificado = emailVerificado; }

    public VerificationStatus getIdentidadVerificada() { return identidadVerificada; }
    public void setIdentidadVerificada(VerificationStatus identidadVerificada) {
        this.identidadVerificada = identidadVerificada;
    }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public Integer getCiclo() { return ciclo; }
    public void setCiclo(Integer ciclo) { this.ciclo = ciclo; }

    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }

    public String getPreferenciasConvivencia() { return preferenciasConvivencia; }
    public void setPreferenciasConvivencia(String preferenciasConvivencia) {
        this.preferenciasConvivencia = preferenciasConvivencia;
    }

    public String getFotoCarnetUrl() { return fotoCarnetUrl; }
    public void setFotoCarnetUrl(String fotoCarnetUrl) { this.fotoCarnetUrl = fotoCarnetUrl; }
}

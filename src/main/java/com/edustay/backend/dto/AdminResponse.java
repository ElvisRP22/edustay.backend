package com.edustay.backend.dto;

import java.time.LocalDateTime;

public class AdminResponse {

    private Long usuarioId;
    private String nombre;
    private String email;
    private String rol;
    private Boolean verificado;
    private LocalDateTime fechaRevision;

    public AdminResponse() {
    }

    public AdminResponse(
            Long usuarioId,
            String nombre,
            String email,
            String rol,
            Boolean verificado,
            LocalDateTime fechaRevision) {

        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.verificado = verificado;
        this.fechaRevision = fechaRevision;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public LocalDateTime getFechaRevision() {
        return fechaRevision;
    }
}
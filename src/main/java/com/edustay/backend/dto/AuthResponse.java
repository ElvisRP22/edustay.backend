package com.edustay.backend.dto;

import com.edustay.backend.models.enums.UserRole;

/**
 * DTO para la respuesta de autenticación (login/register)
 */
public class AuthResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String fotoUrl;
    private UserRole rol;
    private String token;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(Long id, String nombre, String apellido, String email, 
                       String telefono, String fotoUrl, UserRole rol, String token) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fotoUrl = fotoUrl;
        this.rol = rol;
        this.token = token;
    }

    public AuthResponse(Long id, String nombre, String apellido, String email, 
                       String telefono, String fotoUrl, UserRole rol, String token, String message) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fotoUrl = fotoUrl;
        this.rol = rol;
        this.token = token;
        this.message = message;
    }

    // Getters y Setters
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public UserRole getRol() {
        return rol;
    }

    public void setRol(UserRole rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

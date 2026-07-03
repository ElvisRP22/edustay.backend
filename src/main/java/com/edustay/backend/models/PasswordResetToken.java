package com.edustay.backend.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * Entidad que representa los tokens para restablecer la contraseña.
 */
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    @Column(nullable = false)
    private boolean usado = false;

    public PasswordResetToken() {
    }

    public PasswordResetToken(Usuario usuario, String token, LocalDateTime expiracion) {
        this.usuario = usuario;
        this.token = token;
        this.expiracion = expiracion;
        this.usado = false;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(LocalDateTime expiracion) {
        this.expiracion = expiracion;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}

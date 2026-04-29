package com.edustay.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;
/**
 * Entidad que representa a los códigos OTP que se usaran para verificación
 * mediante email
 */
@Entity
public class CodigoOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private String codigo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime expiracion;

    @Column(nullable = false)
    private boolean usado = false;

    public CodigoOtp() {}

    // Constructor útil para el Service
    public CodigoOtp(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Este método se ejecuta automáticamente justo antes de
     * insertar el registro en la base de datos.
     */
    @PrePersist
    protected void onCreate() {
        // 1. Generación automática del código de 6 dígitos (ej: 054210)
        Random random = new Random();
        this.codigo = String.format("%06d", random.nextInt(1000000));

        // 2. Definición automática de la expiración (10 minutos desde ahora)
        this.expiracion = LocalDateTime.now().plusMinutes(10);
    }

    // Getters
    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getCodigo() { return codigo; }
    public LocalDateTime getExpiracion() { return expiracion; }
    public boolean isUsado() { return usado; }

    // Setters Necesarios
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setUsado(boolean usado) { this.usado = usado; }

    // No incluimos Setters para 'codigo' ni 'expiracion' para proteger la integridad del OTP.
}
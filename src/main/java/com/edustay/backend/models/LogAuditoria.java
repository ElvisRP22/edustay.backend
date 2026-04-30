package com.edustay.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que almacenara los logs de las actividades
 * críticas que realicen los usuarios
 */
@Entity
@Table(name = "logs_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100) // Limitar el largo ayuda a la DB
    private String accion;

    @Column(nullable = false, updatable = false) // Un log nunca debería editarse
    private LocalDateTime fecha;

    // --- Constructor, Getters y Setters ---

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now(); // Se asigna la fecha automáticamente al guardar
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public LocalDateTime getFecha() { return fecha; }
}
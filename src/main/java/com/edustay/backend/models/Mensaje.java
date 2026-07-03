package com.edustay.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa la mensajería interna entre Estudiantes y Arrendadores.
 * El chat siempre está vinculado a una habitación específica para dar contexto.
 */
@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id", nullable = false)
    private Usuario emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id", nullable = false)
    private Usuario receptor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private boolean leido = false;

    @Column(nullable = false)
    private boolean moderado = false;

    @Column(length = 100)
    private String categoriaModeracion;

    @Column(nullable = false)
    private boolean bloqueado = false;

    @Column(length = 50)
    private String estadoModeracion;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    public Mensaje() {
    }

    public Mensaje(Usuario emisor, Usuario receptor, Habitacion habitacion, String contenido) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.habitacion = habitacion;
        this.contenido = contenido;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public boolean isModerado() {
        return moderado;
    }

    public void setModerado(boolean moderado) {
        this.moderado = moderado;
    }

    public String getCategoriaModeracion() {
        return categoriaModeracion;
    }

    public void setCategoriaModeracion(String categoriaModeracion) {
        this.categoriaModeracion = categoriaModeracion;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getEstadoModeracion() {
        return estadoModeracion;
    }

    public void setEstadoModeracion(String estadoModeracion) {
        this.estadoModeracion = estadoModeracion;
    }
}
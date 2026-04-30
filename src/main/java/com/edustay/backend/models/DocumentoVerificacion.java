package com.edustay.backend.models;

import com.edustay.backend.models.enums.DocType;
import com.edustay.backend.models.enums.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que almacena los documentos probatorios para la verificación de identidad.
 * Un usuario (Arrendador o Estudiante) puede subir múltiples documentos.
 */
@Entity
@Table(name = "documentos_verificacion")
public class DocumentoVerificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocType tipo;

    @NotBlank
    @Column(nullable = false)
    private String archivoUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaSubida;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus estado = VerificationStatus.PENDIENTE;

    @Column(columnDefinition = "TEXT")
    private String comentarioAdmin;

    public DocumentoVerificacion() {
    }

    public DocumentoVerificacion(Usuario usuario, DocType tipo, String archivoUrl) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.archivoUrl = archivoUrl;
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

    public DocType getTipo() {
        return tipo;
    }

    public void setTipo(DocType tipo) {
        this.tipo = tipo;
    }

    public String getArchivoUrl() {
        return archivoUrl;
    }

    public void setArchivoUrl(String archivoUrl) {
        this.archivoUrl = archivoUrl;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    // No setFechaSubida por ser @CreationTimestamp

    public VerificationStatus getEstado() {
        return estado;
    }

    public void setEstado(VerificationStatus estado) {
        this.estado = estado;
    }

    public String getComentarioAdmin() {
        return comentarioAdmin;
    }

    public void setComentarioAdmin(String comentarioAdmin) {
        this.comentarioAdmin = comentarioAdmin;
    }
}
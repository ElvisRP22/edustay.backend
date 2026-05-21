package com.edustay.backend.dto;

import com.edustay.backend.models.enums.VerificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resumen del estado de perfil y verificación del usuario autenticado.
 * Sirve al frontend para renderizar el flujo end to end en una sola llamada.
 */
@Schema(description = "Resumen del estado de perfil y verificación del usuario autenticado")
public class VerificacionPerfilResponse {

    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String email;
    private boolean emailVerificado;
    private VerificationStatus identidadVerificada;
    private boolean perfilCompleto;
    private boolean perfilRegistrado;
    private String carrera;
    private Integer ciclo;
    private String universidad;
    private String fotoCarnetUrl;
    private long totalDocumentos;
    private long documentosPendientes;
    private long documentosVerificados;
    private long documentosRechazados;
    private String siguientePaso;

    public VerificacionPerfilResponse() {
    }

    public VerificacionPerfilResponse(Long usuarioId, String nombre, String apellido, String email,
                                      boolean emailVerificado, VerificationStatus identidadVerificada,
                                      boolean perfilCompleto, boolean perfilRegistrado,
                                      String carrera, Integer ciclo, String universidad,
                                      String fotoCarnetUrl, long totalDocumentos,
                                      long documentosPendientes, long documentosVerificados,
                                      long documentosRechazados, String siguientePaso) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.emailVerificado = emailVerificado;
        this.identidadVerificada = identidadVerificada;
        this.perfilCompleto = perfilCompleto;
        this.perfilRegistrado = perfilRegistrado;
        this.carrera = carrera;
        this.ciclo = ciclo;
        this.universidad = universidad;
        this.fotoCarnetUrl = fotoCarnetUrl;
        this.totalDocumentos = totalDocumentos;
        this.documentosPendientes = documentosPendientes;
        this.documentosVerificados = documentosVerificados;
        this.documentosRechazados = documentosRechazados;
        this.siguientePaso = siguientePaso;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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

    public boolean isPerfilCompleto() {
        return perfilCompleto;
    }

    public void setPerfilCompleto(boolean perfilCompleto) {
        this.perfilCompleto = perfilCompleto;
    }

    public boolean isPerfilRegistrado() {
        return perfilRegistrado;
    }

    public void setPerfilRegistrado(boolean perfilRegistrado) {
        this.perfilRegistrado = perfilRegistrado;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getFotoCarnetUrl() {
        return fotoCarnetUrl;
    }

    public void setFotoCarnetUrl(String fotoCarnetUrl) {
        this.fotoCarnetUrl = fotoCarnetUrl;
    }

    public long getTotalDocumentos() {
        return totalDocumentos;
    }

    public void setTotalDocumentos(long totalDocumentos) {
        this.totalDocumentos = totalDocumentos;
    }

    public long getDocumentosPendientes() {
        return documentosPendientes;
    }

    public void setDocumentosPendientes(long documentosPendientes) {
        this.documentosPendientes = documentosPendientes;
    }

    public long getDocumentosVerificados() {
        return documentosVerificados;
    }

    public void setDocumentosVerificados(long documentosVerificados) {
        this.documentosVerificados = documentosVerificados;
    }

    public long getDocumentosRechazados() {
        return documentosRechazados;
    }

    public void setDocumentosRechazados(long documentosRechazados) {
        this.documentosRechazados = documentosRechazados;
    }

    public String getSiguientePaso() {
        return siguientePaso;
    }

    public void setSiguientePaso(String siguientePaso) {
        this.siguientePaso = siguientePaso;
    }
}
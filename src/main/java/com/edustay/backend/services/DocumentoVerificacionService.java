package com.edustay.backend.services;

import com.edustay.backend.dto.DocumentoVerificacionRequest;
import com.edustay.backend.dto.VerificacionResponse;

import java.util.List;

/**
 * Servicio para que los usuarios suban sus documentos de verificación
 */
public interface DocumentoVerificacionService {

    /** El usuario sube uno de sus documentos de identidad */
    VerificacionResponse subirDocumento(Long usuarioId, DocumentoVerificacionRequest request);

    /** El usuario consulta sus propios documentos */
    List<VerificacionResponse> obtenerMisDocumentos(Long usuarioId);
}

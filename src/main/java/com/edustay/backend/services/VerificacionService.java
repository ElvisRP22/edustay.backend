package com.edustay.backend.services;

import com.edustay.backend.dto.VerificacionRequest;
import com.edustay.backend.dto.VerificacionResponse;

import java.util.List;

/**
 * Interfaz del servicio de Verificaciones de documentos (uso exclusivo de administradores)
 */
public interface VerificacionService {

    /**
     * Obtiene todos los documentos de verificación (pendientes y procesados)
     */
    List<VerificacionResponse> obtenerTodos();

    /**
     * Obtiene todos los documentos de verificación pendientes de revisión
     */
    List<VerificacionResponse> obtenerPendientes();

    /**
     * Obtiene los documentos de verificación de un usuario específico
     */
    List<VerificacionResponse> obtenerPorUsuario(Long usuarioId);

    /**
     * Cambia el estado de un documento de verificación (Aprobado / Rechazado).
     * Si todos los documentos del usuario quedan en VERIFICADO, actualiza
     * el campo identidad_verificada del usuario a VERIFICADO.
     *
     * @param documentoId ID del documento a actualizar
     * @param request     Contiene el nuevo estado y el comentario opcional del admin
     * @return VerificacionResponse con los datos actualizados
     */
    VerificacionResponse actualizarEstado(Long documentoId, VerificacionRequest request);
}

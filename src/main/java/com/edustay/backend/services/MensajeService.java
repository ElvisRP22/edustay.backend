package com.edustay.backend.services;

import com.edustay.backend.dto.MensajeRequest;
import com.edustay.backend.dto.MensajeResponse;

import java.util.List;

/**
 * Servicio para la mensajería interna entre estudiantes y arrendadores
 */
public interface MensajeService {

    /** Envía un mensaje */
    MensajeResponse enviarMensaje(Long emisorId, MensajeRequest request);

    /** Obtiene la conversación entre dos usuarios sobre una habitación */
    List<MensajeResponse> obtenerConversacion(Long habitacionId, Long usuarioA, Long usuarioB);

    /** Bandeja de entrada del usuario autenticado */
    List<MensajeResponse> obtenerBandejaEntrada(Long receptorId);

    /** Marca todos los mensajes de una conversación como leídos */
    void marcarComoLeido(Long habitacionId, Long emisorId, Long receptorId);

    /** Cantidad de mensajes no leídos */
    long contarNoLeidos(Long receptorId);
}

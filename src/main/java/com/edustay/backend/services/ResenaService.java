package com.edustay.backend.services;

import com.edustay.backend.dto.ResenaRequest;
import com.edustay.backend.dto.ResenaResponse;

import java.util.List;

/**
 * Interfaz del servicio de Reseñas
 */
public interface ResenaService {

    /**
     * Crea una nueva reseña
     */
    ResenaResponse crearResena(ResenaRequest request, Long estudianteId);

    /**
     * Obtiene todas las reseñas de una habitación
     */
    List<ResenaResponse> obtenerPorHabitacion(Long habitacionId);

    /**
     * Obtiene todas las reseñas de un estudiante
     */
    List<ResenaResponse> obtenerPorEstudiante(Long estudianteId);

    /**
     * Obtiene una reseña por ID
     */
    ResenaResponse obtenerPorId(Long id);

    /**
     * Actualiza una reseña
     */
    ResenaResponse actualizarResena(Long id, ResenaRequest request, Long estudianteId);

    /**
     * Elimina una reseña
     */
    void eliminarResena(Long id, Long estudianteId);

    /**
     * Verifica si un estudiante ya calificó una habitación
     */
    boolean yaCalificada(Long estudianteId, Long habitacionId);
}

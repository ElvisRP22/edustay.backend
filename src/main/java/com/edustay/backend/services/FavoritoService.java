package com.edustay.backend.services;

import com.edustay.backend.dto.HabitacionResponse;

import java.util.List;

/**
 * Servicio para gestionar las habitaciones favoritas de un estudiante
 */
public interface FavoritoService {

    /** Agrega una habitación a favoritos */
    void agregarFavorito(Long estudianteId, Long habitacionId);

    /** Elimina una habitación de favoritos */
    void eliminarFavorito(Long estudianteId, Long habitacionId);

    /** Lista las habitaciones favoritas del estudiante */
    List<HabitacionResponse> obtenerFavoritos(Long estudianteId);

    /** Verifica si una habitación está en favoritos */
    boolean esFavorito(Long estudianteId, Long habitacionId);
}

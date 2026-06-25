package com.edustay.backend.services;

import com.edustay.backend.dto.HabitacionRequest;
import com.edustay.backend.dto.HabitacionResponse;

import java.util.List;

/**
 * Interfaz del servicio de Habitaciones
 */
public interface HabitacionService {

    /**
     * Crea una nueva habitación
     */
    HabitacionResponse crearHabitacion(HabitacionRequest request, Long arrendadorId);

    /**
     * Obtiene todas las habitaciones
     */
    List<HabitacionResponse> obtenerTodas();

    /**
     * Obtiene una habitación por ID
     */
    HabitacionResponse obtenerPorId(Long id);

    /**
     * Obtiene habitaciones de un arrendador
     */
    List<HabitacionResponse> obtenerPorArrendador(Long arrendadorId);

    /**
     * Obtiene habitaciones disponibles
     */
    List<HabitacionResponse> obtenerDisponibles();

    /**
     * Actualiza una habitación
     */
    HabitacionResponse actualizarHabitacion(Long id, HabitacionRequest request, Long arrendadorId);

    /**
     * Elimina una habitación
     */
    void eliminarHabitacion(Long id, Long arrendadorId);

    /**
     * Busca habitaciones por geolocalización, precio, término de búsqueda y estado
     */
    List<HabitacionResponse> buscarHabitaciones(Double lat, Double lon, Double radioKm, Double maxPrecio, String query, Boolean soloDisponibles);
}

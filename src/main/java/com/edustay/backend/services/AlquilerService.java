package com.edustay.backend.services;

import com.edustay.backend.dto.AlquilerRequest;
import com.edustay.backend.dto.AlquilerResponse;

import java.util.List;

/**
 * Servicio para la gestión de alquileres activos
 */
public interface AlquilerService {

    /** Registra un nuevo alquiler (el estudiante alquila una habitación) */
    AlquilerResponse crearAlquiler(Long estudianteId, AlquilerRequest request);

    /** El administrador o propietario finaliza un alquiler */
    void finalizarAlquiler(Long alquilerId, Long solicitanteId);

    /** Mis alquileres (como estudiante) */
    List<AlquilerResponse> obtenerAlquileresDeEstudiante(Long estudianteId);

    /** Alquileres de mis habitaciones (como arrendador) */
    List<AlquilerResponse> obtenerAlquileresDeArrendador(Long arrendadorId);

    /** Todos los alquileres (admin) */
    List<AlquilerResponse> obtenerTodos();

    /** Detalle de un alquiler por ID */
    AlquilerResponse obtenerPorId(Long id);
}

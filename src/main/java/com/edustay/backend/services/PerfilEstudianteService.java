package com.edustay.backend.services;

import com.edustay.backend.dto.PerfilEstudianteRequest;
import com.edustay.backend.dto.PerfilEstudianteResponse;

/**
 * Servicio para la gestión del perfil del estudiante
 */
public interface PerfilEstudianteService {

    /** Obtiene el perfil completo de un estudiante */
    PerfilEstudianteResponse obtenerPerfil(Long usuarioId);

    /** Crea o actualiza el perfil extendido del estudiante */
    PerfilEstudianteResponse guardarPerfil(Long usuarioId, PerfilEstudianteRequest request);
}

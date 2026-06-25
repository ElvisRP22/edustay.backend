package com.edustay.backend.services;

import com.edustay.backend.dto.ReporteRequest;
import com.edustay.backend.dto.ReporteResponse;

import java.util.List;

/**
 * Servicio para gestión de reportes de habitaciones
 */
public interface ReporteService {

    /** Crea un reporte sobre una habitación */
    ReporteResponse crearReporte(Long emisorId, ReporteRequest request);

    /** Obtiene todos los reportes (admin) */
    List<ReporteResponse> obtenerTodos();

    /** Obtiene reportes de una habitación */
    List<ReporteResponse> obtenerPorHabitacion(Long habitacionId);

    List<ReporteResponse> obtenerMisReportes(Long emisorId);

    /** Actualiza el estado de un reporte (admin) */
    ReporteResponse actualizarEstado(Long id, com.edustay.backend.models.enums.ReportStatus nuevoEstado);
}

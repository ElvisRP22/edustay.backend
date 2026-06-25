package com.edustay.backend.services.impl;

import com.edustay.backend.dto.ReporteRequest;
import com.edustay.backend.dto.ReporteResponse;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.Reporte;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.repositories.HabitacionRepository;
import com.edustay.backend.repositories.ReporteRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Reportes de habitaciones
 */
@Service
@Transactional
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    public ReporteResponse crearReporte(Long emisorId, ReporteRequest request) {
        Usuario emisor = usuarioRepository.findById(emisorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + emisorId));

        Habitacion habitacion = habitacionRepository.findById(request.getHabitacionId())
                .orElseThrow(
                        () -> new RuntimeException("Habitación no encontrada con id: " + request.getHabitacionId()));

        Reporte reporte = new Reporte();
        reporte.setEmisor(emisor);
        reporte.setHabitacion(habitacion);
        reporte.setMotivo(request.getMotivo());
        reporte.setDescripcion(request.getDescripcion());

        reporteRepository.save(reporte);
        return convertirAResponse(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> obtenerTodos() {
        return reporteRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> obtenerPorHabitacion(Long habitacionId) {
        return reporteRepository.findByHabitacionId(habitacionId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> obtenerMisReportes(Long emisorId) {
        return reporteRepository.findByEmisorId(emisorId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private ReporteResponse convertirAResponse(Reporte r) {
        return new ReporteResponse(
                r.getId(),
                r.getEmisor().getId(),
                r.getEmisor().getNombre() + " " + r.getEmisor().getApellido(),
                r.getHabitacion().getId(),
                r.getHabitacion().getTitulo(),
                r.getMotivo(),
                r.getDescripcion(),
                r.getEstado(),
                r.getFecha());
    }

    @Override
    public ReporteResponse actualizarEstado(Long id, com.edustay.backend.models.enums.ReportStatus nuevoEstado) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        reporte.setEstado(nuevoEstado);
        reporteRepository.save(reporte);
        return convertirAResponse(reporte);
    }
}

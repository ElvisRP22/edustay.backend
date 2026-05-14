package com.edustay.backend.services.impl;

import com.edustay.backend.dto.ResenaRequest;
import com.edustay.backend.dto.ResenaResponse;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.Resena;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.repositories.HabitacionRepository;
import com.edustay.backend.repositories.ResenaRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Reseñas
 */
@Service
@Transactional
public class ResenaServiceImpl implements ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    public ResenaResponse crearResena(ResenaRequest request, Long estudianteId) {
        // Verificar que el estudiante no haya calificado ya esta habitación
        if (yaCalificada(estudianteId, request.getHabitacionId())) {
            throw new RuntimeException("Ya has calificado esta habitación anteriormente");
        }

        Usuario estudiante = usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Habitacion habitacion = habitacionRepository.findById(request.getHabitacionId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        Resena resena = new Resena();
        resena.setEstudiante(estudiante);
        resena.setHabitacion(habitacion);
        resena.setCalificacion(request.getCalificacion());
        resena.setComentario(request.getComentario());

        Resena guardada = resenaRepository.save(resena);
        return convertirAResponse(guardada);
    }

    @Override
    public List<ResenaResponse> obtenerPorHabitacion(Long habitacionId) {
        return resenaRepository.findByHabitacionId(habitacionId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResenaResponse> obtenerPorEstudiante(Long estudianteId) {
        return resenaRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResenaResponse obtenerPorId(Long id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        return convertirAResponse(resena);
    }

    @Override
    public ResenaResponse actualizarResena(Long id, ResenaRequest request, Long estudianteId) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        // Verificar que el estudiante sea el autor de la reseña
        if (!resena.getEstudiante().getId().equals(estudianteId)) {
            throw new RuntimeException("No tienes permiso para actualizar esta reseña");
        }

        resena.setCalificacion(request.getCalificacion());
        resena.setComentario(request.getComentario());

        Resena actualizada = resenaRepository.save(resena);
        return convertirAResponse(actualizada);
    }

    @Override
    public void eliminarResena(Long id, Long estudianteId) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        // Verificar que el estudiante sea el autor de la reseña
        if (!resena.getEstudiante().getId().equals(estudianteId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta reseña");
        }

        resenaRepository.delete(resena);
    }

    @Override
    public boolean yaCalificada(Long estudianteId, Long habitacionId) {
        return resenaRepository.existsByEstudianteIdAndHabitacionId(estudianteId, habitacionId);
    }

    /**
     * Convierte un Resena a ResenaResponse
     */
    private ResenaResponse convertirAResponse(Resena resena) {
        return new ResenaResponse(
                resena.getId(),
                resena.getCalificacion(),
                resena.getComentario(),
                resena.getFecha(),
                resena.getEstudiante().getId(),
                resena.getEstudiante().getNombre(),
                resena.getHabitacion().getId(),
                resena.getHabitacion().getTitulo()
        );
    }
}

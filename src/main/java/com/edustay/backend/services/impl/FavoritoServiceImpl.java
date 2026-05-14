package com.edustay.backend.services.impl;

import com.edustay.backend.dto.HabitacionResponse;
import com.edustay.backend.models.Favorito;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.repositories.FavoritoRepository;
import com.edustay.backend.repositories.HabitacionRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Favoritos
 */
@Service
@Transactional
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    public void agregarFavorito(Long estudianteId, Long habitacionId) {
        if (favoritoRepository.existsByEstudianteIdAndHabitacionId(estudianteId, habitacionId)) {
            throw new RuntimeException("La habitación ya está en favoritos");
        }

        Usuario estudiante = usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + estudianteId));

        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con id: " + habitacionId));

        favoritoRepository.save(new Favorito(estudiante, habitacion));
    }

    @Override
    public void eliminarFavorito(Long estudianteId, Long habitacionId) {
        if (!favoritoRepository.existsByEstudianteIdAndHabitacionId(estudianteId, habitacionId)) {
            throw new RuntimeException("La habitación no está en favoritos");
        }
        favoritoRepository.deleteByEstudianteIdAndHabitacionId(estudianteId, habitacionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> obtenerFavoritos(Long estudianteId) {
        return favoritoRepository.findByEstudianteId(estudianteId).stream()
                .map(fav -> convertirHabitacion(fav.getHabitacion()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean esFavorito(Long estudianteId, Long habitacionId) {
        return favoritoRepository.existsByEstudianteIdAndHabitacionId(estudianteId, habitacionId);
    }

    private HabitacionResponse convertirHabitacion(Habitacion h) {
        return new HabitacionResponse(
                h.getId(),
                h.getTitulo(),
                h.getDescripcion(),
                h.getPrecio(),
                h.getDireccion(),
                h.getLatitud(),
                h.getLongitud(),
                h.getDistanciaUtpMinutos(),
                h.getEstado(),
                h.getFechaPublicacion(),
                h.getArrendador().getId(),
                h.getArrendador().getNombre()
        );
    }
}

package com.edustay.backend.services.impl;

import com.edustay.backend.dto.PerfilEstudianteRequest;
import com.edustay.backend.dto.PerfilEstudianteResponse;
import com.edustay.backend.models.PerfilEstudiante;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.repositories.PerfilEstudianteRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.PerfilEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de Perfil del Estudiante
 */
@Service
@Transactional
public class PerfilEstudianteServiceImpl implements PerfilEstudianteService {

    @Autowired
    private PerfilEstudianteRepository perfilEstudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public PerfilEstudianteResponse obtenerPerfil(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        PerfilEstudiante perfil = perfilEstudianteRepository.findByUsuarioId(usuarioId)
                .orElse(null);

        return construirResponse(usuario, perfil);
    }

    @Override
    public PerfilEstudianteResponse guardarPerfil(Long usuarioId, PerfilEstudianteRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        PerfilEstudiante perfil = perfilEstudianteRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> new PerfilEstudiante(usuario));

        perfil.setCarrera(request.getCarrera());
        perfil.setCiclo(request.getCiclo());
        perfil.setPreferenciasConvivencia(request.getPreferenciasConvivencia());
        if (request.getFotoCarnetUrl() != null) {
            perfil.setFotoCarnetUrl(request.getFotoCarnetUrl());
        }

        perfilEstudianteRepository.save(perfil);
        return construirResponse(usuario, perfil);
    }

    private PerfilEstudianteResponse construirResponse(Usuario usuario, PerfilEstudiante perfil) {
        return new PerfilEstudianteResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getDni(),
                usuario.getFotoUrl(),
                usuario.isEmailVerificado(),
                usuario.getIdentidadVerificada(),
                perfil != null ? perfil.getCarrera() : null,
                perfil != null ? perfil.getCiclo() : null,
                perfil != null ? perfil.getUniversidad() : "UTP Piura",
                perfil != null ? perfil.getPreferenciasConvivencia() : null,
                perfil != null ? perfil.getFotoCarnetUrl() : null
        );
    }
}

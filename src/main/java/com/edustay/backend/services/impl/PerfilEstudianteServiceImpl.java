package com.edustay.backend.services.impl;

import com.edustay.backend.dto.VerificacionPerfilResponse;
import com.edustay.backend.dto.PerfilEstudianteRequest;
import com.edustay.backend.dto.PerfilEstudianteResponse;
import com.edustay.backend.models.PerfilEstudiante;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.VerificationStatus;
import com.edustay.backend.repositories.DocumentoVerificacionRepository;
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

    @Autowired
    private DocumentoVerificacionRepository documentoVerificacionRepository;

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

        @Override
        @Transactional(readOnly = true)
        public VerificacionPerfilResponse obtenerResumenVerificacion(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        PerfilEstudiante perfil = perfilEstudianteRepository.findByUsuarioId(usuarioId)
            .orElse(null);

        long totalDocumentos = documentoVerificacionRepository.countByUsuarioId(usuarioId);
        long documentosPendientes = documentoVerificacionRepository.countByUsuarioIdAndEstado(usuarioId, VerificationStatus.PENDIENTE);
        long documentosVerificados = documentoVerificacionRepository.countByUsuarioIdAndEstado(usuarioId, VerificationStatus.VERIFICADO);
        long documentosRechazados = documentoVerificacionRepository.countByUsuarioIdAndEstado(usuarioId, VerificationStatus.RECHAZADO);

        boolean perfilCompleto = perfil != null
            && tieneTexto(perfil.getCarrera())
            && perfil.getCiclo() != null
            && tieneTexto(perfil.getPreferenciasConvivencia());

        String siguientePaso = determinarSiguientePaso(usuario.getIdentidadVerificada(), perfilCompleto, totalDocumentos,
            documentosPendientes, documentosRechazados);

        return new VerificacionPerfilResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            usuario.isEmailVerificado(),
            usuario.getIdentidadVerificada(),
            perfilCompleto,
            perfil != null,
            perfil != null ? perfil.getCarrera() : null,
            perfil != null ? perfil.getCiclo() : null,
            perfil != null ? perfil.getUniversidad() : "UTP Piura",
            perfil != null ? perfil.getFotoCarnetUrl() : null,
            totalDocumentos,
            documentosPendientes,
            documentosVerificados,
            documentosRechazados,
            siguientePaso
        );
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

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private String determinarSiguientePaso(VerificationStatus estado,
                                           boolean perfilCompleto,
                                           long totalDocumentos,
                                           long documentosPendientes,
                                           long documentosRechazados) {
        if (!perfilCompleto) {
            return "Completa tu perfil académico antes de enviar documentos";
        }

        if (totalDocumentos == 0) {
            return "Sube tus documentos de verificación";
        }

        if (documentosRechazados > 0) {
            return "Revisa los documentos rechazados y vuelve a subirlos";
        }

        if (documentosPendientes > 0 || estado == VerificationStatus.EN_PROCESO) {
            return "Tus documentos están en revisión";
        }

        if (estado == VerificationStatus.VERIFICADO) {
            return "Tu perfil ya está verificado";
        }

        return "Envía tus documentos de verificación";
    }
}

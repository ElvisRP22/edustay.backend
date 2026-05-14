package com.edustay.backend.services.impl;

import com.edustay.backend.dto.DocumentoVerificacionRequest;
import com.edustay.backend.dto.VerificacionResponse;
import com.edustay.backend.models.DocumentoVerificacion;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.VerificationStatus;
import com.edustay.backend.repositories.DocumentoVerificacionRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.DocumentoVerificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de subida de documentos de verificación (lado del usuario)
 */
@Service
@Transactional
public class DocumentoVerificacionServiceImpl implements DocumentoVerificacionService {

    @Autowired
    private DocumentoVerificacionRepository documentoVerificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public VerificacionResponse subirDocumento(Long usuarioId, DocumentoVerificacionRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        DocumentoVerificacion doc = new DocumentoVerificacion(usuario, request.getTipo(), request.getArchivoUrl());
        documentoVerificacionRepository.save(doc);

        // Al subir el primer documento el usuario pasa a EN_PROCESO si aún estaba PENDIENTE
        if (usuario.getIdentidadVerificada() == VerificationStatus.PENDIENTE) {
            usuario.setIdentidadVerificada(VerificationStatus.EN_PROCESO);
            usuarioRepository.save(usuario);
        }

        return convertirAResponse(doc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificacionResponse> obtenerMisDocumentos(Long usuarioId) {
        return documentoVerificacionRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private VerificacionResponse convertirAResponse(DocumentoVerificacion doc) {
        Usuario usuario = doc.getUsuario();
        String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
        return new VerificacionResponse(
                doc.getId(),
                usuario.getId(),
                nombreCompleto,
                usuario.getEmail(),
                doc.getTipo(),
                doc.getArchivoUrl(),
                doc.getFechaSubida(),
                doc.getEstado(),
                doc.getComentarioAdmin(),
                usuario.getIdentidadVerificada()
        );
    }
}

package com.edustay.backend.services.impl;

import com.edustay.backend.dto.VerificacionRequest;
import com.edustay.backend.dto.VerificacionResponse;
import com.edustay.backend.models.DocumentoVerificacion;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.VerificationStatus;
import com.edustay.backend.repositories.DocumentoVerificacionRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.VerificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Verificaciones de documentos
 */
@Service
@Transactional
public class VerificacionServiceImpl implements VerificacionService {

    @Autowired
    private DocumentoVerificacionRepository documentoVerificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VerificacionResponse> obtenerTodos() {
        return documentoVerificacionRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificacionResponse> obtenerPendientes() {
        return documentoVerificacionRepository.findByEstado(VerificationStatus.PENDIENTE).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificacionResponse> obtenerPorUsuario(Long usuarioId) {
        return documentoVerificacionRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cambia el estado del documento y, si el nuevo estado es VERIFICADO,
     * comprueba si todos los documentos del usuario ya están verificados
     * para actualizar el campo identidad_verificada del usuario.
     * Si el estado es RECHAZADO, el campo identidad_verificada se marca como RECHAZADO.
     */
    @Override
    public VerificacionResponse actualizarEstado(Long documentoId, VerificacionRequest request) {
        // Validar que solo se acepten VERIFICADO o RECHAZADO
        VerificationStatus nuevoEstado = request.getEstado();
        if (nuevoEstado != VerificationStatus.VERIFICADO && nuevoEstado != VerificationStatus.RECHAZADO) {
            throw new IllegalArgumentException("Estado no permitido. Use VERIFICADO o RECHAZADO.");
        }

        // Buscar el documento
        DocumentoVerificacion documento = documentoVerificacionRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento de verificación no encontrado con id: " + documentoId));

        // Actualizar el documento
        documento.setEstado(nuevoEstado);
        documento.setComentarioAdmin(request.getComentarioAdmin());
        documentoVerificacionRepository.save(documento);

        // Actualizar identidad_verificada del usuario
        actualizarIdentidadUsuario(documento.getUsuario(), nuevoEstado);

        return convertirAResponse(documento);
    }

    /**
     * Evalúa el estado global de identidad del usuario según sus documentos.
     *
     * - Si el nuevo estado es RECHAZADO → el usuario queda como RECHAZADO de inmediato.
     * - Si el nuevo estado es VERIFICADO → comprueba si quedan documentos sin verificar:
     *     · Si no hay documentos pendientes/rechazados → marca al usuario como VERIFICADO.
     *     · Si aún quedan pendientes → deja al usuario en EN_PROCESO.
     */
    private void actualizarIdentidadUsuario(Usuario usuario, VerificationStatus nuevoEstadoDocumento) {
        if (nuevoEstadoDocumento == VerificationStatus.RECHAZADO) {
            // Un rechazo implica que la identidad global queda rechazada
            usuario.setIdentidadVerificada(VerificationStatus.RECHAZADO);
        } else {
            // Verificar si todavía existen documentos que NO estén verificados
            boolean quedanDocumentosSinVerificar = documentoVerificacionRepository
                    .existsByUsuarioIdAndEstadoNot(usuario.getId(), VerificationStatus.VERIFICADO);

            if (quedanDocumentosSinVerificar) {
                // Hay al menos un documento pendiente → el proceso continúa
                usuario.setIdentidadVerificada(VerificationStatus.EN_PROCESO);
            } else {
                // Todos los documentos están verificados → identidad confirmada
                usuario.setIdentidadVerificada(VerificationStatus.VERIFICADO);
            }
        }
        usuarioRepository.save(usuario);
    }

    /**
     * Convierte un DocumentoVerificacion en VerificacionResponse
     */
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

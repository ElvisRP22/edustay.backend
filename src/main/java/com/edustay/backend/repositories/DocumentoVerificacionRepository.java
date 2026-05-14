package com.edustay.backend.repositories;

import com.edustay.backend.models.DocumentoVerificacion;
import com.edustay.backend.models.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de documentos de verificación
 */
@Repository
public interface DocumentoVerificacionRepository extends JpaRepository<DocumentoVerificacion, Long> {

    /**
     * Obtiene todos los documentos de un usuario específico
     */
    List<DocumentoVerificacion> findByUsuarioId(Long usuarioId);

    /**
     * Obtiene documentos por estado
     */
    List<DocumentoVerificacion> findByEstado(VerificationStatus estado);

    /**
     * Verifica si todos los documentos de un usuario están verificados
     */
    boolean existsByUsuarioIdAndEstadoNot(Long usuarioId, VerificationStatus estado);
}

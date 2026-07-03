package com.edustay.backend.repositories;

import com.edustay.backend.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para mensajes
 */
@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    /**
     * Conversación entre dos usuarios sobre una habitación, ordenada cronológicamente
     */
    @Query("SELECT m FROM Mensaje m WHERE m.habitacion.id = :habitacionId " +
           "AND ((m.emisor.id = :usuarioA AND m.receptor.id = :usuarioB) " +
           "  OR (m.emisor.id = :usuarioB AND m.receptor.id = :usuarioA)) " +
           "ORDER BY m.fechaEnvio ASC")
    List<Mensaje> findConversacion(@Param("habitacionId") Long habitacionId,
                                   @Param("usuarioA") Long usuarioA,
                                   @Param("usuarioB") Long usuarioB);

    /**
     * Todos los mensajes recibidos por un usuario (bandeja de entrada)
     */
    List<Mensaje> findByReceptorIdOrderByFechaEnvioDesc(Long receptorId);

    /**
     * Mensajes no leídos de un receptor
     */
    List<Mensaje> findByReceptorIdAndLeidoFalse(Long receptorId);

    /**
     * Cantidad de mensajes no leídos
     */
    long countByReceptorIdAndLeidoFalse(Long receptorId);

    /**
     * Mensajes enviados por un usuario
     */
    List<Mensaje> findByEmisorIdOrderByFechaEnvioDesc(Long emisorId);

    /**
     * Mensajes que han sido marcados como moderados/sospechosos
     */
    List<Mensaje> findByModeradoTrueOrderByFechaEnvioDesc();

    List<Mensaje> findByEstadoModeracionIsNotNullOrderByFechaEnvioDesc();
}

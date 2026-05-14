package com.edustay.backend.repositories;

import com.edustay.backend.models.PerfilEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para perfiles de estudiantes
 */
@Repository
public interface PerfilEstudianteRepository extends JpaRepository<PerfilEstudiante, Long> {

    Optional<PerfilEstudiante> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
}

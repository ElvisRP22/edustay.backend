package com.edustay.backend.repositories;

import com.edustay.backend.models.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para habitaciones favoritas
 */
@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByEstudianteId(Long estudianteId);

    Optional<Favorito> findByEstudianteIdAndHabitacionId(Long estudianteId, Long habitacionId);

    boolean existsByEstudianteIdAndHabitacionId(Long estudianteId, Long habitacionId);

    @Transactional
    @Modifying
    void deleteByEstudianteIdAndHabitacionId(Long estudianteId, Long habitacionId);
}

package com.edustay.backend.repositories;

import com.edustay.backend.models.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByHabitacionId(Long habitacionId);
    List<Resena> findByEstudianteId(Long estudianteId);
    Optional<Resena> findByEstudianteIdAndHabitacionId(Long estudianteId, Long habitacionId);
    boolean existsByEstudianteIdAndHabitacionId(Long estudianteId, Long habitacionId);
}

package com.edustay.backend.repositories;

import com.edustay.backend.models.AlquilerActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para alquileres activos
 */
@Repository
public interface AlquilerActivoRepository extends JpaRepository<AlquilerActivo, Long> {

    /** Alquileres de un estudiante */
    List<AlquilerActivo> findByEstudianteId(Long estudianteId);

    /** Alquileres de habitaciones de un arrendador */
    List<AlquilerActivo> findByHabitacionArrendadorId(Long arrendadorId);

    /** Verifica si una habitación ya está alquilada */
    boolean existsByHabitacionId(Long habitacionId);

    /** Busca el alquiler activo de una habitación */
    Optional<AlquilerActivo> findByHabitacionId(Long habitacionId);
}

package com.edustay.backend.repositories;

import com.edustay.backend.models.Reporte;
import com.edustay.backend.models.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para reportes de habitaciones
 */
@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByEmisorId(Long emisorId);

    List<Reporte> findByHabitacionId(Long habitacionId);

    List<Reporte> findByEstado(ReportStatus estado);
}

package com.edustay.backend.repositories;

import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByArrendadorId(Long arrendadorId);
    List<Habitacion> findByEstado(RoomStatus estado);
    Optional<Habitacion> findByIdAndArrendadorId(Long id, Long arrendadorId);
}

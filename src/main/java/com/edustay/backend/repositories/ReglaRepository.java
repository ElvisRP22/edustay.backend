package com.edustay.backend.repositories;

import com.edustay.backend.models.Regla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReglaRepository extends JpaRepository<Regla, Long> {
}

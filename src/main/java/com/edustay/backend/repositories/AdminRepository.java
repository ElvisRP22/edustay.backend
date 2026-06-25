package com.edustay.backend.repositories;

import com.edustay.backend.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUsuarioId(Long usuarioId);

    Long countByVerificado(Boolean verificado);
}
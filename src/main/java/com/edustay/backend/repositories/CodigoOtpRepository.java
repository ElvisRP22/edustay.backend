package com.edustay.backend.repositories;

import com.edustay.backend.models.CodigoOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodigoOtpRepository extends JpaRepository<CodigoOtp, Long> {
    Optional<CodigoOtp> findFirstByUsuarioEmailAndCodigoAndUsadoFalseOrderByExpiracionDesc(String email, String codigo);
    List<CodigoOtp> findByUsuarioIdAndUsadoFalse(Long usuarioId);
}

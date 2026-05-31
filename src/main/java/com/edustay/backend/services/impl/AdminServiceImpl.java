package com.edustay.backend.services.impl;

import com.edustay.backend.dto.AdminRequest;
import com.edustay.backend.dto.AdminResponse;
import com.edustay.backend.models.Admin;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.repositories.AdminRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<AdminResponse> obtenerUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .map(u -> {

                    Admin verificacion = adminRepository
                            .findByUsuarioId(u.getId())
                            .orElse(null);

                    return new AdminResponse(
                            u.getId(),
                            u.getNombre() + " " + u.getApellido(),
                            u.getEmail(),
                            u.getRol().name(),
                            verificacion != null && verificacion.getVerificado(),
                            verificacion != null ? verificacion.getFechaRevision() : null
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminResponse> obtenerPendientes() {

        return obtenerUsuarios()
                .stream()
                .filter(u -> !u.getVerificado())
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponse verificarCuenta(AdminRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Admin admin = adminRepository.findByUsuarioId(usuario.getId())
                .orElse(new Admin());

        admin.setUsuario(usuario);
        admin.setVerificado(request.getVerificado());
        admin.setObservacion(request.getObservacion());
        admin.setFechaRevision(LocalDateTime.now());

        adminRepository.save(admin);

        return new AdminResponse(
                usuario.getId(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name(),
                admin.getVerificado(),
                admin.getFechaRevision()
        );
    }

    @Override
    public Long totalUsuarios() {
        return usuarioRepository.count();
    }

    @Override
    public Long totalPendientes() {
        return adminRepository.countByVerificado(false);
    }

    @Override
    public Long totalClientes() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getRol().name().equals("CLIENTE"))
                .count();
    }

    @Override
    public Long totalArrendadores() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getRol().name().equals("ARRENDADOR"))
                .count();
    }
}
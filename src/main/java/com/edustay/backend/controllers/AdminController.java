package com.edustay.backend.controllers;

import com.edustay.backend.dto.AdminRequest;
import com.edustay.backend.dto.AdminResponse;
import com.edustay.backend.services.AdminService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<AdminResponse>> obtenerUsuarios() {
        return ResponseEntity.ok(adminService.obtenerUsuarios());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<AdminResponse>> obtenerPendientes() {
        return ResponseEntity.ok(adminService.obtenerPendientes());
    }

    @PostMapping("/verificar")
    public ResponseEntity<AdminResponse> verificarCuenta(
            @Valid @RequestBody AdminRequest request) {

        return ResponseEntity.ok(
                adminService.verificarCuenta(request)
        );
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> estadisticas() {

        return ResponseEntity.ok(
                new Object() {
                    public final Long usuarios = adminService.totalUsuarios();
                    public final Long clientes = adminService.totalClientes();
                    public final Long arrendadores = adminService.totalArrendadores();
                    public final Long pendientes = adminService.totalPendientes();
                }
        );
    }
}
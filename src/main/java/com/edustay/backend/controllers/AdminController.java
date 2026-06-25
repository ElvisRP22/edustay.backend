package com.edustay.backend.controllers;

import com.edustay.backend.dto.UsuarioAdminResponse;
import com.edustay.backend.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones de administración de la plataforma.
 * Reservado exclusivamente para usuarios con rol ADMIN.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administración", description = "Endpoints para la gestión administrativa global de la plataforma (solo ADMIN)")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private AdminService adminService;

    private boolean esAdmin(String userRole) {
        return "ADMIN".equalsIgnoreCase(userRole);
    }

    /**
     * GET /api/admin/usuarios - Lista todos los usuarios registrados
     */
    @GetMapping("/usuarios")
    @Operation(summary = "Listar todos los usuarios", description = "Retorna todos los usuarios del sistema (requiere rol ADMIN)")
    public ResponseEntity<?> obtenerTodosUsuarios(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }

        List<UsuarioAdminResponse> response = adminService.obtenerTodosUsuariosParaAdmin();
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/admin/usuarios/{id}/rol - Cambia el rol de un usuario
     */
    @PatchMapping("/usuarios/{id}/rol")
    @Operation(summary = "Cambiar rol de usuario", description = "Actualiza el rol de un usuario registrado (requiere rol ADMIN)")
    public ResponseEntity<?> cambiarRol(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestAttribute("userRole") String userRole) {

        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }

        String nuevoRolStr = body.get("rol");
        if (nuevoRolStr == null || nuevoRolStr.isBlank()) {
            return ResponseEntity.badRequest().body("El rol es obligatorio");
        }

        try {
            UsuarioAdminResponse res = adminService.cambiarRolDeUsuario(id, nuevoRolStr);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * DELETE /api/admin/usuarios/{id} - Elimina un usuario por su ID
     */
    @DeleteMapping("/usuarios/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID (requiere rol ADMIN)")
    public ResponseEntity<?> eliminarUsuario(
            @PathVariable Long id,
            @RequestAttribute("userRole") String userRole) {

        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }

        try {
            adminService.eliminarUsuarioPermanente(id);
            return ResponseEntity.ok(Map.of("message", "Usuario eliminado de forma permanente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


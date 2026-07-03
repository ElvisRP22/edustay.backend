package com.edustay.backend.controllers;

import com.edustay.backend.dto.UsuarioAdminResponse;
import com.edustay.backend.dto.MensajeResponse;
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

    /**
     * GET /api/admin/mensajes/reportados - Obtiene todos los mensajes reportados/moderados
     */
    @GetMapping("/mensajes/reportados")
    @Operation(summary = "Listar mensajes reportados", description = "Retorna todos los mensajes que han sido marcados como moderados/sospechosos (requiere rol ADMIN)")
    public ResponseEntity<?> obtenerMensajesReportados(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        List<MensajeResponse> response = adminService.obtenerMensajesReportados();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/admin/mensajes/historial - Obtiene el historial completo de auditoría de moderación
     */
    @GetMapping("/mensajes/historial")
    @Operation(summary = "Listar historial de moderación", description = "Retorna el historial completo de mensajes auditados, desestimados o eliminados (requiere rol ADMIN)")
    public ResponseEntity<?> obtenerHistorialModeracion(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        List<MensajeResponse> response = adminService.obtenerHistorialModeracion();
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/admin/mensajes/{id}/desestimar - Desestima el reporte de un mensaje
     */
    @PatchMapping("/mensajes/{id}/desestimar")
    @Operation(summary = "Desestimar reporte de mensaje", description = "Quita el flag de moderado al mensaje y limpia su categoría (requiere rol ADMIN)")
    public ResponseEntity<?> desestimarReporteMensaje(
            @PathVariable Long id,
            @RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        try {
            adminService.desestimarReporteMensaje(id);
            return ResponseEntity.ok(Map.of("message", "Reporte desestimado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * DELETE /api/admin/mensajes/{id} - Elimina un mensaje moderado
     */
    @DeleteMapping("/mensajes/{id}")
    @Operation(summary = "Eliminar mensaje moderado", description = "Elimina físicamente un mensaje del historial (requiere rol ADMIN)")
    public ResponseEntity<?> eliminarMensajeModerado(
            @PathVariable Long id,
            @RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        try {
            adminService.eliminarMensajeModerado(id);
            return ResponseEntity.ok(Map.of("message", "Mensaje eliminado correctamente de la conversación"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


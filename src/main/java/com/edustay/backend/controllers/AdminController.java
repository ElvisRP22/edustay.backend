package com.edustay.backend.controllers;

import com.edustay.backend.dto.UsuarioAdminResponse;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.UserRole;
import com.edustay.backend.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private UsuarioRepository usuarioRepository;

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

        List<UsuarioAdminResponse> response = usuarioRepository.findAll().stream()
                .map(u -> new UsuarioAdminResponse(
                        u.getId(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getEmail(),
                        u.getTelefono(),
                        u.getDni(),
                        u.getRol(),
                        u.isEmailVerificado(),
                        u.getIdentidadVerificada(),
                        u.getFechaRegistro()
                ))
                .collect(Collectors.toList());

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

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        String nuevoRolStr = body.get("rol");
        if (nuevoRolStr == null || nuevoRolStr.isBlank()) {
            return ResponseEntity.badRequest().body("El rol es obligatorio");
        }

        try {
            UserRole nuevoRol = UserRole.valueOf(nuevoRolStr.toUpperCase());
            usuario.setRol(nuevoRol);
            usuarioRepository.save(usuario);

            UsuarioAdminResponse res = new UsuarioAdminResponse(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getDni(),
                    usuario.getRol(),
                    usuario.isEmailVerificado(),
                    usuario.getIdentidadVerificada(),
                    usuario.getFechaRegistro()
            );

            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Rol inválido. Roles permitidos: ESTUDIANTE, ARRENDADOR, ADMIN");
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

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuarioRepository.delete(usuario);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado de forma permanente"));
    }
}


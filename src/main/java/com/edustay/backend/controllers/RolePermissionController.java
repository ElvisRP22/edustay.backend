package com.edustay.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edustay.backend.dto.PermissionCatalogResponse;
import com.edustay.backend.dto.RolePermissionRequest;
import com.edustay.backend.dto.RolePermissionResponse;
import com.edustay.backend.services.RolePermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para la administración de plantillas de roles y su catálogo de
 * permisos.
 */
@RestController
@RequestMapping("/api/admin/roles-permisos")
@Tag(name = "Roles y permisos", description = "Plantillas administrativas de roles y catálogo de permisos (solo ADMIN)")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    private boolean esAdmin(String userRole) {
        return "ADMIN".equalsIgnoreCase(userRole);
    }

    @GetMapping
    @Operation(summary = "Listar roles", description = "Retorna las plantillas de roles disponibles en el sistema")
    public ResponseEntity<?> listarRoles(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        List<RolePermissionResponse> roles = rolePermissionService.listarRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/permisos")
    @Operation(summary = "Listar permisos", description = "Retorna el catálogo de permisos disponibles para los roles")
    public ResponseEntity<?> listarPermisos(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        List<PermissionCatalogResponse> permisos = rolePermissionService.listarPermisos();
        return ResponseEntity.ok(permisos);
    }

    @PostMapping("/roles")
    @Operation(summary = "Crear rol", description = "Crea una nueva plantilla de rol con su configuración base")
    public ResponseEntity<?> crearRol(
            @Valid @RequestBody RolePermissionRequest request,
            @RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        try {
            RolePermissionResponse response = rolePermissionService.crearRol(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/roles/{id}")
    @Operation(summary = "Actualizar rol", description = "Actualiza la metadata y permisos de una plantilla de rol")
    public ResponseEntity<?> actualizarRol(
            @PathVariable Long id,
            @Valid @RequestBody RolePermissionRequest request,
            @RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        try {
            RolePermissionResponse response = rolePermissionService.actualizarRol(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/roles/{id}/permisos/{permissionId}")
    @Operation(summary = "Alternar permiso", description = "Activa o desactiva un permiso dentro de una plantilla de rol")
    public ResponseEntity<?> alternarPermiso(
            @PathVariable Long id,
            @PathVariable Long permissionId,
            @RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        try {
            RolePermissionResponse response = rolePermissionService.alternarPermiso(id, permissionId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/roles/{id}/restaurar")
    @Operation(summary = "Restaurar permisos", description = "Vuelve a aplicar los permisos base del rol seleccionado")
    public ResponseEntity<?> restaurarPermisos(
            @PathVariable Long id,
            @RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }
        try {
            RolePermissionResponse response = rolePermissionService.restaurarPermisos(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/resumen")
    @Operation(summary = "Resumen de roles", description = "Retorna métricas básicas del módulo de roles y permisos")
    public ResponseEntity<?> resumen(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: se requiere rol ADMIN");
        }

        List<RolePermissionResponse> roles = rolePermissionService.listarRoles();
        List<PermissionCatalogResponse> permisos = rolePermissionService.listarPermisos();
        int permisosAsignados = roles.stream().mapToInt(role -> role.getPermissions().size()).sum();
        int usuariosCubiertos = roles.stream().mapToInt(role -> role.getUsers() != null ? role.getUsers() : 0).sum();

        return ResponseEntity.ok(Map.of(
                "rolesActivos", roles.size(),
                "permisosDisponibles", permisos.size(),
                "permisosAsignados", permisosAsignados,
                "usuariosCubiertos", usuariosCubiertos));
    }
}
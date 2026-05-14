package com.edustay.backend.controllers;

import com.edustay.backend.dto.VerificacionRequest;
import com.edustay.backend.dto.VerificacionResponse;
import com.edustay.backend.services.VerificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión de verificaciones de documentos de identidad.
 * Solo los administradores pueden acceder a estos endpoints.
 */
@RestController
@RequestMapping("/api/verificaciones")
@Tag(name = "Verificaciones", description = "Endpoints para la gestión de verificaciones de identidad (solo ADMIN)")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VerificacionController {

    @Autowired
    private VerificacionService verificacionService;

    /**
     * GET /api/verificaciones - Lista todos los documentos de verificación
     */
    @GetMapping
    @Operation(summary = "Listar todos los documentos", description = "Retorna todos los documentos de verificación registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de documentos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerificacionResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo los administradores pueden acceder")
    })
    public ResponseEntity<?> obtenerTodos(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: se requiere rol ADMIN");
        }
        List<VerificacionResponse> docs = verificacionService.obtenerTodos();
        return ResponseEntity.ok(docs);
    }

    /**
     * GET /api/verificaciones/pendientes - Lista documentos pendientes de revisión
     */
    @GetMapping("/pendientes")
    @Operation(summary = "Listar documentos pendientes", description = "Retorna solo los documentos en estado PENDIENTE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de documentos pendientes",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerificacionResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo los administradores pueden acceder")
    })
    public ResponseEntity<?> obtenerPendientes(@RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: se requiere rol ADMIN");
        }
        List<VerificacionResponse> docs = verificacionService.obtenerPendientes();
        return ResponseEntity.ok(docs);
    }

    /**
     * GET /api/verificaciones/usuario/{usuarioId} - Lista documentos de un usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar documentos de un usuario", description = "Retorna todos los documentos subidos por un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de documentos del usuario"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo los administradores pueden acceder")
    })
    public ResponseEntity<?> obtenerPorUsuario(
            @PathVariable Long usuarioId,
            @RequestAttribute("userRole") String userRole) {
        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: se requiere rol ADMIN");
        }
        List<VerificacionResponse> docs = verificacionService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(docs);
    }

    /**
     * PATCH /api/verificaciones/{id} - Cambia el estado de un documento (Aprobado/Rechazado)
     * Al aprobar todos los documentos del usuario, se actualiza identidad_verificada en la tabla usuarios.
     */
    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar estado de verificación",
            description = "Cambia el estado de un documento a VERIFICADO o RECHAZADO. " +
                          "Si todos los documentos del usuario quedan VERIFICADOS, su identidad_verificada se actualiza a VERIFICADO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerificacionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Estado no permitido o datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Solo los administradores pueden acceder"),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado")
    })
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody VerificacionRequest request,
            @RequestAttribute("userRole") String userRole) {

        if (!esAdmin(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: se requiere rol ADMIN");
        }

        try {
            VerificacionResponse response = verificacionService.actualizarEstado(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Valida si el rol recibido del request corresponde a ADMIN
     */
    private boolean esAdmin(String userRole) {
        return "ADMIN".equalsIgnoreCase(userRole);
    }
}

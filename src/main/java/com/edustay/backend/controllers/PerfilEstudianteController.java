package com.edustay.backend.controllers;

import com.edustay.backend.dto.PerfilEstudianteRequest;
import com.edustay.backend.dto.PerfilEstudianteResponse;
import com.edustay.backend.services.PerfilEstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión del perfil de estudiante.
 * El usuario solo puede ver y editar su propio perfil.
 */
@RestController
@RequestMapping("/api/perfil")
@Tag(name = "Perfil de Estudiante", description = "Endpoints para ver y actualizar el perfil del estudiante autenticado")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PerfilEstudianteController {

    @Autowired
    private PerfilEstudianteService perfilEstudianteService;

    /**
     * GET /api/perfil - Obtiene el perfil completo del estudiante autenticado
     */
    @GetMapping
    @Operation(summary = "Ver mi perfil", description = "Obtiene el perfil completo del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PerfilEstudianteResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<PerfilEstudianteResponse> obtenerMiPerfil(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(perfilEstudianteService.obtenerPerfil(userId));
    }

    /**
     * PUT /api/perfil - Crea o actualiza el perfil del estudiante autenticado
     */
    @PutMapping
    @Operation(summary = "Actualizar mi perfil", description = "Crea o actualiza el perfil extendido del estudiante autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<PerfilEstudianteResponse> guardarMiPerfil(
            @Valid @RequestBody PerfilEstudianteRequest request,
            @RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(perfilEstudianteService.guardarPerfil(userId, request));
    }

    /**
     * GET /api/perfil/{usuarioId} - Obtiene el perfil público de otro usuario (arrendadores lo ven del estudiante)
     */
    @GetMapping("/{usuarioId}")
    @Operation(summary = "Ver perfil de un usuario", description = "Obtiene el perfil de cualquier estudiante por ID")
    @ApiResponse(responseCode = "200", description = "Perfil obtenido")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<PerfilEstudianteResponse> obtenerPerfilPorId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(perfilEstudianteService.obtenerPerfil(usuarioId));
    }
}

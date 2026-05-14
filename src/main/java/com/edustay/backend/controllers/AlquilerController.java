package com.edustay.backend.controllers;

import com.edustay.backend.dto.AlquilerRequest;
import com.edustay.backend.dto.AlquilerResponse;
import com.edustay.backend.services.AlquilerService;
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
 * Controlador para la gestión de alquileres activos
 */
@RestController
@RequestMapping("/api/alquileres")
@Tag(name = "Alquileres", description = "Endpoints para gestionar alquileres de habitaciones")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    /**
     * POST /api/alquileres - Crea un nuevo alquiler (el estudiante alquila una habitación)
     */
    @PostMapping
    @Operation(summary = "Crear alquiler", description = "Registra un nuevo alquiler de habitación para el estudiante autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alquiler creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlquilerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "409", description = "La habitación ya está alquilada")
    })
    public ResponseEntity<AlquilerResponse> crearAlquiler(
            @Valid @RequestBody AlquilerRequest request,
            @RequestAttribute("userId") Long userId) {
        AlquilerResponse response = alquilerService.crearAlquiler(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/alquileres/mis-alquileres - Alquileres del estudiante autenticado
     */
    @GetMapping("/mis-alquileres")
    @Operation(summary = "Mis alquileres", description = "Lista los alquileres activos del estudiante autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de alquileres del estudiante")
    public ResponseEntity<List<AlquilerResponse>> obtenerMisAlquileres(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(alquilerService.obtenerAlquileresDeEstudiante(userId));
    }

    /**
     * GET /api/alquileres/mis-arrendamientos - Alquileres de las habitaciones del arrendador autenticado
     */
    @GetMapping("/mis-arrendamientos")
    @Operation(summary = "Mis arrendamientos", description = "Lista los alquileres de las habitaciones del arrendador autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de alquileres del arrendador")
    public ResponseEntity<List<AlquilerResponse>> obtenerMisArrendamientos(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(alquilerService.obtenerAlquileresDeArrendador(userId));
    }

    /**
     * GET /api/alquileres - Lista todos los alquileres (admin)
     */
    @GetMapping
    @Operation(summary = "Todos los alquileres", description = "Lista todos los alquileres activos del sistema (uso administrativo)")
    @ApiResponse(responseCode = "200", description = "Lista completa de alquileres")
    public ResponseEntity<List<AlquilerResponse>> obtenerTodos() {
        return ResponseEntity.ok(alquilerService.obtenerTodos());
    }

    /**
     * GET /api/alquileres/{id} - Detalle de un alquiler
     */
    @GetMapping("/{id}")
    @Operation(summary = "Detalle de alquiler", description = "Obtiene los datos completos de un alquiler por ID")
    @ApiResponse(responseCode = "200", description = "Alquiler encontrado")
    @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")
    public ResponseEntity<AlquilerResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alquilerService.obtenerPorId(id));
    }

    /**
     * DELETE /api/alquileres/{id} - Finaliza un alquiler (arrendador o admin)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Finalizar alquiler", description = "Cierra el alquiler y libera la habitación. Solo el arrendador propietario o un admin pueden hacerlo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Alquiler finalizado correctamente"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para finalizar este alquiler"),
            @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")
    })
    public ResponseEntity<Void> finalizarAlquiler(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        alquilerService.finalizarAlquiler(id, userId);
        return ResponseEntity.noContent().build();
    }
}

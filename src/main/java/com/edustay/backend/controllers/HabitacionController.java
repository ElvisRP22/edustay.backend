package com.edustay.backend.controllers;

import com.edustay.backend.dto.HabitacionRequest;
import com.edustay.backend.dto.HabitacionResponse;
import com.edustay.backend.services.HabitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para CRUD de Habitaciones
 */
@RestController
@RequestMapping("/api/habitaciones")
@Tag(name = "Habitaciones", description = "Endpoints para gestión de habitaciones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    /**
     * POST /api/habitaciones - Crea una nueva habitación
     */
    @PostMapping
    @PreAuthorize("hasRole('ARRENDADOR')")
    @Operation(summary = "Crear habitación", description = "Crea una nueva habitación para el arrendador autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Habitación creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HabitacionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<HabitacionResponse> crearHabitacion(
            @Valid @RequestBody HabitacionRequest request,
            @RequestAttribute("userId") Long userId) {
        try {
            HabitacionResponse response = habitacionService.crearHabitacion(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * GET /api/habitaciones - Obtiene todas las habitaciones
     */
    @GetMapping
    @Operation(summary = "Obtener todas las habitaciones", description = "Retorna una lista de todas las habitaciones disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de habitaciones")
    public ResponseEntity<List<HabitacionResponse>> obtenerTodas() {
        List<HabitacionResponse> habitaciones = habitacionService.obtenerTodas();
        return ResponseEntity.ok(habitaciones);
    }

    /**
     * GET /api/habitaciones/{id} - Obtiene una habitación por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener habitación por ID", description = "Retorna los detalles de una habitación específica")
    @ApiResponse(responseCode = "200", description = "Habitación encontrada")
    @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    public ResponseEntity<HabitacionResponse> obtenerPorId(@PathVariable Long id) {
        HabitacionResponse habitacion = habitacionService.obtenerPorId(id);
        return ResponseEntity.ok(habitacion);
    }

    /**
     * GET /api/habitaciones/arrendador/{arrendadorId} - Obtiene habitaciones de un arrendador
     */
    @GetMapping("/arrendador/{arrendadorId}")
    @Operation(summary = "Obtener habitaciones del arrendador", description = "Retorna todas las habitaciones publicadas por un arrendador")
    @ApiResponse(responseCode = "200", description = "Lista de habitaciones del arrendador")
    public ResponseEntity<List<HabitacionResponse>> obtenerPorArrendador(@PathVariable Long arrendadorId) {
        List<HabitacionResponse> habitaciones = habitacionService.obtenerPorArrendador(arrendadorId);
        return ResponseEntity.ok(habitaciones);
    }

    /**
     * GET /api/habitaciones/estado/disponibles - Obtiene habitaciones disponibles
     */
    @GetMapping("/estado/disponibles")
    @Operation(summary = "Obtener habitaciones disponibles", description = "Retorna todas las habitaciones con estado DISPONIBLE")
    @ApiResponse(responseCode = "200", description = "Lista de habitaciones disponibles")
    public ResponseEntity<List<HabitacionResponse>> obtenerDisponibles() {
        List<HabitacionResponse> habitaciones = habitacionService.obtenerDisponibles();
        return ResponseEntity.ok(habitaciones);
    }

    /**
     * PUT /api/habitaciones/{id} - Actualiza una habitación
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar habitación", description = "Actualiza los detalles de una habitación (solo el propietario puede hacerlo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitación actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    @PreAuthorize("hasRole('ARRENDADOR')")
    public ResponseEntity<HabitacionResponse> actualizarHabitacion(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request,
            @RequestAttribute("userId") Long userId) {
        try {
            HabitacionResponse response = habitacionService.actualizarHabitacion(id, request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * DELETE /api/habitaciones/{id} - Elimina una habitación
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar habitación", description = "Elimina una habitación (solo el propietario puede hacerlo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Habitación eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    @PreAuthorize("hasRole('ARRENDADOR')")
    public ResponseEntity<Void> eliminarHabitacion(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        try {
            habitacionService.eliminarHabitacion(id, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

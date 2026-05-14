package com.edustay.backend.controllers;

import com.edustay.backend.dto.ResenaRequest;
import com.edustay.backend.dto.ResenaResponse;
import com.edustay.backend.services.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para CRUD de Reseñas y Calificaciones
 */
@RestController
@RequestMapping("/api/resenas")
@Tag(name = "Reseñas", description = "Endpoints para gestión de reseñas y calificaciones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    /**
     * POST /api/resenas - Crea una nueva reseña
     */
    @PostMapping
    @Operation(summary = "Crear reseña", description = "Crea una nueva reseña/calificación para una habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResenaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ya calificada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<ResenaResponse> crearResena(
            @Valid @RequestBody ResenaRequest request,
            @RequestAttribute("userId") Long userId) {
        try {
            ResenaResponse response = resenaService.crearResena(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GET /api/resenas/habitacion/{habitacionId} - Obtiene reseñas de una habitación
     */
    @GetMapping("/habitacion/{habitacionId}")
    @Operation(summary = "Obtener reseñas de una habitación", description = "Retorna todas las reseñas y calificaciones de una habitación")
    @ApiResponse(responseCode = "200", description = "Lista de reseñas")
    public ResponseEntity<List<ResenaResponse>> obtenerPorHabitacion(@PathVariable Long habitacionId) {
        List<ResenaResponse> resenas = resenaService.obtenerPorHabitacion(habitacionId);
        return ResponseEntity.ok(resenas);
    }

    /**
     * GET /api/resenas/estudiante/{estudianteId} - Obtiene reseñas de un estudiante
     */
    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Obtener reseñas de un estudiante", description = "Retorna todas las reseñas dejadas por un estudiante")
    @ApiResponse(responseCode = "200", description = "Lista de reseñas del estudiante")
    public ResponseEntity<List<ResenaResponse>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        List<ResenaResponse> resenas = resenaService.obtenerPorEstudiante(estudianteId);
        return ResponseEntity.ok(resenas);
    }

    /**
     * GET /api/resenas/{id} - Obtiene una reseña por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña por ID", description = "Retorna los detalles de una reseña específica")
    @ApiResponse(responseCode = "200", description = "Reseña encontrada")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    public ResponseEntity<ResenaResponse> obtenerPorId(@PathVariable Long id) {
        ResenaResponse resena = resenaService.obtenerPorId(id);
        return ResponseEntity.ok(resena);
    }

    /**
     * PUT /api/resenas/{id} - Actualiza una reseña
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reseña", description = "Actualiza una reseña existente (solo el autor puede hacerlo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<ResenaResponse> actualizarResena(
            @PathVariable Long id,
            @Valid @RequestBody ResenaRequest request,
            @RequestAttribute("userId") Long userId) {
        try {
            ResenaResponse response = resenaService.actualizarResena(id, request, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * DELETE /api/resenas/{id} - Elimina una reseña
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña (solo el autor puede hacerlo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<Void> eliminarResena(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        try {
            resenaService.eliminarResena(id, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GET /api/resenas/validar/{estudianteId}/{habitacionId} - Verifica si ya fue calificada
     */
    @GetMapping("/validar/{estudianteId}/{habitacionId}")
    @Operation(summary = "Verificar si habitación ya fue calificada", description = "Retorna true si el estudiante ya calificó esta habitación")
    @ApiResponse(responseCode = "200", description = "Validación completada")
    public ResponseEntity<Boolean> yaCalificada(
            @PathVariable Long estudianteId,
            @PathVariable Long habitacionId) {
        boolean calificada = resenaService.yaCalificada(estudianteId, habitacionId);
        return ResponseEntity.ok(calificada);
    }
}

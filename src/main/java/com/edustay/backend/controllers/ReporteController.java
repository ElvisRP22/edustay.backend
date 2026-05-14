package com.edustay.backend.controllers;

import com.edustay.backend.dto.ReporteRequest;
import com.edustay.backend.dto.ReporteResponse;
import com.edustay.backend.services.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
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
 * Controlador para la gestión de reportes de habitaciones
 */
@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Endpoints para reportar habitaciones irregulares")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    /**
     * POST /api/reportes - Crea un reporte sobre una habitación
     */
    @PostMapping
    @Operation(summary = "Crear reporte", description = "Registra un reporte de irregularidades sobre una habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    public ResponseEntity<ReporteResponse> crearReporte(
            @Valid @RequestBody ReporteRequest request,
            @RequestAttribute("userId") Long userId) {
        ReporteResponse response = reporteService.crearReporte(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/reportes - Lista todos los reportes (admin)
     */
    @GetMapping
    @Operation(summary = "Todos los reportes", description = "Lista todos los reportes del sistema (uso administrativo)")
    @ApiResponse(responseCode = "200", description = "Lista de reportes")
    public ResponseEntity<List<ReporteResponse>> obtenerTodos() {
        return ResponseEntity.ok(reporteService.obtenerTodos());
    }

    /**
     * GET /api/reportes/mis-reportes - Reportes del usuario autenticado
     */
    @GetMapping("/mis-reportes")
    @Operation(summary = "Mis reportes", description = "Lista los reportes enviados por el usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de mis reportes")
    public ResponseEntity<List<ReporteResponse>> obtenerMisReportes(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(reporteService.obtenerMisReportes(userId));
    }

    /**
     * GET /api/reportes/habitacion/{habitacionId} - Reportes de una habitación específica
     */
    @GetMapping("/habitacion/{habitacionId}")
    @Operation(summary = "Reportes de habitación", description = "Lista los reportes de una habitación específica (uso administrativo)")
    @ApiResponse(responseCode = "200", description = "Lista de reportes de la habitación")
    public ResponseEntity<List<ReporteResponse>> obtenerPorHabitacion(@PathVariable Long habitacionId) {
        return ResponseEntity.ok(reporteService.obtenerPorHabitacion(habitacionId));
    }
}

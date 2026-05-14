package com.edustay.backend.controllers;

import com.edustay.backend.dto.HabitacionResponse;
import com.edustay.backend.services.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de habitaciones favoritas del estudiante
 */
@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "Endpoints para gestionar habitaciones favoritas")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    /**
     * GET /api/favoritos - Lista las habitaciones favoritas del usuario autenticado
     */
    @GetMapping
    @Operation(summary = "Mis favoritos", description = "Retorna la lista de habitaciones marcadas como favoritas")
    @ApiResponse(responseCode = "200", description = "Lista de favoritos")
    public ResponseEntity<List<HabitacionResponse>> obtenerFavoritos(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(favoritoService.obtenerFavoritos(userId));
    }

    /**
     * POST /api/favoritos/{habitacionId} - Agrega una habitación a favoritos
     */
    @PostMapping("/{habitacionId}")
    @Operation(summary = "Agregar a favoritos", description = "Marca una habitación como favorita para el usuario autenticado")
    @ApiResponse(responseCode = "201", description = "Favorito agregado")
    @ApiResponse(responseCode = "409", description = "Ya está en favoritos")
    public ResponseEntity<Void> agregarFavorito(
            @PathVariable Long habitacionId,
            @RequestAttribute("userId") Long userId) {
        favoritoService.agregarFavorito(userId, habitacionId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * DELETE /api/favoritos/{habitacionId} - Quita una habitación de favoritos
     */
    @DeleteMapping("/{habitacionId}")
    @Operation(summary = "Quitar de favoritos", description = "Elimina una habitación de la lista de favoritos del usuario autenticado")
    @ApiResponse(responseCode = "204", description = "Favorito eliminado")
    @ApiResponse(responseCode = "404", description = "No estaba en favoritos")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable Long habitacionId,
            @RequestAttribute("userId") Long userId) {
        favoritoService.eliminarFavorito(userId, habitacionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/favoritos/{habitacionId}/check - Verifica si una habitación está en favoritos
     */
    @GetMapping("/{habitacionId}/check")
    @Operation(summary = "Verificar favorito", description = "Indica si una habitación está en la lista de favoritos del usuario")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    public ResponseEntity<Map<String, Boolean>> esFavorito(
            @PathVariable Long habitacionId,
            @RequestAttribute("userId") Long userId) {
        boolean resultado = favoritoService.esFavorito(userId, habitacionId);
        return ResponseEntity.ok(Map.of("esFavorito", resultado));
    }
}

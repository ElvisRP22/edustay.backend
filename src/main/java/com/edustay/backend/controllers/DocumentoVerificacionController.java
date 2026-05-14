package com.edustay.backend.controllers;

import com.edustay.backend.dto.DocumentoVerificacionRequest;
import com.edustay.backend.dto.VerificacionResponse;
import com.edustay.backend.services.DocumentoVerificacionService;
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
 * Controlador para que los usuarios suban y consulten sus documentos de verificación.
 */
@RestController
@RequestMapping("/api/documentos")
@Tag(name = "Documentos de Verificación", description = "Endpoints para subida y consulta de documentos de identidad")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DocumentoVerificacionController {

    @Autowired
    private DocumentoVerificacionService documentoVerificacionService;

    /**
     * POST /api/documentos - Sube un nuevo documento de verificación
     */
    @PostMapping
    @Operation(summary = "Subir documento", description = "Registra un nuevo documento de verificación para el usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VerificacionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<VerificacionResponse> subirDocumento(
            @Valid @RequestBody DocumentoVerificacionRequest request,
            @RequestAttribute("userId") Long userId) {
        VerificacionResponse response = documentoVerificacionService.subirDocumento(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/documentos/mis-documentos - Lista los documentos del usuario autenticado
     */
    @GetMapping("/mis-documentos")
    @Operation(summary = "Mis documentos", description = "Lista todos los documentos de verificación del usuario autenticado y su estado")
    @ApiResponse(responseCode = "200", description = "Lista de documentos")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    public ResponseEntity<List<VerificacionResponse>> obtenerMisDocumentos(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(documentoVerificacionService.obtenerMisDocumentos(userId));
    }
}

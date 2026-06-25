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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
     * POST /api/documentos/upload - Sube un archivo físico al servidor y retorna su URL
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir archivo físico", description = "Sube un archivo de documento al servidor local y retorna su URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo subido exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Archivo vacío o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al guardar el archivo")
    })
    public ResponseEntity<Map<String, String>> subirArchivoFisico(
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        try {
            // Asegurar que el directorio de subidas exista
            Path uploadDir = Paths.get("./uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Generar un nombre único para evitar colisiones
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            // Guardar el archivo en el sistema de archivos
            Path destination = uploadDir.resolve(newFilename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            // Construir la URL completa usando el request actual
            String fileUrl = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(newFilename)
                    .toUriString();

            Map<String, String> response = new HashMap<>();
            response.put("archivoUrl", fileUrl);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo guardar el archivo: " + e.getMessage(), e);
        }
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

package com.edustay.backend.controllers;

import com.edustay.backend.dto.MensajeRequest;
import com.edustay.backend.dto.MensajeResponse;
import com.edustay.backend.services.MensajeService;
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
import java.util.Map;

/**
 * Controlador para la mensajería interna entre estudiantes y arrendadores
 */
@RestController
@RequestMapping("/api/mensajes")
@Tag(name = "Mensajes", description = "Endpoints para la mensajería interna entre usuarios")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    /**
     * POST /api/mensajes - Envía un nuevo mensaje
     */
    @PostMapping
    @Operation(summary = "Enviar mensaje", description = "Envía un mensaje a otro usuario en el contexto de una habitación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensaje enviado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<MensajeResponse> enviarMensaje(
            @Valid @RequestBody MensajeRequest request,
            @RequestAttribute("userId") Long userId) {
        MensajeResponse response = mensajeService.enviarMensaje(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/mensajes/conversacion - Obtiene la conversación entre dos usuarios sobre una habitación
     */
    @GetMapping("/conversacion")
    @Operation(summary = "Ver conversación", description = "Retorna todos los mensajes entre dos usuarios sobre una habitación específica, ordenados cronológicamente")
    @ApiResponse(responseCode = "200", description = "Lista de mensajes de la conversación")
    public ResponseEntity<List<MensajeResponse>> obtenerConversacion(
            @RequestParam Long habitacionId,
            @RequestParam Long otroUsuarioId,
            @RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(mensajeService.obtenerConversacion(habitacionId, userId, otroUsuarioId));
    }

    /**
     * GET /api/mensajes/bandeja - Bandeja de entrada del usuario autenticado
     */
    @GetMapping("/bandeja")
    @Operation(summary = "Bandeja de entrada", description = "Lista todos los mensajes recibidos por el usuario autenticado, ordenados por fecha descendente")
    @ApiResponse(responseCode = "200", description = "Mensajes recibidos")
    public ResponseEntity<List<MensajeResponse>> obtenerBandeja(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(mensajeService.obtenerBandejaEntrada(userId));
    }

    /**
     * GET /api/mensajes/no-leidos - Cantidad de mensajes sin leer
     */
    @GetMapping("/no-leidos")
    @Operation(summary = "Mensajes no leídos", description = "Retorna la cantidad de mensajes no leídos del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Contador de mensajes no leídos")
    public ResponseEntity<Map<String, Long>> contarNoLeidos(@RequestAttribute("userId") Long userId) {
        long count = mensajeService.contarNoLeidos(userId);
        return ResponseEntity.ok(Map.of("noLeidos", count));
    }

    /**
     * PATCH /api/mensajes/conversacion/leer - Marca mensajes de una conversación como leídos
     */
    @PatchMapping("/conversacion/leer")
    @Operation(summary = "Marcar como leído", description = "Marca como leídos todos los mensajes de un emisor en una conversación sobre una habitación")
    @ApiResponse(responseCode = "204", description = "Mensajes marcados como leídos")
    public ResponseEntity<Void> marcarComoLeido(
            @RequestParam Long habitacionId,
            @RequestParam Long emisorId,
            @RequestAttribute("userId") Long userId) {
        mensajeService.marcarComoLeido(habitacionId, emisorId, userId);
        return ResponseEntity.noContent().build();
    }
}

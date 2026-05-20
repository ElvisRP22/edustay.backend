package com.edustay.backend.controllers;

import com.edustay.backend.dto.AuthResponse;
import com.edustay.backend.dto.LoginRequest;
import com.edustay.backend.dto.RegisterRequest;
import com.edustay.backend.services.AuthService;
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

/**
 * Controller para manejar las operaciones de autenticación (login y registro)
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para el login de un usuario
     * 
     * @param loginRequest Credenciales del usuario
     * @return Token JWT y datos del usuario
     */
    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica un usuario con email y contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, null, null, null, null, null, e.getMessage()));
        }
    }

    /**
     * Endpoint para el registro de un nuevo usuario
     * 
     * @param registerRequest Datos del nuevo usuario
     * @return Token JWT y datos del usuario creado
     */
    @PostMapping("/register")
    @Operation(summary = "Registro de usuario", description = "Crea una nueva cuenta de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @ApiResponse(responseCode = "409", description = "Email ya registrado"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("coinciden") || e.getMessage().contains("registrado")
                    ? HttpStatus.BAD_REQUEST
                    : HttpStatus.CONFLICT;
            return ResponseEntity.status(status)
                    .body(new AuthResponse(null, null, null, null, null, null, null, null, e.getMessage()));
        }
    }

    /**
     * Endpoint para validar el token (útil para el frontend)
     * 
     * @param token Token JWT a validar
     * @return Estado de validación del token
     */
    @GetMapping("/validate")
    @Operation(summary = "Validar token", description = "Valida si un token JWT es válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado")
    })
    public ResponseEntity<String> validateToken(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
        }

        // Eliminar el prefijo "Bearer " si existe
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        // La validación se hará en el filtro JWT, pero aquí retornamos un endpoint
        return ResponseEntity.ok("Token válido");
    }
}

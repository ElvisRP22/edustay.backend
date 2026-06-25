package com.edustay.backend.controllers;

import com.edustay.backend.dto.AuthResponse;
import com.edustay.backend.dto.GoogleTokenDto;
import com.edustay.backend.dto.LoginRequest;
import com.edustay.backend.dto.RegisterRequest;
import com.edustay.backend.dto.VerifyEmailRequest;
import com.edustay.backend.dto.ResendOtpRequest;
import com.edustay.backend.security.JwtTokenProvider;
import com.edustay.backend.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.edustay.backend.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Controller para manejar las operaciones de autenticación (login y registro)
 */
@RestController
@RequestMapping({ "/api/auth", "/api/v1/auth" })
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
     * Endpoint para login social con Google.
     * Recibe el tokenId desde el frontend y devuelve el JWT propio de EduStay.
     */
    @PostMapping("/google")
    @Operation(summary = "Login con Google", description = "Autentica o registra un usuario usando Google ID Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login con Google exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Token inválido"),
            @ApiResponse(responseCode = "401", description = "Token expirado o no verificado"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<AuthResponse> loginConGoogle(@Valid @RequestBody GoogleTokenDto googleTokenDto) {
        try {
            AuthResponse response = authService.loginConGoogle(googleTokenDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtTokenProvider.validateToken(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }

        // Además de validar firma/expiración, comprobar que el usuario existe en la BD
        try {
            Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
            boolean exists = false;
            if (userId != null) {
                exists = usuarioRepository.findById(userId).isPresent();
            } else {
                String email = jwtTokenProvider.getEmailFromToken(jwt);
                if (email != null && !email.isBlank()) {
                    exists = usuarioRepository.findByEmail(email).isPresent();
                }
            }

            if (!exists) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o usuario no encontrado");
        }

        return ResponseEntity.ok("Token válido");
    }

    /**
     * Endpoint para verificar el email de un usuario usando código OTP
     */
    @PostMapping("/verify-email")
    @Operation(summary = "Verificar email", description = "Valida el código OTP enviado al correo del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verificado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Código inválido o expirado"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        try {
            authService.verifyEmail(request.getEmail(), request.getCodigo());
            return ResponseEntity.ok(Map.of("message", "Correo verificado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Endpoint para reenviar el código OTP de verificación
     */
    @PostMapping("/resend-otp")
    @Operation(summary = "Reenviar OTP", description = "Invalida códigos OTP anteriores y envía uno nuevo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP reenviado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al reenviar OTP"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    public ResponseEntity<?> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        try {
            authService.resendOtp(request.getEmail());
            return ResponseEntity.ok(Map.of("message", "Código de verificación reenviado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
}

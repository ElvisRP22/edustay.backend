package com.edustay.backend.services;

import com.edustay.backend.dto.AuthResponse;
import com.edustay.backend.dto.GoogleTokenDto;
import com.edustay.backend.dto.LoginRequest;
import com.edustay.backend.dto.RegisterRequest;
import com.edustay.backend.models.Usuario;

/**
 * Interfaz del servicio de autenticación
 */
public interface AuthService {
    
    /**
     * Realiza el login de un usuario
     */
    AuthResponse login(LoginRequest loginRequest);

    /**
     * Realiza el registro de un nuevo usuario
     */
    AuthResponse register(RegisterRequest registerRequest);

    /**
     * Realiza el login social con Google usando el tokenId del frontend.
     */
    AuthResponse loginConGoogle(GoogleTokenDto googleTokenDto);

    /**
     * Obtiene un usuario por su email
     */
    Usuario obtenerPorEmail(String email);

    /**
     * Verifica si un email ya está registrado
     */
    boolean existeEmail(String email);

    /**
     * Verifica el email del usuario usando un código OTP
     */
    void verifyEmail(String email, String codigo);

    /**
     * Genera y reenvía un nuevo código OTP al correo del usuario
     */
    void resendOtp(String email);

    /**
     * Genera un token de restablecimiento y envía el enlace al usuario.
     */
    void forgotPassword(String email);

    /**
     * Valida si un token de restablecimiento es válido (existe, no usado, no expirado).
     */
    boolean validarTokenRestablecimiento(String token);

    /**
     * Valida el token y actualiza la contraseña del usuario.
     */
    void resetPassword(String token, String nuevaPassword);
}

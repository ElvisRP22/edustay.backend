package com.edustay.backend.services;

import com.edustay.backend.dto.AuthResponse;
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
     * Obtiene un usuario por su email
     */
    Usuario obtenerPorEmail(String email);

    /**
     * Verifica si un email ya está registrado
     */
    boolean existeEmail(String email);
}

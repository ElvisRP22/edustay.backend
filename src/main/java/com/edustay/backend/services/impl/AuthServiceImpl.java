package com.edustay.backend.services.impl;

import com.edustay.backend.dto.AuthResponse;
import com.edustay.backend.dto.GoogleTokenDto;
import com.edustay.backend.dto.LoginRequest;
import com.edustay.backend.dto.RegisterRequest;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.UserRole;
import com.edustay.backend.models.enums.VerificationStatus;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.security.JwtTokenProvider;
import com.edustay.backend.services.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.UUID;

/**
 * Implementación del servicio de autenticación que maneja login y registro
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {



    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    // Inyecta aquí tu servicio JWT actual (JwtTokenProvider) para reutilizar la generación del token propio de EduStay.
    private JwtTokenProvider jwtTokenProvider;

    @Value("${app.google.client-id}")
    private String googleClientId;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Realiza el login de un usuario
     * @param loginRequest Datos del login (email y contraseña)
     * @return AuthResponse con el token y datos del usuario
     * @throws RuntimeException si las credenciales son inválidas
     */
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + loginRequest.getEmail()));

        // Validar la contraseña
        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Generar el token JWT
        String token = jwtTokenProvider.generateToken(
                usuario.getEmail(),
                usuario.getId(),
                usuario.getRol().toString()
        );

        // Retornar la respuesta con el token
        return new AuthResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFotoUrl(),
                usuario.getRol(),
                token,
                "Login exitoso"
        );
    }

    /**
     * Realiza el registro de un nuevo usuario
     * @param registerRequest Datos del registro
     * @return AuthResponse con el token y datos del usuario creado
     * @throws RuntimeException si el email ya está registrado o si las contraseñas no coinciden
     */
    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        // Validar que las contraseñas coincidan
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        // Validar que el email no esté registrado
        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear el nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registerRequest.getNombre());
        nuevoUsuario.setApellido(registerRequest.getApellido());
        nuevoUsuario.setEmail(registerRequest.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        nuevoUsuario.setTelefono(registerRequest.getTelefono());
        nuevoUsuario.setRol(UserRole.ESTUDIANTE); // Por defecto, rol de estudiante
        nuevoUsuario.setEmailVerificado(false);
        nuevoUsuario.setIdentidadVerificada(VerificationStatus.PENDIENTE);

        // Guardar el usuario
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // Generar el token JWT
        String token = jwtTokenProvider.generateToken(
                usuarioGuardado.getEmail(),
                usuarioGuardado.getId(),
                usuarioGuardado.getRol().toString()
        );

        // Retornar la respuesta con el token
        return new AuthResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getApellido(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getTelefono(),
                usuarioGuardado.getFotoUrl(),
                usuarioGuardado.getRol(),
                token,
                "Registro exitoso"
        );
    }

    /**
     * Realiza el login social con Google.
     * Verifica el token ID, crea el usuario si no existe y retorna el JWT propio de EduStay.
     */
    @Override
    public AuthResponse loginConGoogle(GoogleTokenDto googleTokenDto) {
        GoogleIdTokenVerifier verifier = buildGoogleIdTokenVerifier();
        GoogleIdToken googleIdToken;

        try {
            googleIdToken = verifier.verify(googleTokenDto.getTokenId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo verificar el token de Google");
        }

        if (googleIdToken == null) {
            throw new RuntimeException("Token de Google inválido o expirado");
        }

        Payload payload = googleIdToken.getPayload();
        String email = payload.getEmail();
        String nombre = extractNombre(payload);
        String apellido = extractApellido(payload);
        String fotoUrl = (String) payload.get("picture");

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() -> crearUsuarioDesdeGoogle(email, nombre, apellido, fotoUrl));

        String token = jwtTokenProvider.generateToken(
                usuario.getEmail(),
                usuario.getId(),
                usuario.getRol().toString()
        );

        return new AuthResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFotoUrl(),
                usuario.getRol(),
                token,
                "Login con Google exitoso"
        );
    }

    /**
     * Obtiene un usuario por su email
     * @param email Email del usuario
     * @return Usuario si existe
     * @throws RuntimeException si el usuario no existe
     */
    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    /**
     * Verifica si un email ya está registrado
     * @param email Email a verificar
     * @return true si el email existe, false si no
     */
    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    private GoogleIdTokenVerifier buildGoogleIdTokenVerifier() {
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new RuntimeException("La configuración app.google.client-id es obligatoria para validar Google Sign-In");
        }

        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    private Usuario crearUsuarioDesdeGoogle(String email, String nombre, String apellido, String fotoUrl) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(generarPasswordAleatoria()));
        nuevoUsuario.setRol(UserRole.ESTUDIANTE);
        nuevoUsuario.setFotoUrl(fotoUrl);
        nuevoUsuario.setEmailVerificado(true);
        nuevoUsuario.setIdentidadVerificada(VerificationStatus.PENDIENTE);

        return usuarioRepository.save(nuevoUsuario);
    }

    private String extractNombre(Payload payload) {
        String givenName = (String) payload.get("given_name");
        if (givenName != null && !givenName.isBlank()) {
            return givenName;
        }

        String fullName = (String) payload.get("name");
        if (fullName != null && !fullName.isBlank()) {
            String[] parts = fullName.trim().split("\\s+");
            return parts[0];
        }

        return "Usuario";
    }

    private String extractApellido(Payload payload) {
        String familyName = (String) payload.get("family_name");
        if (familyName != null && !familyName.isBlank()) {
            return familyName;
        }

        String fullName = (String) payload.get("name");
        if (fullName != null && !fullName.isBlank()) {
            String[] parts = fullName.trim().split("\\s+");
            if (parts.length > 1) {
                return String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
            }
        }

        return "";
    }

    private String generarPasswordAleatoria() {
        byte[] seed = new byte[24];
        secureRandom.nextBytes(seed);
        return UUID.nameUUIDFromBytes(seed).toString() + "-" + System.currentTimeMillis();
    }
}

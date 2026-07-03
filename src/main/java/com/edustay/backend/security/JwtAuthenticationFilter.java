package com.edustay.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT para validar tokens en cada request
 * Se ejecuta una sola vez por request
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Extrae el JWT token del header Authorization
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // Extraer email y otros datos del token
                String email = jwtTokenProvider.getEmailFromToken(jwt);
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                String rol = jwtTokenProvider.getRolFromToken(jwt);

                // Crear objeto de autenticación con las autoridades derivadas del rol en el
                // token
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
                        null, java.util.List.of(authority));

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Guardar el usuario en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Pasar información adicional al request para que esté disponible en los
                // controllers
                request.setAttribute("userId", userId);
                request.setAttribute("userEmail", email);
                request.setAttribute("userRole", rol);
            }
        } catch (Exception ex) {
            logger.error("No se pudo establecer la autenticación del usuario en el contexto de seguridad", ex);
        }

        filterChain.doFilter(request, response);
    }
}

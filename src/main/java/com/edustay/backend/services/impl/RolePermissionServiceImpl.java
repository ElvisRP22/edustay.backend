package com.edustay.backend.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edustay.backend.dto.PermissionCatalogResponse;
import com.edustay.backend.dto.RolePermissionRequest;
import com.edustay.backend.dto.RolePermissionResponse;
import com.edustay.backend.models.Permiso;
import com.edustay.backend.models.Rol;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.UserRole;
import com.edustay.backend.repositories.PermisoRepository;
import com.edustay.backend.repositories.RolRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.RolePermissionService;

import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final List<PermissionCatalogResponse> CATALOGO_PERMISOS = List.of(
            new PermissionCatalogResponse(1L, "Ver panel y reportes",
                    "Permite consultar métricas, estados y resúmenes generales.", "Accesos", "heroShieldCheck"),
            new PermissionCatalogResponse(2L, "Gestionar credenciales",
                    "Permite emitir, rotar y revocar accesos sensibles.", "Accesos", "heroKey"),
            new PermissionCatalogResponse(3L, "Ver usuarios",
                    "Permite consultar el listado de cuentas y sus datos básicos.", "Usuarios", "heroUserCircle"),
            new PermissionCatalogResponse(4L, "Editar roles",
                    "Permite cambiar asignaciones y perfiles administrativos.", "Usuarios", "heroPencilSquare"),
            new PermissionCatalogResponse(5L, "Administrar habitaciones",
                    "Permite crear, editar y suspender publicaciones.", "Operación", "heroBuildingOffice2"),
            new PermissionCatalogResponse(6L, "Administrar alquileres",
                    "Permite revisar contratos y finalizar operaciones.", "Operación", "heroHome"),
            new PermissionCatalogResponse(7L, "Moderar mensajes",
                    "Permite revisar conversaciones y aplicar acciones de control.", "Moderación",
                    "heroChatBubbleLeftRight"),
            new PermissionCatalogResponse(8L, "Resolver verificaciones",
                    "Permite aprobar o rechazar documentos pendientes.", "Moderación", "heroDocumentText"));

    private static final java.util.Map<String, List<String>> PERMISOS_BASE_POR_ROL = java.util.Map.of(
            "ADMIN", List.of(
                    "Ver panel y reportes", "Gestionar credenciales", "Ver usuarios", "Editar roles",
                    "Administrar habitaciones", "Administrar alquileres", "Moderar mensajes", "Resolver verificaciones"
            ),
            "ARRENDADOR", List.of(
                    "Ver usuarios", "Administrar habitaciones", "Administrar alquileres", "Moderar mensajes"
            ),
            "ESTUDIANTE", List.of(
                    "Ver usuarios", "Moderar mensajes"
            ));

    @PostConstruct
    public void init() {
        // 1. Sembrar el catálogo de permisos en la base de datos si no existen
        for (PermissionCatalogResponse p : CATALOGO_PERMISOS) {
            Optional<Permiso> existente = permisoRepository.findByTitle(p.getTitle());
            if (existente.isEmpty()) {
                permisoRepository.save(new Permiso(p.getTitle(), p.getDescription(), p.getArea(), p.getIcon()));
            }
        }

        // 2. Sembrar los roles principales si no existen
        sembrarRolSiNoExiste("ADMIN", "Control total sobre configuración, seguridad y moderación de la plataforma.", "#1d4ed8");
        sembrarRolSiNoExiste("ARRENDADOR", "Gestiona sus habitaciones, contratos y comunicaciones con estudiantes.", "#059669");
        sembrarRolSiNoExiste("ESTUDIANTE", "Accede a búsqueda, mensajes, favoritos y su perfil de verificación.", "#7c3aed");
    }

    private void sembrarRolSiNoExiste(String name, String description, String color) {
        Optional<Rol> existente = rolRepository.findByNameIgnoreCase(name);
        if (existente.isEmpty()) {
            Rol rol = new Rol(name, description, color, "ACTIVO", true, Instant.now().toString());
            List<String> titulosPermisos = PERMISOS_BASE_POR_ROL.get(name);
            if (titulosPermisos != null) {
                Set<Permiso> permisosAsociados = new HashSet<>();
                for (String titulo : titulosPermisos) {
                    permisoRepository.findByTitle(titulo).ifPresent(permisosAsociados::add);
                }
                rol.setPermisos(permisosAsociados);
            }
            rolRepository.save(rol);
        }
    }

    @Override
    public List<RolePermissionResponse> listarRoles() {
        return rolRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionCatalogResponse> listarPermisos() {
        return permisoRepository.findAll().stream()
                .map(p -> new PermissionCatalogResponse(p.getId(), p.getTitle(), p.getDescription(), p.getArea(), p.getIcon()))
                .collect(Collectors.toList());
    }

    @Override
    public RolePermissionResponse crearRol(RolePermissionRequest request) {
        String nombreNormalizado = normalizarNombre(request.getName());
        validarNombreDisponible(nombreNormalizado, null);

        Rol rol = new Rol(
                nombreNormalizado,
                request.getDescription().trim(),
                request.getColor().trim(),
                normalizarEstado(request.getStatus()),
                false,
                Instant.now().toString()
        );

        if (request.getPermissions() != null) {
            Set<Permiso> permisosValidos = new HashSet<>(permisoRepository.findAllById(request.getPermissions()));
            rol.setPermisos(permisosValidos);
        }

        Rol guardado = rolRepository.save(rol);
        return toResponse(guardado);
    }

    @Override
    public RolePermissionResponse actualizarRol(Long id, RolePermissionRequest request) {
        Rol rol = obtenerRolInterno(id);
        String nombreNormalizado = normalizarNombre(request.getName());
        validarNombreDisponible(nombreNormalizado, id);

        rol.setName(nombreNormalizado);
        rol.setDescription(request.getDescription().trim());
        rol.setColor(request.getColor().trim());
        rol.setStatus(normalizarEstado(request.getStatus()));
        rol.setUpdatedAt(Instant.now().toString());

        if (request.getPermissions() != null) {
            Set<Permiso> permisosValidos = new HashSet<>(permisoRepository.findAllById(request.getPermissions()));
            rol.setPermisos(permisosValidos);
        }

        Rol actualizado = rolRepository.save(rol);
        return toResponse(actualizado);
    }

    @Override
    public RolePermissionResponse alternarPermiso(Long id, Long permissionId) {
        Rol rol = obtenerRolInterno(id);
        Permiso permiso = permisoRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado con id: " + permissionId));

        if (rol.getPermisos().stream().anyMatch(p -> p.getId().equals(permissionId))) {
            rol.getPermisos().removeIf(p -> p.getId().equals(permissionId));
        } else {
            rol.getPermisos().add(permiso);
        }

        rol.setUpdatedAt(Instant.now().toString());
        Rol guardado = rolRepository.save(rol);
        return toResponse(guardado);
    }

    @Override
    public RolePermissionResponse restaurarPermisos(Long id) {
        Rol rol = obtenerRolInterno(id);
        List<String> permisosBase = PERMISOS_BASE_POR_ROL.get(rol.getName().toUpperCase(Locale.ROOT));
        Set<Permiso> nuevosPermisos = new HashSet<>();
        if (permisosBase != null) {
            for (String titulo : permisosBase) {
                permisoRepository.findByTitle(titulo).ifPresent(nuevosPermisos::add);
            }
        }
        rol.setPermisos(nuevosPermisos);
        rol.setUpdatedAt(Instant.now().toString());
        Rol guardado = rolRepository.save(rol);
        return toResponse(guardado);
    }

    private int contarUsuariosPorRol(String nombreRol) {
        UserRole roleEnum;
        try {
            roleEnum = UserRole.valueOf(nombreRol.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return 0;
        }

        return (int) usuarioRepository.findAll().stream()
                .map(Usuario::getRol)
                .filter(rol -> rol == roleEnum)
                .count();
    }

    private RolePermissionResponse toResponse(Rol rol) {
        List<Long> permissionIds = rol.getPermisos().stream()
                .map(Permiso::getId)
                .collect(Collectors.toList());

        return new RolePermissionResponse(
                rol.getId(),
                rol.getName(),
                rol.getDescription(),
                rol.getColor(),
                rol.getStatus(),
                contarUsuariosPorRol(rol.getName()),
                rol.getUpdatedAt(),
                permissionIds
        );
    }

    private Rol obtenerRolInterno(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }

    private void validarNombreDisponible(String nombre, Long idActual) {
        Optional<Rol> existente = rolRepository.findByNameIgnoreCase(nombre);
        if (existente.isPresent() && (idActual == null || !existente.get().getId().equals(idActual))) {
            throw new IllegalArgumentException("Ya existe un rol con ese nombre");
        }
    }

    private String normalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return nombre.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        String normalizado = estado.trim().toUpperCase(Locale.ROOT);
        if (!normalizado.equals("ACTIVO") && !normalizado.equals("PAUSADO") && !normalizado.equals("BLOQUEADO")) {
            throw new IllegalArgumentException("Estado inválido. Valores permitidos: ACTIVO, PAUSADO, BLOQUEADO");
        }
        return normalizado;
    }
}
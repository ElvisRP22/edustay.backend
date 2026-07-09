package com.edustay.backend.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edustay.backend.dto.PermissionCatalogResponse;
import com.edustay.backend.dto.RolePermissionRequest;
import com.edustay.backend.dto.RolePermissionResponse;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.UserRole;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.RolePermissionService;

import jakarta.annotation.PostConstruct;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

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

    private static final Map<String, List<Long>> PERMISOS_BASE_POR_ROL = Map.of(
            "ADMIN", List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L),
            "ARRENDADOR", List.of(3L, 5L, 6L, 7L),
            "ESTUDIANTE", List.of(3L, 7L));

    private final Map<Long, RoleTemplate> roles = new LinkedHashMap<>();
    private final AtomicLong nextRoleId = new AtomicLong(4L);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    public void init() {
        roles.put(1L, new RoleTemplate(1L, "ADMIN",
                "Control total sobre configuración, seguridad y moderación de la plataforma.", "#1d4ed8", "ACTIVO", 1,
                Instant.now().toString(), new LinkedHashSet<>(PERMISOS_BASE_POR_ROL.get("ADMIN")), true));
        roles.put(2L, new RoleTemplate(2L, "ARRENDADOR",
                "Gestiona sus habitaciones, contratos y comunicaciones con estudiantes.", "#059669", "ACTIVO", 0,
                Instant.now().toString(), new LinkedHashSet<>(PERMISOS_BASE_POR_ROL.get("ARRENDADOR")), true));
        roles.put(3L,
                new RoleTemplate(3L, "ESTUDIANTE",
                        "Accede a búsqueda, mensajes, favoritos y su perfil de verificación.", "#7c3aed", "ACTIVO", 0,
                        Instant.now().toString(), new LinkedHashSet<>(PERMISOS_BASE_POR_ROL.get("ESTUDIANTE")), true));
        sincronizarConteosIniciales();
    }

    @Override
    public List<RolePermissionResponse> listarRoles() {
        sincronizarConteosIniciales();
        return roles.values().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionCatalogResponse> listarPermisos() {
        return new ArrayList<>(CATALOGO_PERMISOS);
    }

    @Override
    public RolePermissionResponse crearRol(RolePermissionRequest request) {
        String nombreNormalizado = normalizarNombre(request.getName());
        validarNombreDisponible(nombreNormalizado, null);

        Long nuevoId = nextRoleId.getAndIncrement();
        RoleTemplate role = new RoleTemplate(
                nuevoId,
                nombreNormalizado,
                request.getDescription().trim(),
                request.getColor().trim(),
                normalizarEstado(request.getStatus()),
                request.getUsers(),
                Instant.now().toString(),
                new LinkedHashSet<>(filtrarPermisosValidos(request.getPermissions())),
                false);

        roles.put(nuevoId, role);
        return toResponse(role);
    }

    @Override
    public RolePermissionResponse actualizarRol(Long id, RolePermissionRequest request) {
        RoleTemplate role = obtenerRoleInterno(id);
        String nombreNormalizado = normalizarNombre(request.getName());
        validarNombreDisponible(nombreNormalizado, id);

        role.name = nombreNormalizado;
        role.description = request.getDescription().trim();
        role.color = request.getColor().trim();
        role.status = normalizarEstado(request.getStatus());
        role.users = request.getUsers();
        if (request.getPermissions() != null) {
            role.permissions = new LinkedHashSet<>(filtrarPermisosValidos(request.getPermissions()));
        }
        role.updatedAt = Instant.now().toString();
        return toResponse(role);
    }

    @Override
    public RolePermissionResponse alternarPermiso(Long id, Long permissionId) {
        RoleTemplate role = obtenerRoleInterno(id);
        validarPermisoExiste(permissionId);

        if (role.permissions.contains(permissionId)) {
            role.permissions.remove(permissionId);
        } else {
            role.permissions.add(permissionId);
        }

        role.updatedAt = Instant.now().toString();
        return toResponse(role);
    }

    @Override
    public RolePermissionResponse restaurarPermisos(Long id) {
        RoleTemplate role = obtenerRoleInterno(id);
        List<Long> permisosBase = PERMISOS_BASE_POR_ROL.get(role.name.toUpperCase(Locale.ROOT));
        role.permissions = permisosBase != null
                ? new LinkedHashSet<>(permisosBase)
                : new LinkedHashSet<>();
        role.updatedAt = Instant.now().toString();
        return toResponse(role);
    }

    private void sincronizarConteosIniciales() {
        for (RoleTemplate role : roles.values()) {
            if (role.builtIn) {
                role.users = contarUsuariosPorRol(role.name);
            }
        }
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

    private RolePermissionResponse toResponse(RoleTemplate role) {
        return new RolePermissionResponse(
                role.id,
                role.name,
                role.description,
                role.color,
                role.status,
                role.users,
                role.updatedAt,
                new ArrayList<>(role.permissions));
    }

    private RoleTemplate obtenerRoleInterno(Long id) {
        RoleTemplate role = roles.get(id);
        if (role == null) {
            throw new RuntimeException("Rol no encontrado con id: " + id);
        }
        return role;
    }

    private void validarNombreDisponible(String nombre, Long idActual) {
        for (RoleTemplate role : roles.values()) {
            if (role.name.equalsIgnoreCase(nombre) && (idActual == null || !role.id.equals(idActual))) {
                throw new IllegalArgumentException("Ya existe un rol con ese nombre");
            }
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

    private List<Long> filtrarPermisosValidos(Collection<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return List.of();
        }

        Set<Long> idsValidos = CATALOGO_PERMISOS.stream()
                .map(PermissionCatalogResponse::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Long> filtrados = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            if (permissionId != null && idsValidos.contains(permissionId) && !filtrados.contains(permissionId)) {
                filtrados.add(permissionId);
            }
        }
        return filtrados;
    }

    private void validarPermisoExiste(Long permissionId) {
        boolean existe = CATALOGO_PERMISOS.stream().anyMatch(permission -> permission.getId().equals(permissionId));
        if (!existe) {
            throw new IllegalArgumentException("Permiso no encontrado con id: " + permissionId);
        }
    }

    private static final class RoleTemplate {
        private final Long id;
        private String name;
        private String description;
        private String color;
        private String status;
        private Integer users;
        private String updatedAt;
        private LinkedHashSet<Long> permissions;
        private final boolean builtIn;

        private RoleTemplate(Long id, String name, String description, String color, String status,
                Integer users, String updatedAt, LinkedHashSet<Long> permissions, boolean builtIn) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.color = color;
            this.status = status;
            this.users = users;
            this.updatedAt = updatedAt;
            this.permissions = permissions;
            this.builtIn = builtIn;
        }
    }
}
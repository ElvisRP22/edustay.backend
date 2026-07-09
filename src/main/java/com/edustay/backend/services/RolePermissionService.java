package com.edustay.backend.services;

import java.util.List;

import com.edustay.backend.dto.PermissionCatalogResponse;
import com.edustay.backend.dto.RolePermissionRequest;
import com.edustay.backend.dto.RolePermissionResponse;

public interface RolePermissionService {

    List<RolePermissionResponse> listarRoles();

    List<PermissionCatalogResponse> listarPermisos();

    RolePermissionResponse crearRol(RolePermissionRequest request);

    RolePermissionResponse actualizarRol(Long id, RolePermissionRequest request);

    RolePermissionResponse alternarPermiso(Long id, Long permissionId);

    RolePermissionResponse restaurarPermisos(Long id);
}
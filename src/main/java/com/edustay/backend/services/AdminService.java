package com.edustay.backend.services;

import com.edustay.backend.dto.AdminRequest;
import com.edustay.backend.dto.AdminResponse;

import java.util.List;

public interface AdminService {

    List<AdminResponse> obtenerUsuarios();

    List<AdminResponse> obtenerPendientes();

    AdminResponse verificarCuenta(AdminRequest request);

    Long totalUsuarios();

    Long totalPendientes();

    Long totalClientes();

    Long totalArrendadores();

    List<com.edustay.backend.dto.UsuarioAdminResponse> obtenerTodosUsuariosParaAdmin();

    com.edustay.backend.dto.UsuarioAdminResponse cambiarRolDeUsuario(Long id, String nuevoRolStr);

    void eliminarUsuarioPermanente(Long id);
}
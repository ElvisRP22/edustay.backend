package com.edustay.backend.dto;

import java.util.List;

/**
 * DTO para exponer una plantilla de rol y sus permisos asociados.
 */
public class RolePermissionResponse {
    private Long id;
    private String name;
    private String description;
    private String color;
    private String status;
    private Integer users;
    private String updatedAt;
    private List<Long> permissions;

    public RolePermissionResponse() {
    }

    public RolePermissionResponse(Long id, String name, String description, String color, String status,
            Integer users, String updatedAt, List<Long> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.status = status;
        this.users = users;
        this.updatedAt = updatedAt;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Long> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Long> permissions) {
        this.permissions = permissions;
    }
}
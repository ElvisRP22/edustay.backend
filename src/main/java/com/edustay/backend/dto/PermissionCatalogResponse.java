package com.edustay.backend.dto;

/**
 * DTO para exponer el catálogo de permisos disponible para los roles
 * administrativos.
 */
public class PermissionCatalogResponse {
    private Long id;
    private String title;
    private String description;
    private String area;
    private String icon;

    public PermissionCatalogResponse() {
    }

    public PermissionCatalogResponse(Long id, String title, String description, String area, String icon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.area = area;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
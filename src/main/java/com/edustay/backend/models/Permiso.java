package com.edustay.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "permisos")
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String description;
    private String area;
    private String icon;

    public Permiso() {}

    public Permiso(String title, String description, String area, String icon) {
        this.title = title;
        this.description = description;
        this.area = area;
        this.icon = icon;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}

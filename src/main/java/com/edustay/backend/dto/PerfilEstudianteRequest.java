package com.edustay.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear o actualizar el perfil de un estudiante
 */
@Schema(description = "Datos del perfil del estudiante")
public class PerfilEstudianteRequest {

    @NotBlank(message = "La carrera es obligatoria")
    @Schema(description = "Carrera universitaria", example = "Ingeniería de Software")
    private String carrera;

    @Min(value = 1, message = "El ciclo mínimo es 1")
    @Max(value = 10, message = "El ciclo máximo es 10")
    @Schema(description = "Ciclo académico actual", example = "5")
    private Integer ciclo;

    @Schema(description = "Preferencias de convivencia con el arrendador", example = "Prefiero silencio en las noches")
    private String preferenciasConvivencia;

    @Schema(description = "URL de la foto del carnet universitario")
    private String fotoCarnetUrl;

    public PerfilEstudianteRequest() {}

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public Integer getCiclo() { return ciclo; }
    public void setCiclo(Integer ciclo) { this.ciclo = ciclo; }

    public String getPreferenciasConvivencia() { return preferenciasConvivencia; }
    public void setPreferenciasConvivencia(String preferenciasConvivencia) {
        this.preferenciasConvivencia = preferenciasConvivencia;
    }

    public String getFotoCarnetUrl() { return fotoCarnetUrl; }
    public void setFotoCarnetUrl(String fotoCarnetUrl) { this.fotoCarnetUrl = fotoCarnetUrl; }
}

package com.edustay.backend.controllers;

import com.edustay.backend.dto.ReglaResponse;
import com.edustay.backend.dto.ServicioResponse;
import com.edustay.backend.repositories.ReglaRepository;
import com.edustay.backend.repositories.ServicioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints públicos para que el frontend cargue catálogos de servicios y reglas.
 */
@RestController
@RequestMapping("/api/catalogos/habitaciones")
@Tag(name = "Catálogos de Habitaciones", description = "Servicios y reglas disponibles para habitaciones")
public class CatalogoHabitacionController {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ReglaRepository reglaRepository;

    @GetMapping("/servicios")
    @Operation(summary = "Listar servicios", description = "Devuelve los servicios disponibles para asociar a una habitación")
    @ApiResponse(responseCode = "200", description = "Lista de servicios")
    public ResponseEntity<List<ServicioResponse>> listarServicios() {
        List<ServicioResponse> servicios = servicioRepository.findAll().stream()
                .map(servicio -> new ServicioResponse(servicio.getId(), servicio.getNombre()))
                .toList();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/reglas")
    @Operation(summary = "Listar reglas", description = "Devuelve las reglas disponibles para asociar a una habitación")
    @ApiResponse(responseCode = "200", description = "Lista de reglas")
    public ResponseEntity<List<ReglaResponse>> listarReglas() {
        List<ReglaResponse> reglas = reglaRepository.findAll().stream()
                .map(regla -> new ReglaResponse(regla.getId(), regla.getDescripcion()))
                .toList();
        return ResponseEntity.ok(reglas);
    }
}
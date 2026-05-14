package com.edustay.backend.services;

import com.edustay.backend.dto.HabitacionRequest;
import com.edustay.backend.dto.HabitacionResponse;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.RoomStatus;
import com.edustay.backend.repositories.HabitacionRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejo de habitaciones
 */
@Service
@Transactional
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Coordenadas de UTP Piura (aproximadas)
    private static final double UTP_LATITUD = -5.192;
    private static final double UTP_LONGITUD = -80.632;
    
    // Velocidad promedio en km/h para calcular minutos
    private static final double VELOCIDAD_PROMEDIO_KMH = 30.0;

    /**
     * Crea una nueva habitación
     */
    public HabitacionResponse crearHabitacion(HabitacionRequest request, Long arrendadorId) {
        Usuario arrendador = usuarioRepository.findById(arrendadorId)
                .orElseThrow(() -> new RuntimeException("Arrendador no encontrado"));

        Habitacion habitacion = new Habitacion();
        habitacion.setArrendador(arrendador);
        habitacion.setTitulo(request.getTitulo());
        habitacion.setDescripcion(request.getDescripcion());
        habitacion.setPrecio(request.getPrecio());
        habitacion.setDireccion(request.getDireccion());
        habitacion.setLatitud(request.getLatitud());
        habitacion.setLongitud(request.getLongitud());
        
        // Calcular distancia a UTP
        Integer distanciaMinutos = calcularDistanciaUtpMinutos(request.getLatitud(), request.getLongitud());
        habitacion.setDistanciaUtpMinutos(distanciaMinutos);
        
        habitacion.setEstado(RoomStatus.DISPONIBLE);

        Habitacion guardada = habitacionRepository.save(habitacion);
        return convertirAResponse(guardada);
    }

    /**
     * Obtiene todas las habitaciones
     */
    public List<HabitacionResponse> obtenerTodas() {
        return habitacionRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una habitación por ID
     */
    public HabitacionResponse obtenerPorId(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
        return convertirAResponse(habitacion);
    }

    /**
     * Obtiene habitaciones de un arrendador
     */
    public List<HabitacionResponse> obtenerPorArrendador(Long arrendadorId) {
        return habitacionRepository.findByArrendadorId(arrendadorId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene habitaciones disponibles
     */
    public List<HabitacionResponse> obtenerDisponibles() {
        return habitacionRepository.findByEstado(RoomStatus.DISPONIBLE).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una habitación
     */
    public HabitacionResponse actualizarHabitacion(Long id, HabitacionRequest request, Long arrendadorId) {
        Habitacion habitacion = habitacionRepository.findByIdAndArrendadorId(id, arrendadorId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada o no pertenece al arrendador"));

        habitacion.setTitulo(request.getTitulo());
        habitacion.setDescripcion(request.getDescripcion());
        habitacion.setPrecio(request.getPrecio());
        habitacion.setDireccion(request.getDireccion());
        habitacion.setLatitud(request.getLatitud());
        habitacion.setLongitud(request.getLongitud());
        
        // Recalcular distancia
        Integer distanciaMinutos = calcularDistanciaUtpMinutos(request.getLatitud(), request.getLongitud());
        habitacion.setDistanciaUtpMinutos(distanciaMinutos);

        Habitacion actualizada = habitacionRepository.save(habitacion);
        return convertirAResponse(actualizada);
    }

    /**
     * Elimina una habitación
     */
    public void eliminarHabitacion(Long id, Long arrendadorId) {
        Habitacion habitacion = habitacionRepository.findByIdAndArrendadorId(id, arrendadorId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada o no pertenece al arrendador"));
        habitacionRepository.delete(habitacion);
    }

    /**
     * Calcula la distancia en minutos desde una ubicación hasta UTP
     * Usa la fórmula de Haversine para calcular distancia en km
     * Luego convierte a minutos asumiendo velocidad promedio
     */
    private Integer calcularDistanciaUtpMinutos(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) {
            return 0;
        }

        // Fórmula de Haversine
        double distanciaKm = calcularDistanciaHaversine(latitud, longitud, UTP_LATITUD, UTP_LONGITUD);
        
        // Convertir km a minutos (distancia / velocidad * 60)
        double minutos = (distanciaKm / VELOCIDAD_PROMEDIO_KMH) * 60;
        
        return (int) Math.round(minutos);
    }

    /**
     * Implementa la fórmula de Haversine para calcular distancia entre dos puntos
     * Retorna la distancia en kilómetros
     */
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int RADIO_TIERRA_KM = 6371; // Radio de la Tierra en km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = RADIO_TIERRA_KM * c;

        return distancia;
    }

    /**
     * Convierte un Habitacion a HabitacionResponse
     */
    private HabitacionResponse convertirAResponse(Habitacion habitacion) {
        return new HabitacionResponse(
                habitacion.getId(),
                habitacion.getTitulo(),
                habitacion.getDescripcion(),
                habitacion.getPrecio(),
                habitacion.getDireccion(),
                habitacion.getLatitud(),
                habitacion.getLongitud(),
                habitacion.getDistanciaUtpMinutos(),
                habitacion.getEstado(),
                habitacion.getFechaPublicacion(),
                habitacion.getArrendador().getId(),
                habitacion.getArrendador().getNombre()
        );
    }
}

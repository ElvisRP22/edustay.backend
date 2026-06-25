package com.edustay.backend.services.impl;

import com.edustay.backend.dto.ReglaResponse;
import com.edustay.backend.dto.HabitacionRequest;
import com.edustay.backend.dto.HabitacionResponse;
import com.edustay.backend.dto.ServicioResponse;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.Regla;
import com.edustay.backend.models.Servicio;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.RoomStatus;
import com.edustay.backend.models.enums.VerificationStatus;
import com.edustay.backend.repositories.ReglaRepository;
import com.edustay.backend.repositories.HabitacionRepository;
import com.edustay.backend.repositories.ServicioRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Habitaciones
 */
@Service
@Transactional
public class HabitacionServiceImpl implements HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ReglaRepository reglaRepository;

    // Coordenadas de UTP Piura (aproximadas)
    private static final double UTP_LATITUD = -5.192;
    private static final double UTP_LONGITUD = -80.632;
    
    // Velocidad promedio en km/h para calcular minutos
    private static final double VELOCIDAD_PROMEDIO_KMH = 30.0;

    @Override
    public HabitacionResponse crearHabitacion(HabitacionRequest request, Long arrendadorId) {
        Usuario arrendador = usuarioRepository.findById(arrendadorId)
                .orElseThrow(() -> new RuntimeException("Arrendador no encontrado"));

        if (arrendador.getIdentidadVerificada() != VerificationStatus.VERIFICADO) {
            throw new RuntimeException("Tu cuenta aún no ha sido verificada. Debes subir tus documentos y esperar a que sean aprobados por un administrador para poder publicar habitaciones.");
        }

        Habitacion habitacion = new Habitacion();
        habitacion.setArrendador(arrendador);
        habitacion.setTitulo(request.getTitulo());
        habitacion.setDescripcion(request.getDescripcion());
        habitacion.setPrecio(request.getPrecio());
        habitacion.setDireccion(request.getDireccion());
        habitacion.setLatitud(request.getLatitud());
        habitacion.setLongitud(request.getLongitud());
        habitacion.setServicios(cargarServicios(request.getServicioIds()));
        habitacion.setReglas(cargarReglas(request.getReglaIds()));
        
        // Calcular distancia a UTP
        Integer distanciaMinutos = calcularDistanciaUtpMinutos(request.getLatitud(), request.getLongitud());
        habitacion.setDistanciaUtpMinutos(distanciaMinutos);
        
        habitacion.setEstado(RoomStatus.DISPONIBLE);

        guardarFotos(habitacion, request.getFotos());

        Habitacion guardada = habitacionRepository.save(habitacion);
        return convertirAResponse(guardada);
    }

    @Override
    public List<HabitacionResponse> obtenerTodas() {
        return habitacionRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HabitacionResponse obtenerPorId(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
        return convertirAResponse(habitacion);
    }

    @Override
    public List<HabitacionResponse> obtenerPorArrendador(Long arrendadorId) {
        return habitacionRepository.findByArrendadorId(arrendadorId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HabitacionResponse> obtenerDisponibles() {
        return habitacionRepository.findByEstado(RoomStatus.DISPONIBLE).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HabitacionResponse actualizarHabitacion(Long id, HabitacionRequest request, Long arrendadorId) {
        Habitacion habitacion = habitacionRepository.findByIdAndArrendadorId(id, arrendadorId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada o no pertenece al arrendador"));

        Usuario arrendador = habitacion.getArrendador();
        if (arrendador != null && arrendador.getIdentidadVerificada() != VerificationStatus.VERIFICADO) {
            throw new RuntimeException("Tu cuenta aún no ha sido verificada. Debes subir tus documentos y esperar a que sean aprobados por un administrador para poder modificar habitaciones.");
        }

        habitacion.setTitulo(request.getTitulo());
        habitacion.setDescripcion(request.getDescripcion());
        habitacion.setPrecio(request.getPrecio());
        habitacion.setDireccion(request.getDireccion());
        habitacion.setLatitud(request.getLatitud());
        habitacion.setLongitud(request.getLongitud());
        habitacion.setServicios(cargarServicios(request.getServicioIds()));
        habitacion.setReglas(cargarReglas(request.getReglaIds()));
        
        // Recalcular distancia
        Integer distanciaMinutos = calcularDistanciaUtpMinutos(request.getLatitud(), request.getLongitud());
        habitacion.setDistanciaUtpMinutos(distanciaMinutos);

        guardarFotos(habitacion, request.getFotos());

        Habitacion actualizada = habitacionRepository.save(habitacion);
        return convertirAResponse(actualizada);
    }

    @Override
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
        Set<ServicioResponse> servicios = habitacion.getServicios() == null
            ? Collections.emptySet()
            : habitacion.getServicios().stream()
            .map(servicio -> new ServicioResponse(servicio.getId(), servicio.getNombre()))
            .collect(Collectors.toSet());

        Set<ReglaResponse> reglas = habitacion.getReglas() == null
            ? Collections.emptySet()
            : habitacion.getReglas().stream()
            .map(regla -> new ReglaResponse(regla.getId(), regla.getDescripcion()))
            .collect(Collectors.toSet());

        HabitacionResponse response = new HabitacionResponse(
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
                habitacion.getArrendador().getNombre(),
                servicios,
                reglas
        );

        if (habitacion.getFotos() != null) {
            response.setFotos(habitacion.getFotos().stream()
                .map(com.edustay.backend.models.FotoHabitacion::getUrl)
                .collect(Collectors.toList()));
        } else {
            response.setFotos(Collections.emptyList());
        }

        return response;
    }

    private Set<Servicio> cargarServicios(Set<Long> servicioIds) {
        if (servicioIds == null || servicioIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Servicio> servicios = servicioRepository.findAllById(servicioIds);
        if (servicios.size() != servicioIds.size()) {
            throw new RuntimeException("Uno o más servicios no existen");
        }
        return new HashSet<>(servicios);
    }

    private Set<Regla> cargarReglas(Set<Long> reglaIds) {
        if (reglaIds == null || reglaIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Regla> reglas = reglaRepository.findAllById(reglaIds);
        if (reglas.size() != reglaIds.size()) {
            throw new RuntimeException("Una o más reglas no existen");
        }
        return new HashSet<>(reglas);
    }

    @Override
    public List<HabitacionResponse> buscarHabitaciones(Double lat, Double lon, Double radioKm, Double maxPrecio, String query, Boolean soloDisponibles) {
        List<Habitacion> todas = (soloDisponibles != null && soloDisponibles)
                ? habitacionRepository.findByEstado(RoomStatus.DISPONIBLE)
                : habitacionRepository.findAll();

        double finalRadioKm = radioKm != null ? radioKm : 5.0;
        String lowercaseQuery = query != null ? query.trim().toLowerCase() : "";

        return todas.stream()
                // Filtrar por precio si es provisto
                .filter(h -> maxPrecio == null || h.getPrecio() <= maxPrecio)
                // Filtrar por query si es provisto (busca en título y dirección)
                .filter(h -> lowercaseQuery.isEmpty() 
                        || h.getTitulo().toLowerCase().contains(lowercaseQuery) 
                        || h.getDireccion().toLowerCase().contains(lowercaseQuery))
                // Calcular distancia si lat/lon son provistos
                .map(h -> {
                    HabitacionResponse res = convertirAResponse(h);
                    if (lat != null && lon != null && h.getLatitud() != null && h.getLongitud() != null) {
                        double dist = calcularDistanciaHaversine(lat, lon, h.getLatitud(), h.getLongitud());
                        res.setDistanciaKm(Math.round(dist * 100.0) / 100.0); // Redondear a 2 decimales
                    }
                    return res;
                })
                // Filtrar por radio si se calculó distancia y si se especificó ubicación
                .filter(res -> {
                    if (lat != null && lon != null) {
                        return res.getDistanciaKm() != null && res.getDistanciaKm() <= finalRadioKm;
                    }
                    return true;
                })
                // Ordenar por distancia (si lat/lon están presentes) o por ID descendente
                .sorted((a, b) -> {
                    if (lat != null && lon != null && a.getDistanciaKm() != null && b.getDistanciaKm() != null) {
                        return Double.compare(a.getDistanciaKm(), b.getDistanciaKm());
                    }
                    return Long.compare(b.getId(), a.getId());
                })
                .collect(Collectors.toList());
    }

    private void guardarFotos(Habitacion habitacion, java.util.List<String> urls) {
        if (habitacion.getFotos() == null) {
            habitacion.setFotos(new java.util.ArrayList<>());
        } else {
            habitacion.getFotos().clear();
        }

        if (urls != null) {
            boolean esPrimera = true;
            for (String url : urls) {
                if (url != null && !url.isBlank()) {
                    com.edustay.backend.models.FotoHabitacion foto = new com.edustay.backend.models.FotoHabitacion();
                    foto.setUrl(url.trim());
                    foto.setEsPrincipal(esPrimera);
                    foto.setHabitacion(habitacion);
                    habitacion.getFotos().add(foto);
                    esPrimera = false;
                }
            }
        }
    }
}

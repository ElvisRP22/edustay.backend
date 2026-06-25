package com.edustay.backend.services.impl;

import com.edustay.backend.dto.AlquilerRequest;
import com.edustay.backend.dto.AlquilerResponse;
import com.edustay.backend.models.AlquilerActivo;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.enums.RoomStatus;
import com.edustay.backend.models.enums.VerificationStatus;
import com.edustay.backend.repositories.AlquilerActivoRepository;
import com.edustay.backend.repositories.HabitacionRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.AlquilerService;
import com.edustay.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Alquileres Activos
 */
@Service
@Transactional
public class AlquilerServiceImpl implements AlquilerService {

    @Autowired
    private AlquilerActivoRepository alquilerActivoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public AlquilerResponse crearAlquiler(Long estudianteId, AlquilerRequest request) {
        // Validar que la habitación no esté ya alquilada
        if (alquilerActivoRepository.existsByHabitacionId(request.getHabitacionId())) {
            throw new RuntimeException("La habitación ya está alquilada");
        }

        Habitacion habitacion = habitacionRepository.findById(request.getHabitacionId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con id: " + request.getHabitacionId()));

        if (habitacion.getEstado() != RoomStatus.DISPONIBLE) {
            throw new RuntimeException("La habitación no está disponible para alquilar");
        }

        Usuario estudiante = usuarioRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + estudianteId));

        // Verificar que el estudiante esté verificado
        if (estudiante.getIdentidadVerificada() != VerificationStatus.VERIFICADO) {
            throw new RuntimeException("Tu cuenta aún no ha sido verificada. Debes subir tus documentos y esperar a que sean aprobados por un administrador para poder realizar un alquiler.");
        }

        AlquilerActivo alquiler = new AlquilerActivo();
        alquiler.setHabitacion(habitacion);
        alquiler.setEstudiante(estudiante);
        alquiler.setMontoPactado(request.getMontoPactado());
        if (request.getFechaInicio() != null) {
            alquiler.setFechaInicio(request.getFechaInicio());
        }
        if (request.getContratoUrl() != null) {
            alquiler.setContratoUrl(request.getContratoUrl());
        }

        // Marcar la habitación como ocupada
        habitacion.setEstado(RoomStatus.OCUPADO);
        habitacionRepository.save(habitacion);

        AlquilerActivo guardado = alquilerActivoRepository.save(alquiler);

        // Enviar correos de confirmación a estudiante y arrendador
        try {
            Usuario arr = habitacion.getArrendador();
            String emailEst = estudiante.getEmail();
            String nombreEst = estudiante.getNombre() + " " + estudiante.getApellido();
            String emailArr = arr.getEmail();
            String nombreArr = arr.getNombre() + " " + arr.getApellido();
            String tituloHab = habitacion.getTitulo();
            String direccionHab = habitacion.getDireccion();
            Double monto = alquiler.getMontoPactado();
            String urlContrato = alquiler.getContratoUrl();

            emailService.enviarConfirmacionAlquilerEstudiante(
                    emailEst, nombreEst, nombreArr, tituloHab, direccionHab, monto, urlContrato
            );
            emailService.enviarConfirmacionAlquilerArrendador(
                    emailArr, nombreArr, nombreEst, tituloHab, direccionHab, monto, urlContrato
            );
        } catch (Exception e) {
            System.err.println("❌ Error al enviar correos de confirmación de alquiler: " + e.getMessage());
        }

        return convertirAResponse(guardado);
    }

    @Override
    public void finalizarAlquiler(Long alquilerId, Long solicitanteId) {
        AlquilerActivo alquiler = alquilerActivoRepository.findById(alquilerId)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado con id: " + alquilerId));

        Usuario solicitante = usuarioRepository.findById(solicitanteId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + solicitanteId));

        // Solo puede finalizar el arrendador propietario o un admin
        boolean esArrendador = alquiler.getHabitacion().getArrendador().getId().equals(solicitanteId);
        boolean esAdmin = solicitante.getRol().name().equals("ADMIN");
        if (!esArrendador && !esAdmin) {
            throw new RuntimeException("No tienes permiso para finalizar este alquiler");
        }

        // Liberar la habitación
        Habitacion habitacion = alquiler.getHabitacion();
        habitacion.setEstado(RoomStatus.DISPONIBLE);
        habitacionRepository.save(habitacion);

        alquilerActivoRepository.delete(alquiler);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlquilerResponse> obtenerAlquileresDeEstudiante(Long estudianteId) {
        return alquilerActivoRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlquilerResponse> obtenerAlquileresDeArrendador(Long arrendadorId) {
        return alquilerActivoRepository.findByHabitacionArrendadorId(arrendadorId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlquilerResponse> obtenerTodos() {
        return alquilerActivoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AlquilerResponse obtenerPorId(Long id) {
        AlquilerActivo alquiler = alquilerActivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado con id: " + id));
        return convertirAResponse(alquiler);
    }

    private AlquilerResponse convertirAResponse(AlquilerActivo a) {
        Habitacion h = a.getHabitacion();
        Usuario est = a.getEstudiante();
        Usuario arr = h.getArrendador();
        AlquilerResponse res = new AlquilerResponse(
                a.getId(),
                h.getId(),
                h.getTitulo(),
                h.getDireccion(),
                est.getId(),
                est.getNombre() + " " + est.getApellido(),
                arr.getId(),
                arr.getNombre() + " " + arr.getApellido(),
                a.getMontoPactado(),
                a.getFechaInicio(),
                a.getFechaRegistro()
        );
        res.setContratoUrl(a.getContratoUrl());
        return res;
    }
}

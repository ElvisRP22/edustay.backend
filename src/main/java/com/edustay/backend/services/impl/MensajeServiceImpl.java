package com.edustay.backend.services.impl;

import com.edustay.backend.dto.MensajeRequest;
import com.edustay.backend.dto.MensajeResponse;
import com.edustay.backend.dto.ResultadoModeracion;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.Mensaje;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.repositories.HabitacionRepository;
import com.edustay.backend.repositories.MensajeRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.services.MensajeService;
import com.edustay.backend.services.ModeracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Mensajería
 */
@Service
@Transactional
public class MensajeServiceImpl implements MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private ModeracionService moderacionService;

    @Override
    public MensajeResponse enviarMensaje(Long emisorId, MensajeRequest request) {
        if (emisorId.equals(request.getReceptorId())) {
            throw new IllegalArgumentException("No puedes enviarte mensajes a ti mismo");
        }

        Usuario emisor = usuarioRepository.findById(emisorId)
                .orElseThrow(() -> new RuntimeException("Emisor no encontrado con id: " + emisorId));

        Usuario receptor = usuarioRepository.findById(request.getReceptorId())
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado con id: " + request.getReceptorId()));

        Habitacion habitacion = habitacionRepository.findById(request.getHabitacionId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con id: " + request.getHabitacionId()));

        // Evaluar la moderación del mensaje
        ResultadoModeracion resultado = moderacionService.moderarContenido(request.getContenido());
        if (resultado.isBlocked()) {
            throw new IllegalArgumentException("Tu mensaje infringe las normas comunitarias de seguridad de EduStay (violencia/acoso detectado).");
        }

        Mensaje mensaje = new Mensaje(emisor, receptor, habitacion, request.getContenido());
        mensaje.setModerado(resultado.isFlagged());
        mensaje.setCategoriaModeracion(resultado.getCategoria());
        mensaje.setBloqueado(resultado.isBlocked());
        if (resultado.isFlagged()) {
            mensaje.setEstadoModeracion("PENDIENTE");
        }

        mensajeRepository.save(mensaje);
        return convertirAResponse(mensaje);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MensajeResponse> obtenerConversacion(Long habitacionId, Long usuarioA, Long usuarioB) {
        return mensajeRepository.findConversacion(habitacionId, usuarioA, usuarioB).stream()
                .filter(m -> !m.isBloqueado())
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MensajeResponse> obtenerBandejaEntrada(Long receptorId) {
        return mensajeRepository.findByReceptorIdOrderByFechaEnvioDesc(receptorId).stream()
                .filter(m -> !m.isBloqueado())
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void marcarComoLeido(Long habitacionId, Long emisorId, Long receptorId) {
        List<Mensaje> mensajes = mensajeRepository.findConversacion(habitacionId, emisorId, receptorId);
        mensajes.stream()
                .filter(m -> m.getReceptor().getId().equals(receptorId) && !m.isLeido())
                .forEach(m -> {
                    m.setLeido(true);
                    mensajeRepository.save(m);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public long contarNoLeidos(Long receptorId) {
        return mensajeRepository.countByReceptorIdAndLeidoFalse(receptorId);
    }

    private MensajeResponse convertirAResponse(Mensaje m) {
        return new MensajeResponse(
                m.getId(),
                m.getEmisor().getId(),
                m.getEmisor().getNombre() + " " + m.getEmisor().getApellido(),
                m.getReceptor().getId(),
                m.getReceptor().getNombre() + " " + m.getReceptor().getApellido(),
                m.getHabitacion().getId(),
                m.getHabitacion().getTitulo(),
                m.getContenido(),
                m.isLeido(),
                m.getFechaEnvio(),
                m.isModerado(),
                m.getCategoriaModeracion(),
                m.isBloqueado(),
                m.getEstadoModeracion()
        );
    }
}

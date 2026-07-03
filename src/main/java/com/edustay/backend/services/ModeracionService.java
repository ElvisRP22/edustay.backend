package com.edustay.backend.services;

import com.edustay.backend.dto.ResultadoModeracion;

public interface ModeracionService {
    ResultadoModeracion moderarContenido(String contenido);
}

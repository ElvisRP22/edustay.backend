package com.edustay.backend.models.enums;

public enum ReportStatus {
    ABIERTO,      // Reporte recién creado
    EN_REVISION,  // El admin lo está analizando
    RESUELTO,     // Se tomó una acción (ej. se borró la habitación)
    DESESTIMADO   // No se encontró falta alguna
}
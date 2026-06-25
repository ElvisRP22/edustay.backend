package com.edustay.backend.services;

/**
 * Servicio para el envío de correos electrónicos.
 */
public interface EmailService {
    
    /**
     * Envía el código de verificación OTP al correo electrónico especificado.
     * 
     * @param destinatario Dirección de correo del destinatario.
     * @param codigo       Código OTP de 6 dígitos.
     */
    void enviarCodigoOtp(String destinatario, String codigo);

    /**
     * Envía un correo de confirmación al estudiante cuando completa el alquiler de una habitación.
     */
    void enviarConfirmacionAlquilerEstudiante(String emailEstudiante, String nombreEstudiante, String nombreArrendador,
                                             String tituloHabitacion, String direccionHabitacion, Double monto, String urlContrato);

    /**
     * Envía un correo de confirmación al arrendador cuando un estudiante completa el alquiler de su habitación.
     */
    void enviarConfirmacionAlquilerArrendador(String emailArrendador, String nombreArrendador, String nombreEstudiante,
                                             String tituloHabitacion, String direccionHabitacion, Double monto, String urlContrato);

    /**
     * Envía un correo al usuario notificándole el resultado de la verificación de su documento.
     */
    void enviarResultadoVerificacionDocumento(String emailUsuario, String nombreUsuario, String tipoDocumento,
                                             String estadoDocumento, String comentarioAdmin, String estadoGlobalUsuario);
}

package com.edustay.backend.services.impl;

import com.edustay.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación del servicio de envío de correos utilizando Resend.
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${app.resend.api-key:}")
    private String apiKey;

    @Value("${app.resend.from-email:onboarding@resend.dev}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void enviarCodigoOtp(String destinatario, String codigo) {
        // Log de respaldo en consola para desarrollo
        System.out.println("\n==================================================");
        System.out.println("✉️ [EMAIL SENDER] Preparando envío de OTP a " + destinatario);
        System.out.println("🔑 CÓDIGO DE VERIFICACIÓN (OTP): " + codigo);
        System.out.println("==================================================\n");

        if (apiKey == null || apiKey.isBlank() || apiKey.equals("re_default_key")) {
            System.out.println("⚠️ Resend API Key no configurada. El código OTP solo estará disponible en consola.");
            return;
        }

        try {
            String url = "https://api.resend.com/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("from", "EduStay <" + fromEmail + ">");
            body.put("to", Collections.singletonList(destinatario));
            body.put("subject", "EduStay - Código de Verificación OTP");
            
            String htmlContent = "<div style=\"font-family: sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);\">" +
                    "  <h2 style=\"color: #1e3a8a; margin-top: 0;\">EduStay</h2>" +
                    "  <p style=\"font-size: 16px; color: #334155;\">¡Hola!</p>" +
                    "  <p style=\"font-size: 16px; color: #334155; line-height: 1.5;\">Gracias por registrarte en EduStay. Tu código de verificación de correo electrónico es:</p>" +
                    "  <div style=\"background-color: #f1f5f9; padding: 15px; border-radius: 8px; text-align: center; margin: 20px 0;\">" +
                    "    <span style=\"font-size: 32px; font-weight: bold; letter-spacing: 4px; color: #2563eb;\">" + codigo + "</span>" +
                    "  </div>" +
                    "  <p style=\"font-size: 14px; color: #64748b;\">Este código es de un solo uso y expirará en 15 minutos.</p>" +
                    "  <p style=\"font-size: 14px; color: #64748b;\">Si no solicitaste este código, puedes ignorar este mensaje de forma segura.</p>" +
                    "  <hr style=\"border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;\" />" +
                    "  <p style=\"font-size: 12px; color: #94a3b8; text-align: center;\">Este es un correo automático, por favor no respondas a este mensaje.</p>" +
                    "</div>";
            
            body.put("html", htmlContent);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("✅ Correo con código OTP enviado exitosamente a " + destinatario + " mediante Resend.");

        } catch (Exception e) {
            System.err.println("❌ Error al enviar el correo mediante Resend: " + e.getMessage());
            // Capturamos el error para no entorpecer el registro del usuario si la API falla
        }
    }

    @Override
    public void enviarConfirmacionAlquilerEstudiante(String emailEstudiante, String nombreEstudiante, String nombreArrendador,
                                                     String tituloHabitacion, String direccionHabitacion, Double monto, String urlContrato) {
        // Log de respaldo en consola
        System.out.println("\n==================================================");
        System.out.println("✉️ [EMAIL SENDER] Enviando confirmación de alquiler a Estudiante: " + emailEstudiante);
        System.out.println("🏠 Habitación: " + tituloHabitacion + " (Monto: S/ " + monto + ")");
        System.out.println("==================================================\n");

        if (apiKey == null || apiKey.isBlank() || apiKey.equals("re_default_key")) {
            System.out.println("⚠️ Resend API Key no configurada. Se omitió el envío real.");
            return;
        }

        try {
            String url = "https://api.resend.com/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("from", "EduStay <" + fromEmail + ">");
            body.put("to", Collections.singletonList(emailEstudiante));
            body.put("subject", "EduStay - ¡Tu alquiler ha sido confirmado! 🎉");

            String htmlContent = "<div style=\"font-family: sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);\">" +
                    "  <h2 style=\"color: #2563eb; margin-top: 0;\">¡Felicitaciones, " + nombreEstudiante + "! 🎉</h2>" +
                    "  <p style=\"font-size: 16px; color: #334155;\">Tu alquiler ha sido registrado exitosamente en **EduStay**.</p>" +
                    "  " +
                    "  <div style=\"background-color: #f8fafc; border: 1px solid #e2e8f0; padding: 15px; border-radius: 8px; margin: 20px 0;\">" +
                    "    <h3 style=\"color: #0f172a; margin-top: 0; font-size: 16px;\">Resumen del Alquiler:</h3>" +
                    "    <table style=\"width: 100%; font-size: 14px; border-collapse: collapse; color: #475569;\">" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Habitación:</strong></td><td>" + tituloHabitacion + "</td></tr>" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Dirección:</strong></td><td>" + direccionHabitacion + "</td></tr>" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Monto Mensual:</strong></td><td style=\"color: #2563eb; font-weight: bold;\">S/ " + monto + "</td></tr>" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Arrendador:</strong></td><td>" + nombreArrendador + "</td></tr>" +
                    "    </table>" +
                    "  </div>" +
                    "  " +
                    "  <p style=\"font-size: 15px; color: #334155;\">Puedes acceder al contrato de arrendamiento digital que se cargó para este alquiler haciendo clic en el siguiente enlace:</p>" +
                    "  <div style=\"text-align: center; margin: 25px 0;\">" +
                    "    <a href=\"" + urlContrato + "\" target=\"_blank\" style=\"background-color: #2563eb; color: white; padding: 12px 24px; border-radius: 8px; text-decoration: none; font-weight: bold; display: inline-block;\">Ver Contrato de Alquiler (PDF)</a>" +
                    "  </div>" +
                    "  " +
                    "  <p style=\"font-size: 14px; color: #64748b; margin-top: 25px;\"><strong>Aviso Legal de EduStay:</strong> Recuerda que EduStay es solo una plataforma facilitadora de conexión. Las responsabilidades derivadas del cumplimiento del contrato, pagos y condiciones recaen exclusivamente en ti y en el arrendador.</p>" +
                    "  <hr style=\"border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;\" />" +
                    "  <p style=\"font-size: 12px; color: #94a3b8; text-align: center;\">El equipo de EduStay</p>" +
                    "</div>";

            body.put("html", htmlContent);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("✅ Correo de confirmación de alquiler enviado exitosamente a Estudiante: " + emailEstudiante);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo de confirmación a estudiante: " + e.getMessage());
        }
    }

    @Override
    public void enviarConfirmacionAlquilerArrendador(String emailArrendador, String nombreArrendador, String nombreEstudiante,
                                                     String tituloHabitacion, String direccionHabitacion, Double monto, String urlContrato) {
        // Log de respaldo en consola
        System.out.println("\n==================================================");
        System.out.println("✉️ [EMAIL SENDER] Enviando confirmación de alquiler a Arrendador: " + emailArrendador);
        System.out.println("🏠 Habitación: " + tituloHabitacion + " (Inquilino: " + nombreEstudiante + ")");
        System.out.println("==================================================\n");

        if (apiKey == null || apiKey.isBlank() || apiKey.equals("re_default_key")) {
            System.out.println("⚠️ Resend API Key no configurada. Se omitió el envío real.");
            return;
        }

        try {
            String url = "https://api.resend.com/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("from", "EduStay <" + fromEmail + ">");
            body.put("to", Collections.singletonList(emailArrendador));
            body.put("subject", "EduStay - ¡Tu habitación ha sido alquilada! 🏠");

            String htmlContent = "<div style=\"font-family: sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);\">" +
                    "  <h2 style=\"color: #1e3a8a; margin-top: 0;\">Estimado(a) " + nombreArrendador + ",</h2>" +
                    "  <p style=\"font-size: 16px; color: #334155;\">Te informamos que un estudiante ha alquilado tu habitación en **EduStay**.</p>" +
                    "  " +
                    "  <div style=\"background-color: #f8fafc; border: 1px solid #e2e8f0; padding: 15px; border-radius: 8px; margin: 20px 0;\">" +
                    "    <h3 style=\"color: #0f172a; margin-top: 0; font-size: 16px;\">Detalles del Alquiler:</h3>" +
                    "    <table style=\"width: 100%; font-size: 14px; border-collapse: collapse; color: #475569;\">" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Habitación:</strong></td><td>" + tituloHabitacion + "</td></tr>" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Dirección:</strong></td><td>" + direccionHabitacion + "</td></tr>" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Monto Mensual:</strong></td><td style=\"color: #1e3a8a; font-weight: bold;\">S/ " + monto + "</td></tr>" +
                    "      <tr><td style=\"padding: 4px 0;\"><strong>Inquilino (Estudiante):</strong></td><td>" + nombreEstudiante + "</td></tr>" +
                    "    </table>" +
                    "  </div>" +
                    "  " +
                    "  <p style=\"font-size: 15px; color: #334155;\">Puedes acceder al contrato de arrendamiento digital firmado y cargado por el estudiante haciendo clic en el siguiente enlace:</p>" +
                    "  <div style=\"text-align: center; margin: 25px 0;\">" +
                    "    <a href=\"" + urlContrato + "\" target=\"_blank\" style=\"background-color: #1e3a8a; color: white; padding: 12px 24px; border-radius: 8px; text-decoration: none; font-weight: bold; display: inline-block;\">Ver Contrato de Alquiler (PDF)</a>" +
                    "  </div>" +
                    "  " +
                    "  <p style=\"font-size: 14px; color: #64748b; margin-top: 25px;\"><strong>Aviso Legal de EduStay:</strong> Recuerda que EduStay es solo una plataforma facilitadora de conexión. Las responsabilidades derivadas del cumplimiento del contrato, pagos y condiciones recaen exclusivamente en ti y en el estudiante.</p>" +
                    "  <hr style=\"border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;\" />" +
                    "  <p style=\"font-size: 12px; color: #94a3b8; text-align: center;\">El equipo de EduStay</p>" +
                    "</div>";

            body.put("html", htmlContent);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("✅ Correo de confirmación de alquiler enviado exitosamente a Arrendador: " + emailArrendador);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo de confirmación a arrendador: " + e.getMessage());
        }
    }

    @Override
    public void enviarResultadoVerificacionDocumento(String emailUsuario, String nombreUsuario, String tipoDocumento,
                                                     String estadoDocumento, String comentarioAdmin, String estadoGlobalUsuario) {
        // Log de respaldo en consola
        System.out.println("\n==================================================");
        System.out.println("✉️ [EMAIL SENDER] Enviando resultado de verificación a: " + emailUsuario);
        System.out.println("📄 Documento: " + tipoDocumento + " | Resultado: " + estadoDocumento);
        System.out.println("🔑 Estado Global Cuenta: " + estadoGlobalUsuario);
        System.out.println("==================================================\n");

        if (apiKey == null || apiKey.isBlank() || apiKey.equals("re_default_key")) {
            System.out.println("⚠️ Resend API Key no configurada. Se omitió el envío real.");
            return;
        }

        try {
            String url = "https://api.resend.com/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("from", "EduStay <" + fromEmail + ">");
            body.put("to", Collections.singletonList(emailUsuario));
            
            boolean aprobado = "VERIFICADO".equalsIgnoreCase(estadoDocumento);
            String subject = aprobado 
                    ? "EduStay - Documento Verificado Exitosamente ✔️"
                    : "EduStay - Atención: Documento Rechazado ⚠️";
            body.put("subject", subject);

            String colorBg = aprobado ? "#ecfdf5" : "#fef2f2";
            String colorTexto = aprobado ? "#065f46" : "#991b1b";
            String estadoLabel = aprobado ? "APROBADO (VERIFICADO)" : "RECHAZADO";

            String obsRow = "";
            if (comentarioAdmin != null && !comentarioAdmin.isBlank()) {
                obsRow = "      <tr><td style=\"padding: 6px 0; vertical-align: top;\"><strong>Observación:</strong></td>" +
                         "<td style=\"color: #ef4444; font-style: italic;\">" + comentarioAdmin + "</td></tr>";
            }

            String globalSection = "";
            if ("VERIFICADO".equalsIgnoreCase(estadoGlobalUsuario)) {
                globalSection = "  <div style=\"background-color: #ecfdf5; border: 1px solid #a7f3d0; padding: 15px; border-radius: 8px; margin: 20px 0; color: #065f46; text-align: center;\">" +
                        "    <strong>¡Felicidades! 🎉 Tu perfil ha sido completamente verificado.</strong>" +
                        "    <p style=\"margin: 5px 0 0; font-size: 14px;\">Ya puedes publicar habitaciones o realizar alquileres dentro de la plataforma sin restricciones.</p>" +
                        "  </div>";
            } else if ("RECHAZADO".equalsIgnoreCase(estadoGlobalUsuario)) {
                globalSection = "  <div style=\"background-color: #fef2f2; border: 1px solid #fecaca; padding: 15px; border-radius: 8px; margin: 20px 0; color: #991b1b;\">" +
                        "    <strong>Atención ⚠️</strong>" +
                        "    <p style=\"margin: 5px 0 0; font-size: 14px;\">Por favor, ingresa a tu perfil en EduStay, corrige el documento rechazado según la observación indicada y vuelve a subirlo para reanudar tu proceso de verificación.</p>" +
                        "  </div>";
            } else {
                globalSection = "  <div style=\"background-color: #eff6ff; border: 1px solid #bfdbfe; padding: 15px; border-radius: 8px; margin: 20px 0; color: #1e40af;\">" +
                        "    <strong>Proceso en Curso ℹ️</strong>" +
                        "    <p style=\"margin: 5px 0 0; font-size: 14px;\">Aún quedan otros documentos pendientes por verificar antes de habilitar por completo tu cuenta.</p>" +
                        "  </div>";
            }

            String htmlContent = "<div style=\"font-family: sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);\">" +
                    "  <h2 style=\"color: #1e3a8a; margin-top: 0;\">EduStay - Verificación de Identidad</h2>" +
                    "  <p style=\"font-size: 16px; color: #334155;\">¡Hola, " + nombreUsuario + "!</p>" +
                    "  <p style=\"font-size: 15px; color: #334155; line-height: 1.5;\">Queremos informarte que un administrador ha revisado tu documento de verificación con el siguiente resultado:</p>" +
                    "  " +
                    "  <div style=\"background-color: #f8fafc; border: 1px solid #e2e8f0; padding: 15px; border-radius: 8px; margin: 20px 0;\">" +
                    "    <table style=\"width: 100%; font-size: 14px; border-collapse: collapse; color: #475569;\">" +
                    "      <tr><td style=\"padding: 6px 0; width: 150px;\"><strong>Documento:</strong></td><td>" + tipoDocumento + "</td></tr>" +
                    "      <tr><td style=\"padding: 6px 0;\"><strong>Resultado:</strong></td><td>" +
                    "        <span style=\"background-color: " + colorBg + "; color: " + colorTexto + "; padding: 4px 10px; border-radius: 999px; font-weight: bold; font-size: 12px;\">" +
                    "          " + estadoLabel + "" +
                    "        </span>" +
                    "      </td></tr>" +
                    "      " + obsRow + "" +
                    "      <tr><td style=\"padding: 6px 0;\"><strong>Estado de Cuenta:</strong></td><td><strong style=\"color: #0f172a;\">" + estadoGlobalUsuario + "</strong></td></tr>" +
                    "    </table>" +
                    "  </div>" +
                    "  " +
                    "  " + globalSection + "" +
                    "  " +
                    "  <div style=\"text-align: center; margin: 25px 0;\">" +
                    "    <a href=\"http://localhost:4200/perfil\" target=\"_blank\" style=\"background-color: #1e3a8a; color: white; padding: 12px 24px; border-radius: 8px; text-decoration: none; font-weight: bold; display: inline-block;\">Ir a Mi Perfil en EduStay</a>" +
                    "  </div>" +
                    "  " +
                    "  <hr style=\"border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;\" />" +
                    "  <p style=\"font-size: 12px; color: #94a3b8; text-align: center;\">El equipo de EduStay</p>" +
                    "</div>";

            body.put("html", htmlContent);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, requestEntity, String.class);
            System.out.println("✅ Correo de verificación enviado exitosamente a: " + emailUsuario);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo de verificación: " + e.getMessage());
        }
    }

    @Override
    public void enviarEnlaceRestablecimiento(String destinatario, String nombreUsuario, String enlace) {
        // Log de respaldo en consola
        System.out.println("\n==================================================");
        System.out.println("✉️ [EMAIL SENDER] Enviando enlace de restablecimiento a: " + destinatario);
        System.out.println("🔗 Enlace: " + enlace);
        System.out.println("==================================================\n");

        if (apiKey == null || apiKey.isBlank() || apiKey.equals("re_default_key")) {
            System.out.println("⚠️ Resend API Key no configurada. El enlace solo estará disponible en consola.");
            return;
        }

        try {
            String url = "https://api.resend.com/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("from", "EduStay <" + fromEmail + ">");
            body.put("to", Collections.singletonList(destinatario));
            body.put("subject", "EduStay - Restablecer tu contraseña");

            String htmlContent = "<div style=\"font-family: sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e2e8f0; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);\">" +
                    "  <h2 style=\"color: #1e3a8a; margin-top: 0;\">EduStay</h2>" +
                    "  <p style=\"font-size: 16px; color: #334155;\">¡Hola, " + nombreUsuario + "!</p>" +
                    "  <p style=\"font-size: 15px; color: #334155; line-height: 1.5;\">Recibimos una solicitud para restablecer la contraseña de tu cuenta en EduStay. Para continuar, haz clic en el siguiente enlace:</p>" +
                    "  " +
                    "  <div style=\"text-align: center; margin: 30px 0;\">" +
                    "    <a href=\"" + enlace + "\" target=\"_blank\" style=\"background-color: #2563eb; color: white; padding: 12px 24px; border-radius: 8px; text-decoration: none; font-weight: bold; display: inline-block;\">Restablecer contraseña</a>" +
                    "  </div>" +
                    "  " +
                    "  <p style=\"font-size: 14px; color: #64748b;\">Este enlace expirará en 15 minutos y solo puede ser usado una vez.</p>" +
                    "  <p style=\"font-size: 14px; color: #64748b;\">Si no solicitaste este cambio, puedes ignorar este correo de forma segura. Tu contraseña no cambiará.</p>" +
                    "  <hr style=\"border: 0; border-top: 1px solid #e2e8f0; margin: 20px 0;\" />" +
                    "  <p style=\"font-size: 12px; color: #94a3b8; text-align: center;\">Este es un correo automático, por favor no respondas a este mensaje.</p>" +
                    "</div>";

            body.put("html", htmlContent);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, requestEntity, String.class);
            System.out.println("✅ Correo de restablecimiento de contraseña enviado exitosamente a: " + destinatario);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo de restablecimiento: " + e.getMessage());
        }
    }
}

package com.edustay.backend.services.impl;

import com.edustay.backend.dto.ResultadoModeracion;
import com.edustay.backend.services.ModeracionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ModeracionServiceImpl implements ModeracionService {

    @Value("${app.openai.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // Patrones regex para el motor local de fallback
    private static final Pattern PATTERN_EMAIL = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
    // Detecta números telefónicos de 7 a 11 dígitos, con guiones o espacios opcionales
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("(\\+?\\d{1,3}[- .]?)?\\d{3,4}[- .]?\\d{3,4}");

    // Lista de palabras ofensivas comunes en español (moderación blanda)
    private static final String[] PALABRAS_OFENSIVAS = {
            "mierda", "puto", "puta", "hijo de puta", "hijo de perra", "imbecil", "imbécil",
            "estupido", "estúpido", "idiota", "maldito", "maldita", "basura", "estafador",
            "estafa", "cbron", "cabron", "cabrón", "mierdas"
    };

    // Lista de frases de amenazas/violencia extrema (moderación dura - bloqueo automático)
    private static final String[] FRASES_VIOLENTAS = {
            "te voy a matar", "te voy a buscar", "muere", "muérete", "te busco y te",
            "te voy a golpear", "amenaza", "violencia", "violador", "violar", "asesinar"
    };

    @Override
    @SuppressWarnings("unchecked")
    public ResultadoModeracion moderarContenido(String contenido) {
        if (contenido == null || contenido.isBlank()) {
            return new ResultadoModeracion(false, false, null);
        }

        // 1. Si hay una API Key de OpenAI configurada, intentar la moderación por IA
        if (apiKey != null && !apiKey.isBlank() && !apiKey.equals("openai_default_key")) {
            try {
                String url = "https://api.openai.com/v1/moderations";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + apiKey);

                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("input", contenido);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                
                Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

                if (response != null && response.containsKey("results")) {
                    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                    if (results != null && !results.isEmpty()) {
                        Map<String, Object> result = results.get(0);
                        boolean flagged = (Boolean) result.getOrDefault("flagged", false);

                        if (flagged) {
                            Map<String, Boolean> categories = (Map<String, Boolean>) result.get("categories");
                            String categoriaDetectada = "inapropiado";
                            boolean blocked = false;

                            if (categories != null) {
                                // Determinar categoría principal
                                for (Map.Entry<String, Boolean> entry : categories.entrySet()) {
                                    if (entry.getValue()) {
                                        categoriaDetectada = entry.getKey();
                                        break;
                                    }
                                }
                                
                                // Bloqueo estricto para violencia o acoso/amenazas graves
                                if (categories.getOrDefault("violence", false) ||
                                    categories.getOrDefault("violence/graphic", false) ||
                                    categories.getOrDefault("harassment/threatening", false)) {
                                    blocked = true;
                                }
                            }

                            System.out.println("🤖 [IA MODERATOR] Flagged message! Category: " + categoriaDetectada + ", Blocked: " + blocked);
                            return new ResultadoModeracion(true, blocked, translateCategory(categoriaDetectada));
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error al comunicarse con OpenAI Moderation. Usando fallback local: " + e.getMessage());
            }
        }

        // 2. Fallback: Motor local por reglas (Regex + Palabras Clave)
        String textoMinuscula = contenido.toLowerCase();

        // Evaluar amenazas críticas (Duro - Bloqueo)
        for (String frase : FRASES_VIOLENTAS) {
            if (textoMinuscula.contains(frase)) {
                System.out.println("🛡️ [LOCAL MODERATOR] Bloqueo automático por contenido violento: " + frase);
                return new ResultadoModeracion(true, true, "Violencia / Amenazas");
            }
        }

        // Evaluar lenguaje inapropiado (Blando - Advertencia/Moderado)
        for (String palabra : PALABRAS_OFENSIVAS) {
            if (textoMinuscula.contains(palabra)) {
                System.out.println("🛡️ [LOCAL MODERATOR] Marcado por lenguaje inapropiado: " + palabra);
                return new ResultadoModeracion(true, false, "Lenguaje Inapropiado");
            }
        }

        // Evaluar intercambio de información de contacto (Blando - Advertencia/Moderado)
        if (PATTERN_EMAIL.matcher(contenido).find()) {
            System.out.println("🛡️ [LOCAL MODERATOR] Marcado por compartir dirección de correo.");
            return new ResultadoModeracion(true, false, "Contacto Externo (Email)");
        }

        if (PATTERN_TELEFONO.matcher(contenido).find()) {
            System.out.println("🛡️ [LOCAL MODERATOR] Marcado por compartir número telefónico.");
            return new ResultadoModeracion(true, false, "Contacto Externo (Teléfono)");
        }

        // Todo correcto
        return new ResultadoModeracion(false, false, null);
    }

    private String translateCategory(String category) {
        if (category == null) return "inapropiado";
        switch (category) {
            case "harassment": return "Acoso";
            case "harassment/threatening": return "Acoso / Amenaza";
            case "hate": return "Odio";
            case "hate/threatening": return "Odio / Amenaza";
            case "self-harm": return "Autolesión";
            case "self-harm/intent": return "Intención de Autolesión";
            case "self-harm/instructions": return "Instrucciones de Autolesión";
            case "sexual": return "Contenido Sexual";
            case "sexual/minors": return "Contenido Sexual con Menores";
            case "violence": return "Violencia";
            case "violence/graphic": return "Gráficos de Violencia";
            default: return "Contenido Inapropiado (" + category + ")";
        }
    }
}

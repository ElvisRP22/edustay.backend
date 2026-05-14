package com.edustay.backend.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller que sirve la documentación interactiva con Scalar
 */
@Controller
public class ScalarController {

    /**
     * Redirige /docs a la página de Scalar
     */
    @GetMapping("/docs")
    public String docs() {
        return "redirect:/scalar/index.html";
    }
}

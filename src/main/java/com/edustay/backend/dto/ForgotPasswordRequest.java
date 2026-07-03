package com.edustay.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe proporcionar una dirección de correo válida")
    private String email;

    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

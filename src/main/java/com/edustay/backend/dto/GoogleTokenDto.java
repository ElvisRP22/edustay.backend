package com.edustay.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para recibir el token ID de Google enviado por el frontend.
 */
public class GoogleTokenDto {

    @NotBlank(message = "El tokenId es requerido")
    private String tokenId;

    public GoogleTokenDto() {
    }

    public GoogleTokenDto(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
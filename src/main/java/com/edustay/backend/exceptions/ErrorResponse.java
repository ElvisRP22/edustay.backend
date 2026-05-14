package com.edustay.backend.exceptions;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * DTO para respuestas de error
 */
public class ErrorResponse {
    
    private int status;
    private String message;
    private String code;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String path;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String message, String code, String path) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package com.edustay.backend.dto;

public class ResultadoModeracion {
    private boolean flagged;
    private boolean blocked;
    private String categoria;

    public ResultadoModeracion() {}

    public ResultadoModeracion(boolean flagged, boolean blocked, String categoria) {
        this.flagged = flagged;
        this.blocked = blocked;
        this.categoria = categoria;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

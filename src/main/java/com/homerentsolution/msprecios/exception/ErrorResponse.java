package com.homerentsolution.msprecios.exception;
import java.time.LocalDateTime;

public class ErrorResponse {
    private String mensaje;
    private int status;
    private LocalDateTime fecha;

    public ErrorResponse() {
    }

    public ErrorResponse(
            String mensaje,
            int status,
            LocalDateTime fecha) {

        this.mensaje = mensaje;
        this.status = status;
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

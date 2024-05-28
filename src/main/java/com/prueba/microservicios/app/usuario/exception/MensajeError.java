package com.prueba.microservicios.app.usuario.exception;

import lombok.Builder;

@Builder
public class MensajeError {

    private String mensaje;

    private String excepcion;

    private String url;

    private String operacion;

    private int status;

    public MensajeError() {

    }

    public MensajeError(String mensaje, String excepcion, String url, String operacion, int status) {
        this.mensaje = mensaje;
        this.excepcion = excepcion;
        this.url = url;
        this.operacion = operacion;
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
}

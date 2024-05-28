package com.prueba.microservicios.app.usuario.exception;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException{
    private HttpStatus httpStatus;

    public GenericException(HttpStatus httpStatus, String mensaje) {
        super(mensaje);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}

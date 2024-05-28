package com.prueba.microservicios.app.usuario.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ManejadorException {

    private static final Map<String, Integer> STATUS = new HashMap<>();


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensajeErroresValidation> handleInvalidArgument(MethodArgumentNotValidException ex,HttpServletRequest request) {
        ResponseEntity<MensajeErroresValidation> resultado;


        /*Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            //System.out.println(error.getField()+":"+ error.getDefaultMessage());
            errorMap.put(error.getField()+":", error.getDefaultMessage());
        });*/
        List<FieldError> err= ex.getBindingResult().getFieldErrors();
        List<String> erroesPorField = new ArrayList<>();
        /*Map<String, List<String>> errroLista = new HashMap<>();
        for (int i = 0; i<err.size();i++){
            FieldError errorCampo = err.get(i);
            if(errroLista.get(errorCampo.getField())==null){
                erroesPorField = new ArrayList<>();
                erroesPorField.add(errorCampo.getDefaultMessage());
                errroLista.put(errorCampo.getField(),erroesPorField);
            }else {
                erroesPorField = errroLista.get(errorCampo.getField());
                erroesPorField.add(errorCampo.getDefaultMessage());
                errroLista.put(errorCampo.getField(),erroesPorField);
            }
        }*/

        Map<String, List<String>> errroLista2 = new HashMap<>();
        for (int i = 0; i<err.size();i++){
            FieldError errorCampo = err.get(i);
            String mensaje = errorCampo.getField()+ ": " + errorCampo.getDefaultMessage();
            erroesPorField.add(mensaje);

        }
        MensajeErroresValidation mensajeErroresValidation = MensajeErroresValidation.builder()
                .errores(erroesPorField)
                .excepcion(ex.getClass().getSimpleName())
                .url(request.getRequestURI())
                .operacion(request.getMethod().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        /*errorMap.put("excepcion",ex.getClass().getSimpleName());
        errorMap.put("url" , request.getRequestURI());
        errorMap.put("Operation" , request.getMethod());
        errorMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));*/

        resultado = new ResponseEntity(mensajeErroresValidation, HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()));
        return resultado;
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<MensajeError> AllExceptions(HttpServletRequest request, Exception exception) {
        ResponseEntity<MensajeError> resultado;

        String excepcion = exception.getClass().getSimpleName();
        String mensaje = exception.getMessage();
        Integer codigo = getStatus(exception);

        if (codigo == null) {
            codigo = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        MensajeError error = new MensajeError();
        error.setMensaje(mensaje);
        error.setExcepcion(excepcion);
        error.setUrl(request.getRequestURI());
        error.setStatus(codigo);
        error.setOperacion(request.getMethod());
        resultado = new ResponseEntity<>(error, HttpStatus.valueOf(codigo));
        exception.printStackTrace();
        return resultado;
    }

    private Integer getStatus(Exception e) {
        if (e instanceof GenericException) {
            GenericException ex = (GenericException) e;
            if (ex.getHttpStatus() != null) {
                return ex.getHttpStatus().value();
            }
        }
        return STATUS.get(e.getClass().getSimpleName());
    }



}

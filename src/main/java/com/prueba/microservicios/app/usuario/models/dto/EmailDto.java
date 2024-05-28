package com.prueba.microservicios.app.usuario.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    @Size(min = 6,max = 6,message = "La longitud de la placa solo permite 6 Caractes")
    @NotBlank(message = "Se debe ingresar una placa")
    private String plate;

    @Email(message = "El email ingresado es invalido")
    @NotEmpty
    private String email;

    private String mensaje;


}

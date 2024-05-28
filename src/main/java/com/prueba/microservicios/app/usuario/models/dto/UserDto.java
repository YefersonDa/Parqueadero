package com.prueba.microservicios.app.usuario.models.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String username;


    @Email(message = "El email ingresado es invalido")
    @NotEmpty

    private String email;

    @NotEmpty(message = "La password no debe estar vacia")
    private String password;


}

package com.prueba.microservicios.app.usuario.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoResponse {

    private Long id;

    private String username;

    private String email;

    private String password;


}

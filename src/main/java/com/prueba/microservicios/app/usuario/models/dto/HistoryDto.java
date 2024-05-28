package com.prueba.microservicios.app.usuario.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HistoryDto {

    @Size(min = 6,max = 6,message = "La longitud de la placa solo permite 6 Caractes")
    @NotBlank(message = "Se debe ingresar una placa")
    private String plate;
}

package com.prueba.microservicios.app.usuario.models.dto;

import com.prueba.microservicios.app.usuario.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDto {

    private Long id;

    @Size(min = 6,max = 6,message = "La longitud de la placa solo permite 6 Caractes")
    private String plate;

    @NotEmpty(message = "el campo marca no debe estar vacio")
    private String brand;

    @Size(min = 4,max = 4,message = "La longitud del model solo permite 4 Caractes")
    @NotEmpty(message = "el campo model no debe estar vacio")
    private String model;

    @NotEmpty(message = "el campo color no debe estar vacio")
    private String color;

    private Boolean isStored;
}

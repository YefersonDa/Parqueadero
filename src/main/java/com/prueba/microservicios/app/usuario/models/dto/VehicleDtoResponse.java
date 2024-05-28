package com.prueba.microservicios.app.usuario.models.dto;

import com.prueba.microservicios.app.usuario.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDtoResponse {

    private Long id;

    private String plate;

    private LocalDate createAt;

    private String brand;

    private String model;

    private String color;

    private Boolean isStored;

    private Boolean firstTime;

}

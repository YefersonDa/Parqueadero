package com.prueba.microservicios.app.usuario.models.dto;


import com.prueba.microservicios.app.usuario.models.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDtoCantidad {

    private Vehicle vehicle;

    private Long countRegistry;
}

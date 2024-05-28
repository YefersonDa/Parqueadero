package com.prueba.microservicios.app.usuario.models.mapper;

import com.prueba.microservicios.app.usuario.models.dto.VehicleDto;
import com.prueba.microservicios.app.usuario.models.dto.VehicleDtoResponse;
import com.prueba.microservicios.app.usuario.models.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle toVehicle(VehicleDto vehicleDto){
        return Vehicle.builder()
                .plate(vehicleDto.getPlate())
                .brand(vehicleDto.getBrand())
                .color(vehicleDto.getColor())
                .model(vehicleDto.getModel())
                .isStored(Boolean.TRUE)
                .build();
    }

    public VehicleDtoResponse toVehicleDtoResponse(Vehicle vehicle){
        return VehicleDtoResponse.builder()
                .id(vehicle.getId())
                .plate(vehicle.getPlate())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .color(vehicle.getColor())
                .createAt(vehicle.getCreateAt())
                .isStored(vehicle.getIsStored())
                .firstTime(vehicle.getFirstTime())
                .build();
    }

    public Vehicle toVehicleofDtoResponse(VehicleDtoResponse vehicleDtoResponse){
        return Vehicle.builder()
                .id(vehicleDtoResponse.getId())
                .plate(vehicleDtoResponse.getPlate())
                .brand(vehicleDtoResponse.getBrand())
                .model(vehicleDtoResponse.getModel())
                .color(vehicleDtoResponse.getColor())
                .createAt(vehicleDtoResponse.getCreateAt())
                .isStored(vehicleDtoResponse.getIsStored())
                .firstTime(vehicleDtoResponse.getFirstTime())
                .build();
    }
}

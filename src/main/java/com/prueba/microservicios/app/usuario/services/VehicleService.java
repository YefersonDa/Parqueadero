package com.prueba.microservicios.app.usuario.services;


import com.prueba.microservicios.app.usuario.models.dto.*;
import com.prueba.microservicios.app.usuario.models.entity.History;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface VehicleService {

    List<VehicleDtoResponse> findAll();
    VehicleDtoResponse save(VehicleDto vehicleDto);
    VehicleDtoResponse findById(Long id);
    void deleteById(Long id);
    VehicleDtoResponse update(Long id, VehicleDto vehicleDto);
    List<VehicleDtoResponse> findAllStatus();
    VehicleDtoResponse findByPlate(String plate);
    void updateStatusVehicle(Long id,LocalDate fecha);
    void updateFirsTimeVehicle(Long id);
    EmailDtoResponse sendEmail(EmailDto emailDto);
    List<VehicleDtoCantidad> getTotalVehicleUser();

}

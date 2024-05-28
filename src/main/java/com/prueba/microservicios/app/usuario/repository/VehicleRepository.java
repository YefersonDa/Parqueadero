package com.prueba.microservicios.app.usuario.repository;

import com.prueba.microservicios.app.usuario.models.entity.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends CrudRepository<Vehicle,Long> {

    Optional<Vehicle> findByPlate(String plate);
    Long countByIsStored(Boolean status);
    List<Vehicle> findByIsStored(Boolean status);

}

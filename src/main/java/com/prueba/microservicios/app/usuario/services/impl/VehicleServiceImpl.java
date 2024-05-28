package com.prueba.microservicios.app.usuario.services.impl;

import com.prueba.microservicios.app.usuario.exception.GenericException;
import com.prueba.microservicios.app.usuario.models.dto.*;
import com.prueba.microservicios.app.usuario.models.entity.History;
import com.prueba.microservicios.app.usuario.models.entity.Vehicle;
import com.prueba.microservicios.app.usuario.models.mapper.VehicleMapper;
import com.prueba.microservicios.app.usuario.repository.HistoryRepository;
import com.prueba.microservicios.app.usuario.repository.VehicleRepository;
import com.prueba.microservicios.app.usuario.services.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository repository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public List<VehicleDtoResponse> findAll() {
        try {
            List<Vehicle> vehicles= (List<Vehicle>) repository.findAll();
            log.info("VehicleService: Se obtienen todos los vehiculos");
            return vehicles.stream()
                    .map(vehicle -> vehicleMapper.toVehicleDtoResponse(vehicle))
                    .collect(Collectors.toList());
        }catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno obteniendo todos los vehiculos");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }



    @Override
    public VehicleDtoResponse save(VehicleDto vehicleDto) {
        if(vehicleStored()>=5){
            log.error("VehicleService:Error al registrar un vehiculo,el parqueadero esta a maxima capacidad");
            throw new GenericException(HttpStatus.BAD_REQUEST,"El parqueado se encuentra por el momento a maxima capacidad de vehiculos permitidos");
        }
        try {
            if(this.foundPlate(vehicleDto.getPlate())){
                VehicleDtoResponse vehicleDtoResponse=findByPlate(vehicleDto.getPlate());
                if(vehicleDtoResponse!=null && vehicleDtoResponse.getIsStored()){
                    log.error("VehicleService:Error al registar un vehiculo,ya existe la placa");
                    throw new GenericException(HttpStatus.BAD_REQUEST,"ya existe la placa");

                }else{
                    log.info("Se guardo el vehiculo en la base de datos");
                    vehicleDtoResponse.setFirstTime(historyRepository.countByPlate(vehicleDto.getPlate())>=1?Boolean.FALSE:Boolean.TRUE);
                    this.updateStatusVehicle(vehicleDtoResponse.getId(),LocalDate.now());
                    this.updateFirsTimeVehicle(vehicleDtoResponse.getId());
                    return findByPlate(vehicleDto.getPlate());
                }
            }else{

                Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto);
                System.out.println(historyRepository.countByPlate(vehicleDto.getPlate()));
                vehicle.setFirstTime(historyRepository.countByPlate(vehicleDto.getPlate())>=1?Boolean.FALSE:Boolean.TRUE);
                vehicle.setCreateAt(LocalDate.now());
                vehicle = repository.save(vehicle);
                return vehicleMapper.toVehicleDtoResponse(vehicle);
            }
        }catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno al registar un vehiculo");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public VehicleDtoResponse findById(Long id) {
        try{
            Vehicle vehicle= repository.findById(id).orElseThrow(()->new GenericException(HttpStatus.NOT_FOUND,"No se encontro el vehiculo"));
            log.info("VehicleService: Buscar vehiculo por id", id);
            return vehicleMapper.toVehicleDtoResponse(vehicle);
        }catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno al Buscar vehiculo por id", id);
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        try {
            log.info("VehicleService: Borrar vehiculo por id", id);
            repository.deleteById(id);
        }catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno al  Borrar vehiculo por id", id);
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public VehicleDtoResponse update(Long id, VehicleDto vehicleDto) {
        VehicleDtoResponse vehicleDtoResponse=findById(id);
        try {
            if(!vehicleDto.getPlate().equals(vehicleDtoResponse.getPlate())){
                foundPlate(vehicleDto.getPlate());
            }
            log.info("VehicleService: Se actualizo la informacion del vehiculo");
            return vehicleMapper.toVehicleDtoResponse(
                    repository.save(Vehicle.builder()
                            .id(vehicleDtoResponse.getId())
                            .plate(vehicleDto.getPlate())
                            .brand(vehicleDto.getBrand())
                            .model(vehicleDto.getModel())
                            .color(vehicleDto.getColor())
                            .createAt(vehicleDtoResponse.getCreateAt())
                            .isStored(vehicleDtoResponse.getIsStored())
                            .build()));
        }catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno al actualizar la informacion del vehiculo");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public void updateStatusVehicle(Long id,LocalDate fecha){
        VehicleDtoResponse vehicleDtoResponse = findById(id);
        try {
            log.info("VehicleService: Se actualizo el estado del vehiculo");
            Vehicle vehicle = vehicleMapper.toVehicleofDtoResponse(vehicleDtoResponse);
            if(vehicle.getIsStored()){
                vehicle.setIsStored(Boolean.FALSE);
            }else{
                vehicle.setIsStored(Boolean.TRUE);
            }
            repository.save(vehicle);
        }catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno no se actualizo el estado del vehiculo");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public void updateFirsTimeVehicle(Long id){
        VehicleDtoResponse vehicleDtoResponse = findById(id);
        try {
            Vehicle vehicle = vehicleMapper.toVehicleofDtoResponse(vehicleDtoResponse);
            vehicle.setFirstTime(Boolean.FALSE);
            log.info("VehicleService: Se verifico si el carro entro por primera vez");
            repository.save(vehicle);
        }catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno no se verifico si el carro entro por primera vez");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public EmailDtoResponse sendEmail(EmailDto emailDto) {
        try {
            if(repository.findByPlate(emailDto.getPlate()).orElse(null)==null){
                throw new GenericException(HttpStatus.BAD_REQUEST,"No se encontro la placa");
            }
            log.info("VehicleService:Se envio email");
            ResponseEntity<String> emailResponse = restTemplate.postForEntity("http://localhost:8082/api/email",emailDto,String.class);
            return EmailDtoResponse.builder().mensaje("Correo Enviado").build();
        }catch (HttpServerErrorException e){
            log.info("VehicleService:Error interno no se envio email");
            throw new GenericException(HttpStatus.BAD_GATEWAY,"Ha ocurrido un error inesperado");
        }catch (HttpClientErrorException e){
            log.info("VehicleService:Error no se envio email, la placa no se encontro");
            throw new GenericException(HttpStatus.NOT_FOUND,"No se encontro la placa");
        }

    }

    @Override
    public List<VehicleDtoResponse> findAllStatus() {
        try {
            List<Vehicle> vehicles =repository.findByIsStored(true);
            log.info("VehicleService:retornar todo por status");
            return vehicles.stream()
                    .map(vehicle -> vehicleMapper.toVehicleDtoResponse(vehicle))
                    .collect(Collectors.toList());
        }catch (HttpServerErrorException e){
            log.error("VehicleService:no se ídp retornar todo por status");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public VehicleDtoResponse findByPlate(String plate) {
        try {
            Vehicle vehicle = repository.findByPlate(plate).orElseThrow(()->new GenericException(HttpStatus.BAD_REQUEST,"No existe la placa"));
            log.info("VehicleService:obtener registro por placa de vehiculos");
            return vehicleMapper.toVehicleDtoResponse(vehicle);
        }catch (HttpClientErrorException e){
            log.error("VehicleService:Error al obtener registro por placa de vehiculos, no existe la placa");

            throw new GenericException(HttpStatus.BAD_REQUEST,"No se puede enviar email no existe la placa");
        }
        catch (HttpServerErrorException e){
            log.error("VehicleService:Error interno no se pudo obtener registro por placa de vehiculos");

            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    public boolean foundPlate(String plate){
        try {
            Vehicle vehicle= repository.findByPlate(plate).orElse(null);
            if(vehicle==null){
                return false;
            }else {
                return true;
            }
        }catch (HttpServerErrorException e){
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    public Long vehicleStored(){
        return repository.countByIsStored(true);
    }

    @Override
    public List<VehicleDtoCantidad> getTotalVehicleUser(){
        List<Map<String, Object>> cantidades = historyRepository.countRegistros();
        List<VehicleDtoCantidad> vehiculos = new ArrayList<>();
        for (int i = 0; i<cantidades.size();i++){
            Map<String ,Object> c= cantidades.get(i);
            c.forEach((k,v) -> System.out.println("Key: " + k + ": Value: " + v));
            String or = c.get("countregistry").toString();
            String pl = c.get("plate").toString();
            System.out.println(pl);
            Vehicle vehicleFind = repository.findByPlate(pl).orElse(null);
            System.out.println(vehicleFind.toString());
            Long cantida = Long.parseLong(or);
            vehiculos.add(VehicleDtoCantidad.builder()
                    .vehicle(repository.findByPlate(c.get("plate").toString()).orElse(null))
                    .countRegistry(cantida).build());
        }
        log.info("VehicleService:obtener vehiculos que mas han utilizado el servicio");
        return vehiculos;
    }
}

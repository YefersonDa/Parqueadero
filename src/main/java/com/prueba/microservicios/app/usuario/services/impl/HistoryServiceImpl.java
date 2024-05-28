package com.prueba.microservicios.app.usuario.services.impl;

import com.prueba.microservicios.app.usuario.exception.GenericException;
import com.prueba.microservicios.app.usuario.models.dto.HistoryDto;
import com.prueba.microservicios.app.usuario.models.dto.HistoryDtoResponse;
import com.prueba.microservicios.app.usuario.models.dto.VehicleDtoCantidad;
import com.prueba.microservicios.app.usuario.models.dto.VehicleDtoResponse;
import com.prueba.microservicios.app.usuario.models.entity.History;
import com.prueba.microservicios.app.usuario.models.mapper.HistoryMapper;
import com.prueba.microservicios.app.usuario.repository.HistoryRepository;
import com.prueba.microservicios.app.usuario.services.HistoryService;
import com.prueba.microservicios.app.usuario.services.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private HistoryRepository repository;

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public HistoryDtoResponse save(HistoryDto historyDto) {
        try {
            VehicleDtoResponse vehicleDtoResponse = vehicleService.findByPlate(historyDto.getPlate());
            if(vehicleDtoResponse.getIsStored()){
                vehicleService.updateStatusVehicle(vehicleDtoResponse.getId(),null);
                log.info("HistoryService: Guardando una salidad del vehiculo la base de datos",historyDto.getPlate());
                History history = historyMapper.toHistory(historyDto);
                history.setInDate(vehicleDtoResponse.getCreateAt());
                //vehicleService.deleteById(vehicleDtoResponse.getId());
                return  historyMapper.toHistoryDtoResponse(repository.save(history));
            }
            log.error("HistoryService:Error Guardando una salidad del vehiculo la base de datos, ya existe la placa",historyDto.getPlate());
            throw new GenericException(HttpStatus.BAD_REQUEST,"No se puede Registrar Salida, no existe la placa");
        }catch (HttpServerErrorException e){
            log.error("HistoryService:Error Interno Guardando una salidad del vehiculo la base de datos",historyDto.getPlate());
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public Long countPlate(String plate) {
        try {
            log.info("HistoryService: Filtrando por el top de vehiculos que han utilizado el servicio");
            return repository.countByPlate(plate);
        }catch (HttpServerErrorException e){
            log.error("HistoryService:Error Interno Filtrando por el top de vehiculos que han utilizado el servicio");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public List<History> findLikeByPlate(String plate) {
        try {
            List<History> histories = repository.findByPlateContaining(plate);
            if(histories.isEmpty()){
                log.error("HistoryService:Error Filtrando por la placa vehiculos, No se encontraron coincidencias");
                throw new GenericException(HttpStatus.BAD_REQUEST,"No se encontraron coincidencias con la placa ingresada");
            }
            log.info("HistoryService: Filtrando por la placa vehiculos");
            return histories;
        }catch (HttpServerErrorException e){
            log.error("HistoryService:Error interno Filtrando por la placa vehiculos");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }

    @Override
    public List<History> findFechaBetween(LocalDate fechaInicio, LocalDate fechaDesde) {
        try {
            log.info("HistoryService: Filtrando por fecha de salida de vehiculos");
            return repository.findByExitDateBetween(fechaInicio,fechaDesde);
        }catch (HttpServerErrorException e){
            log.error("HistoryService:Error interno Filtrando por fecha de salida de vehiculos");
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR,"Ha ocurrido un error inesperado");
        }
    }
}

package com.prueba.microservicios.app.usuario.services;

import com.prueba.microservicios.app.usuario.models.dto.HistoryDto;
import com.prueba.microservicios.app.usuario.models.dto.HistoryDtoResponse;
import com.prueba.microservicios.app.usuario.models.entity.History;

import java.time.LocalDate;
import java.util.List;

public interface HistoryService {

    HistoryDtoResponse save(HistoryDto historyDto);
    Long countPlate(String plate);
    List<History> findLikeByPlate(String plate);
    List<History> findFechaBetween(LocalDate fechaInicio,LocalDate fechaDesde);

}

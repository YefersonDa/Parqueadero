package com.prueba.microservicios.app.usuario.models.mapper;

import com.prueba.microservicios.app.usuario.models.dto.HistoryDto;
import com.prueba.microservicios.app.usuario.models.dto.HistoryDtoResponse;
import com.prueba.microservicios.app.usuario.models.entity.History;
import org.springframework.stereotype.Component;

@Component
public class HistoryMapper {

    public History toHistory(HistoryDto historyDto){
        return History.builder()
                .plate(historyDto.getPlate())
                .build();
    }



    public HistoryDtoResponse toHistoryDtoResponse(History history){
        return HistoryDtoResponse.builder()
                .mensaje("Salida registrada")
                .build();
    }
}

package com.prueba.microservicios.app.usuario.repository;

import com.prueba.microservicios.app.usuario.models.dto.VehicleDtoCantidad;
import com.prueba.microservicios.app.usuario.models.entity.History;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HistoryRepository extends CrudRepository<History,Long> {

    @Query(value = "select h.plate, count(*) As countRegistry from history h group by h.plate LIMIT 10",nativeQuery = true)
    List<Map<String, Object>> countRegistros();
    Long countByPlate(String plate);

    //@Query(value = "select * from history h where h.plate LIKE %:rex%")
    List<History> findByPlateContaining(String rex);

    List<History> findByExitDateBetween(LocalDate fechaDesde,LocalDate fechaHasta);
}

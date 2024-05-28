package com.prueba.microservicios.app.usuario.controllers;

import com.prueba.microservicios.app.usuario.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryService service;

    @Secured("ROLE_ADMIN")
    @GetMapping("/{plate}")
    public ResponseEntity<?> getFiltro(@PathVariable("plate") String plate){
        return ResponseEntity.ok().body(service.findLikeByPlate(plate));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/date")
    public ResponseEntity<?> FindbetweenDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaHasta){
        return ResponseEntity.ok().body(service.findFechaBetween(fechaInicio,fechaHasta));
    }

}

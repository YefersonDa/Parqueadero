package com.prueba.microservicios.app.usuario.controllers;

import com.prueba.microservicios.app.usuario.models.dto.EmailDto;
import com.prueba.microservicios.app.usuario.models.dto.HistoryDto;
import com.prueba.microservicios.app.usuario.models.dto.VehicleDto;
import com.prueba.microservicios.app.usuario.services.HistoryService;
import com.prueba.microservicios.app.usuario.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private HistoryService historyService;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping({"/",""})
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(vehicleService.findAll());
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping({"/",""})
    public ResponseEntity<?> save(@Valid @RequestBody VehicleDto vehicleDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.save(vehicleDto));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/list")
    public ResponseEntity<?> getAllStatus(){
        return ResponseEntity.ok().body(vehicleService.findAllStatus());
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable("id") Long id, @RequestBody VehicleDto vehicleDto){
        return ResponseEntity.ok(vehicleService.update(id,vehicleDto));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/exit")
    public ResponseEntity<?> saveExitVehicle(@Valid @RequestBody HistoryDto historyDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.save(historyDto));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailDto emailDto){
        return ResponseEntity.ok(vehicleService.sendEmail(emailDto));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/plate/{plate}")
    public ResponseEntity<?> findByPlate(@PathVariable("plate") String plate){
        return ResponseEntity.ok(vehicleService.findByPlate(plate));
    }

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/top")
    public ResponseEntity<?> most(){
        return ResponseEntity.ok().body(vehicleService.getTotalVehicleUser());
    }
}

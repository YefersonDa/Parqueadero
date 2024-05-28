package com.prueba.microservicios.app.usuario.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;

    @Column(name = "create_at")
    //@JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate createAt;

    private String brand;

    private String model;

    private String color;

    @Column(name = "is_stored")
    private Boolean isStored;

    @Column(name = "first_time")
    private Boolean firstTime;


}

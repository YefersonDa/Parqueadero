package com.prueba.microservicios.app.usuario.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;

    private LocalDate inDate;

    private LocalDate exitDate;

    @PrePersist
    public void prePersistExitDate(){
        LocalDate date = LocalDate.now();
        int r = new Random().nextInt(3+1)+1;
        System.out.println("r:"+r);
        int b  =new Random().nextInt(5+1)+1;
        if (r==1){
            System.out.println("entro a 1");
            this.exitDate = date.minusDays(b);
        }
        if(r==2){
            System.out.println("entro a 2");
            this.exitDate = date;
        }
        if(r>=3){
            System.out.println("entro a 3");
            this.exitDate = date.plusDays(b);
        }
    }
}

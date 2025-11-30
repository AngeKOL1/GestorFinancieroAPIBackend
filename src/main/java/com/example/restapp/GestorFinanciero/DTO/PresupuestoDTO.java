package com.example.restapp.GestorFinanciero.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PresupuestoDTO {

    private Float montoEstablecido;  
    private Float montoMinimo;     
    private Float montoMaximo;     

    private String periodo;           

    private LocalDate fechaInicial;   
    private LocalDate fechaFinal;      

    private String nombreEstadoPresupuesto;           
}

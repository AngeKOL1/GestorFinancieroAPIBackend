package com.example.restapp.GestorFinanciero.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EditarMetaDTO {

    private String nombre;           
    private LocalDate fechaFinal;    
    private Double montoObjetivo;    
}


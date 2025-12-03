package com.example.restapp.GestorFinanciero.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TrofeosDTO{
    private String nombre; 
    private String prerequisito;
    private Integer xp;
    private LocalDate fecha;
}
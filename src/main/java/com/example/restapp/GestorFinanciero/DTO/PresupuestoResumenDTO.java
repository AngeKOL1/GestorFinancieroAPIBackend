package com.example.restapp.GestorFinanciero.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PresupuestoResumenDTO {
    private Integer idPresupuesto;
    private Float montoEstablecido;
    private Float montoActual;
    private Float montoMinimo;
    private Float montoMaximo;
    private String periodo;
    private LocalDate fechaInicial;
    private LocalDate fechaFinal;
    private String estado;
}

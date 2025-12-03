package com.example.restapp.GestorFinanciero.dto;

import lombok.Data;

@Data
public class TrofeoEstadoDTO {
    private Integer idTrofeo;
    private String nombreTrofeo;
    private String prerequisito;
    private Integer xpRequerida;
    private boolean obtenido;
}

package com.example.restapp.GestorFinanciero.dto;

import lombok.Data;

@Data
public class NivelUsuarioInfoDTO {
    private Integer nivelActual;
    private Integer xpActual;
    private Integer xpNecesaria;
    private Integer xpRestante;
    private Integer porcentaje;
    private String banner;
}

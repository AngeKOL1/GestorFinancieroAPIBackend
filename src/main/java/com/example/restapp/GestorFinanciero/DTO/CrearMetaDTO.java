package com.example.restapp.GestorFinanciero.dto;


import lombok.Data;


import java.time.LocalDate;


@Data
public class CrearMetaDTO {

    private String nombre;
    private Double montoObjetivo;
    private LocalDate fechaFinal;

    private Integer idUsuario;

    private String nombreCategoria;        
    private String nombreMisCategoria;    
    private String nombreTipoMeta;        
    private String nombreEstadoMeta;      
}


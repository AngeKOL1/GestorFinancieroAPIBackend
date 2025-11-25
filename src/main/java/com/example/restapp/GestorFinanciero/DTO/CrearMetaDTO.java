package com.example.restapp.GestorFinanciero.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearMetaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El monto objetivo es obligatorio")
    private Float montoObjetivo;

    @NotNull(message = "La fecha final es obligatoria")
    private LocalDate fechaFinal;


    private Integer idCategoria;
    private Integer idMisCategoria;


    @NotNull(message = "El id de usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El id de tipo de meta es obligatorio")
    private Integer idMeta;

    @NotNull(message = "El id del estado de meta es obligatorio")
    private Integer idEstadoMeta;
}

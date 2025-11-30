package com.example.restapp.GestorFinanciero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransaccionDTO {
    @NotNull(message = "El monto es obligatorio")
    private Float monto;
    @NotBlank(message = "Descripcion es obligatoria")
    private String descripcion;
    @NotNull(message = "El id de usuario es obligatorio")
    private Integer idUsuario;

    private Integer idMeta;
    @NotNull(message = "El id de tipo de transaccion es obligatorio")
    private Integer tipoTransaccionId;

    private Integer presupuestoId;

}

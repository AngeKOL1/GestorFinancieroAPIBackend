package com.example.restapp.GestorFinanciero.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditarTransaccionDTO {
    @NotNull(message = "El monto es obligatorio para editar")
    private Float monto;
    @NotBlank(message = "Descripcion es obligatoria para editar")
    private String descripcion;
}
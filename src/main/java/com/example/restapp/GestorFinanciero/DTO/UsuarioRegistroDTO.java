package com.example.restapp.GestorFinanciero.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {

    @NotNull(message = "La constrasena es obligatoria")
    private String contrasena;
    @NotNull(message = "La confirmacion de la contrasena es obligatoria")   
    private String confirmPassword;
    @NotNull(message = "El correo es obligatorio")
    private String correo;
    @NotNull(message = "El nombre es obligatorio")
    private String nombre;
    @NotNull(message = "El apellido es obligatorio")
    private String apellido;
}

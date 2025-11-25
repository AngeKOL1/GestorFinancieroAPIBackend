package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.DTO.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.models.Usuario;

public interface IUsuarioService extends IGenericService<Usuario, Integer>{
    Usuario registrarUsuario(UsuarioRegistroDTO dto);
    
}

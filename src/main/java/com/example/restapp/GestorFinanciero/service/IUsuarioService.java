package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.models.Usuario;

public interface IUsuarioService extends IGenericService<Usuario, Integer>{
    Usuario registrarUsuario(UsuarioRegistroDTO dto);
    Usuario asignarNiveles(Integer idUsuario);
    Integer obtenerXPUsuario(Integer idUsuario);
    void asignarTrofeo (Usuario user, Integer idTrofeo);
    boolean verificarMetasEnCategoriasDiferentes(Integer idUsuario);
    boolean usuarioTieneTrofeo(Usuario user, Integer idTrofeo);
}

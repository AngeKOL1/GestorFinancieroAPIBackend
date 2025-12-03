package com.example.restapp.GestorFinanciero.repo;

import java.util.Optional;

import com.example.restapp.GestorFinanciero.models.UsuarioTrofeo;

public interface UsuarioTrofeoRepo extends IGenericRepo<UsuarioTrofeo, Integer> {
    Optional<UsuarioTrofeo> findTopByUsuario_IdOrderByFechaObtencionTrofeoDesc(Integer idUsuario);
}

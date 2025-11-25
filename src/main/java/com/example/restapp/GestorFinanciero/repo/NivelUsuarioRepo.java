package com.example.restapp.GestorFinanciero.repo;

import com.example.restapp.GestorFinanciero.models.NivelUsuario;

import java.util.Optional;

public interface NivelUsuarioRepo extends IGenericRepo<NivelUsuario, Integer>{
    Optional<NivelUsuario> findFirstByOrderByIdNivelAsc();
}

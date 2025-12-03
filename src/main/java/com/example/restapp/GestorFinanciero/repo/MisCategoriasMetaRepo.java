package com.example.restapp.GestorFinanciero.repo;

import java.util.Optional;

import com.example.restapp.GestorFinanciero.models.MisCategoriasMetas;

public interface MisCategoriasMetaRepo extends IGenericRepo<MisCategoriasMetas,Integer>{
    Optional<MisCategoriasMetas> findByNombre(String nombre);
    boolean existsByNombreAndUsuario_Id(String nombre, Integer idUsuario);
    Optional<MisCategoriasMetas> findByNombreAndUsuario_Id(String nombre, Integer idUsuario);
}

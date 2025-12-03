package com.example.restapp.GestorFinanciero.repo;

import java.util.Optional;

import com.example.restapp.GestorFinanciero.models.TipoMeta;

public interface TipoMetaRepo extends IGenericRepo<TipoMeta,Integer>{
    Optional<TipoMeta> findByNombreTipoMeta(String nombre);
}

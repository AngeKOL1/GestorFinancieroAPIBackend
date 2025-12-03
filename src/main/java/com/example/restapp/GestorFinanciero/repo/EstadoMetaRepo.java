package com.example.restapp.GestorFinanciero.repo;

import java.util.Optional;

import com.example.restapp.GestorFinanciero.models.EstadoMeta;

public interface EstadoMetaRepo extends IGenericRepo<EstadoMeta,Integer>
{
    Optional<EstadoMeta> findByNombreEstadoMeta(String nombre);

}

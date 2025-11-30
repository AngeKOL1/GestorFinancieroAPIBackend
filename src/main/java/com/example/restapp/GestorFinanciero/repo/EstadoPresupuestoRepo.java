package com.example.restapp.GestorFinanciero.repo;

import java.util.Optional;

import com.example.restapp.GestorFinanciero.models.EstadoPresupuesto;

public interface EstadoPresupuestoRepo extends IGenericRepo<EstadoPresupuesto, Integer>{
    Optional<EstadoPresupuesto> findByNombreEstadoPresupuesto(String nombre);
    
}

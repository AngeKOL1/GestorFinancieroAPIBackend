package com.example.restapp.GestorFinanciero.repo;


import com.example.restapp.GestorFinanciero.models.Rol;

import java.util.Optional;

public interface RolRepo extends IGenericRepo<Rol, Integer>{
    Optional<Rol> findByNombre(String nombre);
}

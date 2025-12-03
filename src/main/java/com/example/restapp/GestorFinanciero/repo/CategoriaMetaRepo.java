package com.example.restapp.GestorFinanciero.repo;

import java.util.Optional;

import com.example.restapp.GestorFinanciero.models.CategoriaMeta;

public interface CategoriaMetaRepo extends IGenericRepo<CategoriaMeta, Integer> {
    Optional<CategoriaMeta> findByNombre(String nombre);
}

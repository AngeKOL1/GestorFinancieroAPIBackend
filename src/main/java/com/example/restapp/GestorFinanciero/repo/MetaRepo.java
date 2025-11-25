package com.example.restapp.GestorFinanciero.repo;

import java.util.List;

import com.example.restapp.GestorFinanciero.models.Meta;


public interface MetaRepo extends IGenericRepo<Meta,Integer>{
    List<Meta> findByUsuarioMetas_Id(Integer idUsuario);
}

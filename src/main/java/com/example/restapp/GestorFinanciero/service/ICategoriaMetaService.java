package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.CategoriaMetaDTO;
import com.example.restapp.GestorFinanciero.models.CategoriaMeta;

public interface ICategoriaMetaService extends IGenericService<CategoriaMeta, Integer>{
    List<CategoriaMetaDTO> listaCategoriasMetaPorNombre();
}

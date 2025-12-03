package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.MisCategoriasDTO;
import com.example.restapp.GestorFinanciero.dto.VerMisCategoriasDTO;
import com.example.restapp.GestorFinanciero.models.MisCategoriasMetas;

public interface IMisCategoriaService extends IGenericService<MisCategoriasMetas, Integer> {
    MisCategoriasMetas crearMiCategoria(MisCategoriasDTO dto, Integer idUsuario);
    List<VerMisCategoriasDTO> listarMisCategorias(Integer idUsuario);
}

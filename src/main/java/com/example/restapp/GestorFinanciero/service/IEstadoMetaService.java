package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.EstadoMetaDTO;
import com.example.restapp.GestorFinanciero.models.EstadoMeta;

public interface IEstadoMetaService extends IGenericService<EstadoMeta, Integer> {
    List<EstadoMetaDTO> listarNombresEstadoMeta(); 
}
    

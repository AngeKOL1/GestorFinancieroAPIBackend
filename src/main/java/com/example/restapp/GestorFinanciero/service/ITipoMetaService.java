package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.TipoMetaDTO;
import com.example.restapp.GestorFinanciero.models.TipoMeta;

public interface ITipoMetaService extends IGenericService<TipoMeta, Integer>{
    List<TipoMetaDTO> listarNombresTipoMeta();
}

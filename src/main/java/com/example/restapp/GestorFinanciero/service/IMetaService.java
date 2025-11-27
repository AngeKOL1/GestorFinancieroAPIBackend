package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.CrearMetaDTO;
import com.example.restapp.GestorFinanciero.models.Meta;

public interface IMetaService extends IGenericService<Meta, Integer> {
    Meta crearMetaDTO(CrearMetaDTO dto) ;
    List<Meta> listarMetasPorUsuario(Integer idUsuario);
    Integer primeraMeta(Integer idUsuario);
    Integer asignarXpPorMeta(Integer idUsuario);
    void validarCumplimientoDeMeta (Meta meta);
}

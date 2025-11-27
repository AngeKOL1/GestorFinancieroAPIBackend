package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.CrearMetaDTO;
import com.example.restapp.GestorFinanciero.models.Meta;

public interface IMetaService extends IGenericService<Meta, Integer> {
    Meta crearMetaDTO(CrearMetaDTO dto) throws Exception;
    List<Meta> listarMetasPorUsuario(Integer idUsuario) throws Exception;
    Integer primeraMeta(Integer idUsuario) throws Exception ;
    Integer asignarXpPorMeta(Integer idUsuario) throws Exception;
    void validarCumplimientoDeMeta (Meta meta) throws Exception;
}

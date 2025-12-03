package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.CrearMetaDTO;
import com.example.restapp.GestorFinanciero.dto.EditarMetaDTO;
import com.example.restapp.GestorFinanciero.models.Meta;
import com.example.restapp.GestorFinanciero.models.Transaccion;

public interface IMetaService extends IGenericService<Meta, Integer> {
    Meta crearMetaDTO(CrearMetaDTO dto) ;
    List<Meta> listarMetasPorUsuario(Integer idUsuario);
    Integer primeraMeta(Integer idUsuario);
    Integer asignarXpPorMeta(Integer idUsuario);
    void validarCumplimientoDeMeta (Meta meta);
    void verificarCumplimientoRapido(Meta meta);
    List<Transaccion> listarTransaccionesPorMeta(Integer idMeta, Integer idUsuario);
    Meta editarMeta(Integer idMeta, Integer idUsuario, EditarMetaDTO dto);
    void eliminarMeta(Integer idMeta, Integer idUsuario);
}

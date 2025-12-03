package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.TrofeoEstadoDTO;
import com.example.restapp.GestorFinanciero.dto.TrofeosDTO;
import com.example.restapp.GestorFinanciero.models.Trofeos;

public interface ITrofeoService extends IGenericService<Trofeos, Integer> {
    List<TrofeosDTO> obtenerTrofeos ();
    TrofeosDTO obtenerUltimoTrofeoDTO(Integer idUsuario);
    List<TrofeoEstadoDTO> obtenerTrofeosConEstado(Integer idUsuario);
}

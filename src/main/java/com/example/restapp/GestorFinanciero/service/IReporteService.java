package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.ReporteDTO;
import com.example.restapp.GestorFinanciero.models.Reporte;

public interface IReporteService extends IGenericService<Reporte, Integer>{
    Reporte generarReporte(ReporteDTO dto, Integer idUsuario);
}
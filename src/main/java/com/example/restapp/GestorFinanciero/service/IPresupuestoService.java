package com.example.restapp.GestorFinanciero.service;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.PresupuestoDTO;
import com.example.restapp.GestorFinanciero.dto.PresupuestoResumenDTO;
import com.example.restapp.GestorFinanciero.models.Presupuesto;

public interface IPresupuestoService extends IGenericService<Presupuesto, Integer>{
    Presupuesto crearPresupuestoDto (PresupuestoDTO presupuestoDTO, Integer idUsuario);
    void evaluarEstadoPresupuesto(Presupuesto p);
    boolean verificarPresupuestosCompletados(Integer idUsuario);
    List<PresupuestoResumenDTO> listarPresupuestosPorUsuario(Integer idUsuario);
}

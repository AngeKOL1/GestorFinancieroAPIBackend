package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.models.Transaccion;
import com.example.restapp.GestorFinanciero.models.Usuario;

import java.util.List;

import com.example.restapp.GestorFinanciero.dto.EditarTransaccionDTO;
import com.example.restapp.GestorFinanciero.dto.TransaccionDTO;

public interface ITransaccionService extends IGenericService<Transaccion, Integer>{
    Transaccion CrearTransaccionDTO(TransaccionDTO dto);
    Transaccion updateTransaccion(Integer idTransaccion, Integer idUsuario, EditarTransaccionDTO transaccionDTO);
    Integer cantidadDeTransacciones(Usuario user);
    void eliminarTransaccion(Integer idTransaccion, Integer idUsuario);
    List<Transaccion> listarTransaccionesPorUsuario(Integer idUsuario);
    List<Transaccion> listarTransaccionesPorPresupuesto(Integer idPresupuesto, Integer idUsuario);
}

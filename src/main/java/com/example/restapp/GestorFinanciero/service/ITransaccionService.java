package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.models.Transaccion;
import com.example.restapp.GestorFinanciero.dto.EditarTransaccionDTO;
import com.example.restapp.GestorFinanciero.dto.TransaccionDTO;

public interface ITransaccionService extends IGenericService<Transaccion, Integer>{
    Transaccion CrearTransaccionDTO(TransaccionDTO dto) throws Exception;
    Transaccion updateTransaccion(Integer idTransaccion, Integer idUsuario, EditarTransaccionDTO transaccionDTO) throws Exception;
}

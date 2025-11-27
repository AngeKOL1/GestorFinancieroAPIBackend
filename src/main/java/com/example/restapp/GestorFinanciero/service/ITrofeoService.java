package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.models.Trofeos;

public interface ITrofeoService extends IGenericService<Trofeos, Integer> {
     Trofeos asignarTrofeoPorXP (Integer idUsuario);
}

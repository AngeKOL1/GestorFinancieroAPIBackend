package com.example.restapp.GestorFinanciero.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.restapp.GestorFinanciero.dto.EstadoMetaDTO;
import com.example.restapp.GestorFinanciero.models.EstadoMeta;
import com.example.restapp.GestorFinanciero.repo.EstadoMetaRepo;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.service.IEstadoMetaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadoMetaService extends GenericService<EstadoMeta, Integer> implements IEstadoMetaService {
    private final EstadoMetaRepo repo;
    @Override
    protected IGenericRepo<EstadoMeta, Integer> getRepo(){
        return repo;
    }
    @Override
    public List<EstadoMetaDTO> listarNombresEstadoMeta(){
        List<EstadoMeta> lista= repo.findAll();

        return lista.stream().map(m->{
            EstadoMetaDTO dto = new EstadoMetaDTO();
            dto.setNombreEstadoMeta(m.getNombreEstadoMeta());
            return dto;
        }).toList();
    }
}

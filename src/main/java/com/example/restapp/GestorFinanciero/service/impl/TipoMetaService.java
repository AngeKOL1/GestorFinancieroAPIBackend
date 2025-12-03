package com.example.restapp.GestorFinanciero.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.restapp.GestorFinanciero.dto.TipoMetaDTO;
import com.example.restapp.GestorFinanciero.models.TipoMeta;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.TipoMetaRepo;
import com.example.restapp.GestorFinanciero.service.ITipoMetaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoMetaService extends GenericService<TipoMeta, Integer> implements ITipoMetaService {
    private final TipoMetaRepo repo;
    @Override
    protected IGenericRepo<TipoMeta,Integer> getRepo(){
        return repo;
    }

    @Override
    public List<TipoMetaDTO> listarNombresTipoMeta(){
        List<TipoMeta> lista =  repo.findAll();
        return lista.stream().map(m->{
            TipoMetaDTO dto= new TipoMetaDTO();
            dto.setNombreTipoMeta(m.getNombreTipoMeta());
            return dto;
        }).toList();
    }

}

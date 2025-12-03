package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.dto.CategoriaMetaDTO;
import com.example.restapp.GestorFinanciero.models.CategoriaMeta;
import com.example.restapp.GestorFinanciero.repo.CategoriaMetaRepo;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.service.ICategoriaMetaService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaMetaService extends GenericService<CategoriaMeta,Integer> implements ICategoriaMetaService {
    private final CategoriaMetaRepo repo;
    @Override
    protected IGenericRepo<CategoriaMeta,Integer> getRepo(){
        return repo;
    }
    @Override
    public  List<CategoriaMetaDTO> listaCategoriasMetaPorNombre(){
        List<CategoriaMeta> categoriaMetas= repo.findAll();
        return categoriaMetas.stream().map(m->{
            CategoriaMetaDTO dto = new CategoriaMetaDTO();
            dto.setNombreCategoria(m.getNombre());
            return dto;
        }).toList();
    }
}

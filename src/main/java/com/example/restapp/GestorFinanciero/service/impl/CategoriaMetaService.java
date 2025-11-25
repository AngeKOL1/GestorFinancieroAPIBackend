package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.CategoriaMeta;
import com.example.restapp.GestorFinanciero.repo.CategoriaMetaRepo;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.service.ICategoriaMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaMetaService extends GenericService<CategoriaMeta,Integer> implements ICategoriaMetaService {
    private final CategoriaMetaRepo repo;
    @Override
    protected IGenericRepo<CategoriaMeta,Integer> getRepo(){
        return repo;
    }
}

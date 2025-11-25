package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.UsuarioLogro;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioLogroRepo;
import com.example.restapp.GestorFinanciero.service.IUsuarioLogroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioLogroService extends GenericService<UsuarioLogro, Integer> implements IUsuarioLogroService {
    private final UsuarioLogroRepo repo;
    @Override
    protected IGenericRepo<UsuarioLogro, Integer> getRepo() {
        return repo;
    }
}

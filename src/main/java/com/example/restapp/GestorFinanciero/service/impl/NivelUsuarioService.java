package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.NivelUsuario;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.NivelUsuarioRepo;
import com.example.restapp.GestorFinanciero.service.INivelUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NivelUsuarioService extends GenericService<NivelUsuario, Integer> implements INivelUsuarioService {
    private final NivelUsuarioRepo repo;
    @Override
    protected IGenericRepo<NivelUsuario, Integer> getRepo(){
        return repo;
    }
}

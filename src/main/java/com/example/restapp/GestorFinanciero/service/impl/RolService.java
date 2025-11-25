package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.Rol;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.RolRepo;
import com.example.restapp.GestorFinanciero.service.IRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolService extends GenericService<Rol, Integer> implements IRolService {
    private final RolRepo repo;
    @Override
    protected IGenericRepo<Rol, Integer> getRepo(){
        return repo;
    }
}

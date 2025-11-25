package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.Presupuesto;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.PresupuestoRepo;
import com.example.restapp.GestorFinanciero.service.IPresupuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresupuestoService extends GenericService<Presupuesto, Integer> implements IPresupuestoService {
    private final PresupuestoRepo repo;
    @Override
    protected IGenericRepo<Presupuesto, Integer> getRepo(){
        return repo;
    }
}

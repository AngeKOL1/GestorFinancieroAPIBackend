package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.Reporte;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.ReporteRepo;
import com.example.restapp.GestorFinanciero.service.IReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReporteService extends GenericService<Reporte, Integer> implements IReporteService {
    private final ReporteRepo repo;
    @Override
    protected IGenericRepo<Reporte, Integer> getRepo(){
        return repo;
    }
}

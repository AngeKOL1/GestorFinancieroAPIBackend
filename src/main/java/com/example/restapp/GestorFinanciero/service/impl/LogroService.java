package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.Logro;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.LogroRepo;
import com.example.restapp.GestorFinanciero.service.ILogroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogroService extends GenericService<Logro, Integer> implements ILogroService {
    private final LogroRepo repo;
    @Override
    protected IGenericRepo<Logro, Integer> getRepo(){
        return repo;
    }
}

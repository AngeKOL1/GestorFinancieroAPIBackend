package com.example.restapp.GestorFinanciero.repo;

import com.example.restapp.GestorFinanciero.models.Trofeos;

import java.util.Optional;

public interface TrofeoRepo extends IGenericRepo<Trofeos, Integer> {
    Optional<Trofeos> findFirstByOrderByIdTrofeoAsc();
    Optional<Trofeos> findByNombreTrofeo(String nombreTrofeo);
}

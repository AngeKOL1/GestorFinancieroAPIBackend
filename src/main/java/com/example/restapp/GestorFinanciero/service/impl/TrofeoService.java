package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.Trofeos;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.TrofeoRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.ITrofeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrofeoService extends GenericService<Trofeos, Integer> implements ITrofeoService {
    private final TrofeoRepo repo;
    private final UsuarioRepo repoUsuario;
    private final TrofeoRepo repoTrofeo;
    @Override
    protected IGenericRepo<Trofeos, Integer> getRepo() {
        return repo;
    }
    //Método para asignar trofeo según XP
    @Override
    public Trofeos asignarTrofeoPorXP(Integer idUsuario) throws Exception {

        Usuario user = repoUsuario.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario con ID " + idUsuario + " no existe"));

        Integer xp = user.getXp();

        // Trofeos trofeo = repoTrofeo.findTrofeoByXpBetween(xp)
        //         .orElseThrow(() -> new ModelNotFoundException("No hay trofeo para XP: " + xp));

        // user.setTrofeo(trofeo);
        // repoUsuario.save(user);

        return null;
    }

}

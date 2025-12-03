package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.dto.TrofeoEstadoDTO;
import com.example.restapp.GestorFinanciero.dto.TrofeosDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.Trofeos;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.models.UsuarioTrofeo;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.TrofeoRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioTrofeoRepo;
import com.example.restapp.GestorFinanciero.service.ITrofeoService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrofeoService extends GenericService<Trofeos, Integer> implements ITrofeoService {
    private final TrofeoRepo trofeoRepo;
    private final UsuarioRepo usuarioRepo;
    private final UsuarioTrofeoRepo usuarioTrofeoRepo;
   
    @Override
    protected IGenericRepo<Trofeos, Integer> getRepo() {
        return trofeoRepo;
    }
    @Override
    public List<TrofeosDTO> obtenerTrofeos() {

        List<Trofeos> trofeos = trofeoRepo.findAll();

        return trofeos.stream().map(trofeo -> {
            TrofeosDTO dto = new TrofeosDTO();
            dto.setNombre(trofeo.getNombreTrofeo());
            dto.setPrerequisito(trofeo.getPrerequisito());
            dto.setXp(trofeo.getXpRequerida());
            return dto;
        }).toList();
    }

    @Override
    public TrofeosDTO obtenerUltimoTrofeoDTO(Integer idUsuario) {

        UsuarioTrofeo ultimo = usuarioTrofeoRepo
                .findTopByUsuario_IdOrderByFechaObtencionTrofeoDesc(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("El usuario aún no tiene trofeos"));

        TrofeosDTO dto = new TrofeosDTO();
        dto.setNombre(ultimo.getTrofeo().getNombreTrofeo());
        dto.setPrerequisito(ultimo.getTrofeo().getPrerequisito());
        dto.setXp(ultimo.getTrofeo().getXpRequerida());
        dto.setFecha(ultimo.getFechaObtencionTrofeo());

        return dto;
    }

    @Override
    public List<TrofeoEstadoDTO> obtenerTrofeosConEstado(Integer idUsuario) {

        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        Set<Integer> idsObtenidos = user.getUsuarioTrofeo().stream()
                .map(ut -> ut.getTrofeo().getIdTrofeo())
                .collect(Collectors.toSet());

        List<Trofeos> todos = trofeoRepo.findAll();

        return todos.stream().map(t -> {

            TrofeoEstadoDTO dto = new TrofeoEstadoDTO();
            dto.setIdTrofeo(t.getIdTrofeo());
            dto.setNombreTrofeo(t.getNombreTrofeo());
            dto.setPrerequisito(t.getPrerequisito());
            dto.setXpRequerida(t.getXpRequerida());

            dto.setObtenido(idsObtenidos.contains(t.getIdTrofeo()));

            return dto;
        }).toList();
    }


}

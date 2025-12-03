package com.example.restapp.GestorFinanciero.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.restapp.GestorFinanciero.dto.MisCategoriasDTO;
import com.example.restapp.GestorFinanciero.dto.VerMisCategoriasDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.MisCategoriasMetas;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.MisCategoriasMetaRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.IMisCategoriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MisCategoriasService extends GenericService<MisCategoriasMetas,Integer> implements IMisCategoriaService {

    private final MisCategoriasMetaRepo repo;
    private final UsuarioRepo usuarioRepo;

    @Override
    protected IGenericRepo<MisCategoriasMetas, Integer> getRepo() {
        return repo;
    }

    @Override
    public MisCategoriasMetas crearMiCategoria(MisCategoriasDTO dto, Integer idUsuario) {

        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        if (repo.existsByNombreAndUsuario_Id(dto.getNombre(), idUsuario)) {
            throw new IllegalArgumentException("Ya tienes una categoría personalizada con ese nombre");
        }

        MisCategoriasMetas misCategoriasMetas = new MisCategoriasMetas();
        misCategoriasMetas.setNombre(dto.getNombre());
        misCategoriasMetas.setDescripcion(dto.getDescripcion());
        misCategoriasMetas.setEstado(true);
        misCategoriasMetas.setUsuario(user);

        user.getMisCategoriasMetas().add(misCategoriasMetas);

        MisCategoriasMetas guardada = repo.save(misCategoriasMetas);

        return guardada;
    }

    @Override
    public List<VerMisCategoriasDTO> listarMisCategorias(Integer idUsuario) {

        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        List<MisCategoriasMetas> lista = user.getMisCategoriasMetas();

        return lista.stream()
                .map(m -> {
                    VerMisCategoriasDTO dto = new VerMisCategoriasDTO();
                    dto.setNombre(m.getNombre());
                    return dto;
                })
                .toList();
    }

}


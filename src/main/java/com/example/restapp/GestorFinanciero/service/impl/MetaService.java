package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.dto.CrearMetaDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.*;
import com.example.restapp.GestorFinanciero.service.IMetaService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaService extends GenericService<Meta, Integer> implements IMetaService {
    private final MetaRepo repo;
    private final UsuarioRepo usuarioRepo;
    private final CategoriaMetaRepo categoriaMetaRepo;
    private final MisCategoriasMetaRepo misCategoriasMetaRepo;
    private final TipoMetaRepo tipoMetaRepo;
    private final EstadoMetaRepo estadoMetaRepo;

    private final  UsuarioService usuarioService;

    @Override
    protected IGenericRepo<Meta, Integer> getRepo(){
        return repo;
    }

    @Override
    public Meta crearMetaDTO(CrearMetaDTO dto) {

        Meta meta = new Meta();
        meta.setNombre(dto.getNombre());
        meta.setMontoActual(0.0);
        meta.setMontoObjetivo(dto.getMontoObjetivo());
        meta.setFechaInicial(LocalDate.now());
        meta.setFechaFinal(dto.getFechaFinal());

        LocalDate hoy = LocalDate.now();
        FechaMeta fechaMeta = new FechaMeta();
        fechaMeta.setDia(hoy.getDayOfMonth());
        fechaMeta.setMes(hoy.getMonthValue());
        fechaMeta.setAnio(hoy.getYear());
        fechaMeta.setFechaTotal(hoy);
        fechaMeta.setMeta(meta);
        meta.setFechaMeta(fechaMeta);

        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));
        meta.setUsuarioMetas(usuario);

        if (dto.getNombreCategoria() != null) {
            CategoriaMeta categoria = categoriaMetaRepo
                    .findByNombre(dto.getNombreCategoria())
                    .orElseThrow(() -> new ModelNotFoundException("Categoría meta no encontrada"));
            meta.setCategoriaMetas(categoria);
        }

        if (dto.getNombreMisCategoria() != null) {
            MisCategoriasMetas misCategoria = misCategoriasMetaRepo
                    .findByNombre(dto.getNombreMisCategoria())
                    .orElseThrow(() -> new ModelNotFoundException("Mi categoría meta no encontrada"));
            meta.setMisCategoriaMeta(misCategoria);
        }

        TipoMeta tipoMeta = tipoMetaRepo.findByNombreTipoMeta(dto.getNombreTipoMeta())
                .orElseThrow(() -> new ModelNotFoundException("Tipo de meta no encontrado"));
        meta.setTipoMeta(tipoMeta);

        EstadoMeta estado = estadoMetaRepo.findByNombreEstadoMeta(dto.getNombreEstadoMeta())
                .orElseThrow(() -> new ModelNotFoundException("Estado meta no encontrado"));
        meta.setEstadoMeta(estado);

        meta.setMetaTransaccion(new HashSet<>());
        meta.setPresupuesto(null);

        asignarXpPorMeta(dto.getIdUsuario());

        repo.save(meta);

        usuarioService.verificarMetasEnCategoriasDiferentes(usuario.getId());

        return meta;
    }

    @Override
    public List<Meta> listarMetasPorUsuario(Integer idUsuario) {
        return repo.findByUsuarioMetas_Id(idUsuario);
    }

    @Override
    public Integer primeraMeta(Integer idUsuario) {

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        return usuario.getMetas().isEmpty() ? 0 : 1;
    }

    @Override
    public Integer asignarXpPorMeta(Integer idUsuario){
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));
        if (primeraMeta(idUsuario) == 1){
            usuario.setXp(usuario.getXp()+100);
            usuarioService.asignarNiveles(idUsuario);
            return 100;
        }
        usuarioService.asignarTrofeo(usuario, 2);
        return 250;
    }

   @Override
    public void validarCumplimientoDeMeta(Meta meta) {

        LocalDate fechaInicial = meta.getFechaInicial();
        LocalDate fechaFinal = meta.getFechaFinal();
        LocalDate hoy = LocalDate.now();

        Usuario user = usuarioRepo.findById(meta.getUsuarioMetas().getId())
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        boolean montoCumplido = meta.getMontoActual().compareTo(meta.getMontoObjetivo()) >= 0;
        boolean fechaEnRango = (hoy.isEqual(fechaInicial) || hoy.isAfter(fechaInicial))
                            && (hoy.isEqual(fechaFinal) || hoy.isBefore(fechaFinal));

        if (montoCumplido && fechaEnRango) {
            usuarioService.asignarTrofeo(user, 3);
            meta.setEstadoMeta(estadoMetaRepo.findById(1).orElseThrow(() -> new ModelNotFoundException("Estado no encontrado")));
        }
    }

    @Override
    public void verificarCumplimientoRapido(Meta meta) {

        if (meta.getMontoActual() < meta.getMontoObjetivo()) {
            return;
        }

        LocalDate fechaInicio = meta.getFechaInicial();
        LocalDate fechaCumplimiento = LocalDate.now(); 

        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaCumplimiento);
                    Usuario usuario = meta.getUsuarioMetas();

        if (dias < 7 && ! usuarioService.usuarioTieneTrofeo(usuario, 13)) {

            usuarioService.asignarTrofeo(usuario, 13);

            System.out.println("Trofeo por cumplimiento rápido asignado");
        }
    }

    
    @Override
    public List<Transaccion> listarTransaccionesPorMeta(Integer idMeta, Integer idUsuario) {

        Meta meta = repo.findById(idMeta)
                .orElseThrow(() -> new ModelNotFoundException("Meta no encontrada"));

        if (!meta.getUsuarioMetas().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("No puedes ver transacciones de una meta que no es tuya");
        }

        return repo.findByMetaId(idMeta);
    }



}

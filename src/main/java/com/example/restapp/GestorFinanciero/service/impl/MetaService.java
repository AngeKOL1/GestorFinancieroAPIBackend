package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.dto.CrearMetaDTO;
import com.example.restapp.GestorFinanciero.dto.EditarMetaDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.*;
import com.example.restapp.GestorFinanciero.service.IMetaService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (dto.getNombreCategoria() != null && !dto.getNombreCategoria().isBlank()) {
            CategoriaMeta categoria = categoriaMetaRepo
                    .findByNombre(dto.getNombreCategoria())
                    .orElseThrow(() -> new ModelNotFoundException("Categoría meta no encontrada"));
            meta.setCategoriaMetas(categoria);
        } else {
            meta.setCategoriaMetas(null); 
        }

        if (dto.getNombreMisCategoria() != null && !dto.getNombreMisCategoria().isBlank()) {

            MisCategoriasMetas misCategoria = misCategoriasMetaRepo
                    .findByNombreAndUsuario_Id(dto.getNombreMisCategoria(), usuario.getId())
                    .orElseThrow(() -> new ModelNotFoundException("Mi categoría meta no encontrada para este usuario"));

            meta.setMisCategoriaMeta(misCategoria);
        }else {
            meta.setMisCategoriaMeta(null); 
        }


        if (meta.getCategoriaMetas() == null && meta.getMisCategoriaMeta() == null) {
            throw new IllegalArgumentException("Debe seleccionar al menos una categoría (principal o personalizada)");
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

        // CORRECCIÓN: si no tiene metas, sí es primera meta
        return usuario.getMetas().isEmpty() ? 1 : 0;
    }


    @Override
    public Integer asignarXpPorMeta(Integer idUsuario){

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        if (usuario.getXp() == null) {
            usuario.setXp(0); // ← PREVENCIÓN DE NULOS
        }

        boolean esPrimeraMeta = usuario.getMetas().isEmpty();

        if (esPrimeraMeta) {
            usuario.setXp(usuario.getXp() + 100);
            usuarioService.asignarNiveles(idUsuario);
            return 100;
        }

        usuarioService.asignarTrofeo(usuario, 2);
        usuario.setXp(usuario.getXp() + 250);
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

    @Override
    @Transactional
    public Meta editarMeta(Integer idMeta, Integer idUsuario, EditarMetaDTO dto) {

        Meta meta = repo.findById(idMeta)
                .orElseThrow(() -> new ModelNotFoundException("Meta no encontrada"));

        if (!meta.getUsuarioMetas().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("No tiene permiso para editar esta meta");
        }

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            meta.setNombre(dto.getNombre());
        }

        if (dto.getFechaFinal() != null) {
            if (dto.getFechaFinal().isBefore(meta.getFechaInicial())) {
                throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha inicial");
            }
            meta.setFechaFinal(dto.getFechaFinal());
        }

        if (dto.getMontoObjetivo() != null) {
            if (dto.getMontoObjetivo() <= 0) {
                throw new IllegalArgumentException("El monto objetivo debe ser mayor que cero");
            }

            if (dto.getMontoObjetivo() < meta.getMontoActual()) {
                throw new IllegalArgumentException("El monto objetivo no puede ser menor al monto actual ahorrado");
            }

            meta.setMontoObjetivo(dto.getMontoObjetivo());
        }

        return repo.save(meta);
    }

    @Override
    @Transactional
    public void eliminarMeta(Integer idMeta, Integer idUsuario) {

        Meta meta = repo.findById(idMeta)
                .orElseThrow(() -> new ModelNotFoundException("Meta no encontrada"));

        if (!meta.getUsuarioMetas().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("No tiene permiso para eliminar esta meta");
        }

        repo.delete(meta);
    }


}

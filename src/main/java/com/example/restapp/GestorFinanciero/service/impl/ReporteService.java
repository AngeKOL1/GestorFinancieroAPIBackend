package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.dto.ReporteDTO;
import com.example.restapp.GestorFinanciero.models.Meta;
import com.example.restapp.GestorFinanciero.models.Reporte;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.MetaRepo;
import com.example.restapp.GestorFinanciero.repo.ReporteRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.IReporteService;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReporteService extends GenericService<Reporte, Integer> implements IReporteService {
    private final ReporteRepo reporteRepo;
    private final UsuarioRepo usuarioRepo;
    private final MetaRepo metaRepo;

    private final IUsuarioService usuarioService;
    @Override
    protected IGenericRepo<Reporte, Integer> getRepo(){
        return reporteRepo;
    }
    @Override
    public Reporte generarReporte(ReporteDTO dto, Integer idUsuario) {

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Meta meta = null;
        if (dto.getIdMeta() != null) {

            meta = metaRepo.findById(dto.getIdMeta())
                    .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

            if (!meta.getUsuarioMetas().getId().equals(idUsuario)) {
                throw new RuntimeException("La meta no pertenece al usuario que intenta generar el reporte");
            }
        }

        Reporte reporte = new Reporte();
        reporte.setUsuarioReporte(usuario);
        reporte.setFechaGeneracion(LocalDate.now());

        if (meta != null) {
            reporte.setTipo("META");
            reporte.setTitulo("Reporte de la meta: " + meta.getNombre());
            reporte.setDescripcion("Informe generado del progreso de la meta");
            reporte.setObservaciones("Reporte generado automáticamente");

            reporte.setMeta(meta);

            reporte.setMontoActual(meta.getMontoActual());
            reporte.setMontoObjetivo(meta.getMontoObjetivo());
            reporte.setPorcentajeAvance((meta.getMontoActual() / meta.getMontoObjetivo()) * 100);
            reporte.setEstadoMeta(meta.getEstadoMeta().getNombreEstadoMeta());
            reporte.setCategoriaMeta(meta.getCategoriaMetas().getNombre());
            reporte.setTipoMeta(meta.getTipoMeta().getNombreTipoMeta());

            reporte.setFechaInicio(meta.getFechaInicial());
            reporte.setFechaFin(meta.getFechaFinal());

            if (meta.getMontoActual() >= meta.getMontoObjetivo()) {
                reporte.setFechaCumplimientoMeta(LocalDate.now());
            }

        } else {

            reporte.setTipo("GENERAL");
            reporte.setTitulo("Reporte general del usuario");
            reporte.setDescripcion("Reporte automático sin meta asociada");
            reporte.setObservaciones("Reporte generado automáticamente");


            reporte.setFechaInicio(null);
            reporte.setFechaFin(null);
        }


        if (usuario.getReportes().isEmpty()) {
            usuarioService.asignarTrofeo(usuario, 5);
        }

        return reporteRepo.save(reporte);
    }



}

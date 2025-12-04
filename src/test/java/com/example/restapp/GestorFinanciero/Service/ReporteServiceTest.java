package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.ReporteDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.MetaRepo;
import com.example.restapp.GestorFinanciero.repo.ReporteRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.impl.ReporteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepo reporteRepo;

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private MetaRepo metaRepo;

    @Mock
    private IUsuarioService usuarioService;

    @InjectMocks
    private ReporteService reporteService;

    private Usuario usuario;
    private Meta meta;

    @BeforeEach
    void init() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setReportes(new java.util.ArrayList<>());

        meta = new Meta();
        meta.setIdMeta(10);
        meta.setNombre("Ahorro Laptop");
        meta.setMontoActual(500.0);
        meta.setMontoObjetivo(1000.0);
        meta.setFechaInicial(LocalDate.of(2025, 1, 1));
        meta.setFechaFinal(LocalDate.of(2025, 12, 1));

        CategoriaMeta categoria = new CategoriaMeta();
        categoria.setNombre("Tecnología");
        meta.setCategoriaMetas(categoria);

        TipoMeta tipoMeta = new TipoMeta();
        tipoMeta.setNombreTipoMeta("Ahorro");
        meta.setTipoMeta(tipoMeta);

        EstadoMeta estado = new EstadoMeta();
        estado.setNombreEstadoMeta("En progreso");
        meta.setEstadoMeta(estado);

        meta.setUsuarioMetas(usuario);
    }


    @Test
    void generarReporte_generalDebeCrearseCorrectamente() {

        ReporteDTO dto = new ReporteDTO(); // Sin idMeta → es reporte general

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(reporteRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Reporte result = reporteService.generarReporte(dto, 1);

        assertNotNull(result);
        assertEquals("GENERAL", result.getTipo());
        assertEquals("Reporte general del usuario", result.getTitulo());
        assertNull(result.getMeta());
        verify(reporteRepo, times(1)).save(any());
    }


    @Test
    void generarReporte_metaDebeGenerarReporteCorrecto() {

        ReporteDTO dto = new ReporteDTO();
        dto.setIdMeta(10);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(metaRepo.findById(10)).thenReturn(Optional.of(meta));
        when(reporteRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Reporte result = reporteService.generarReporte(dto, 1);

        assertNotNull(result);
        assertEquals("META", result.getTipo());
        assertEquals("Reporte de la meta: Ahorro Laptop", result.getTitulo());
        assertEquals(meta, result.getMeta());
        assertEquals(50.0, result.getPorcentajeAvance());
        verify(reporteRepo).save(any());
    }


    @Test
    void generarReporte_debeFallarSiMetaNoEsDelUsuario() {

        ReporteDTO dto = new ReporteDTO();
        dto.setIdMeta(10);

        // Otro usuario asignado a la meta
        Usuario otro = new Usuario();
        otro.setId(999);
        meta.setUsuarioMetas(otro);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(metaRepo.findById(10)).thenReturn(Optional.of(meta));

        assertThrows(IllegalArgumentException.class,
                () -> reporteService.generarReporte(dto, 1)
        );
    }
}

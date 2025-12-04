package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.PresupuestoDTO;
import com.example.restapp.GestorFinanciero.dto.PresupuestoResumenDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.EstadoPresupuesto;
import com.example.restapp.GestorFinanciero.models.Presupuesto;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.EstadoPresupuestoRepo;
import com.example.restapp.GestorFinanciero.repo.PresupuestoRepo;
import com.example.restapp.GestorFinanciero.repo.TransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.impl.PresupuestoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresupuestoServiceTest {

    @Mock private PresupuestoRepo repo;
    @Mock private UsuarioRepo usuarioRepo;
    @Mock private EstadoPresupuestoRepo estadoPresupuestoRepo;
    @Mock private TransaccionRepo transaccionRepo;
    @Mock private IUsuarioService usuarioService;

    @InjectMocks
    private PresupuestoService presupuestoService;

    private Usuario usuario;
    private EstadoPresupuesto activo;
    private EstadoPresupuesto riesgo;
    private EstadoPresupuesto excedido;
    private EstadoPresupuesto completado;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1);

        activo = new EstadoPresupuesto(1, "Activo", "Presupuesto dentro del límite", null);
        riesgo = new EstadoPresupuesto(2, "En Riesgo", "Presupuesto llegando al límite", null);
        excedido = new EstadoPresupuesto(3, "Excedido", "Presupuesto superado", null);
        completado = new EstadoPresupuesto(4, "Completado", "Presupuesto cumplido", null);
    }


    @Test
    void crearPresupuestoDto_debeCrearCorrectamente() {

        PresupuestoDTO dto = new PresupuestoDTO();
        dto.setMontoEstablecido(1000f);
        dto.setMontoMaximo(1500f);
        dto.setMontoMinimo(500f);
        dto.setPeriodo("Enero");
        dto.setFechaInicial(LocalDate.now());
        dto.setFechaFinal(LocalDate.now().plusDays(10));
        dto.setNombreEstadoPresupuesto("Activo");

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(estadoPresupuestoRepo.findByNombreEstadoPresupuesto("Activo"))
                .thenReturn(Optional.of(activo));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Presupuesto p = presupuestoService.crearPresupuestoDto(dto, 1);

        assertNotNull(p);
        assertEquals(1000f, p.getMontoEstablecido());
        assertEquals("Enero", p.getPeriodo());
        assertEquals(usuario, p.getUsuarioPresupuesto());
    }

    @Test
    void evaluarEstadoPresupuesto_debeSerActivo() {
        Presupuesto p = new Presupuesto();
        p.setMontoEstablecido(1000f);
        p.setMontoActual(300f);
        p.setFechaFinal(LocalDate.now().plusDays(3));

        when(estadoPresupuestoRepo.findByNombreEstadoPresupuesto("Activo")).thenReturn(Optional.of(activo));
        when(repo.save(any())).thenReturn(p);

        presupuestoService.evaluarEstadoPresupuesto(p);

        assertEquals("Activo", p.getEstadoPresupuesto().getNombreEstadoPresupuesto());
    }


    @Test
    void evaluarEstadoPresupuesto_debeSerEnRiesgo() {

        Presupuesto p = new Presupuesto();
        p.setMontoEstablecido(1000f);
        p.setMontoActual(850f);
        p.setFechaFinal(LocalDate.now().plusDays(3));

        when(estadoPresupuestoRepo.findByNombreEstadoPresupuesto("En Riesgo")).thenReturn(Optional.of(riesgo));

        presupuestoService.evaluarEstadoPresupuesto(p);

        assertEquals("En Riesgo", p.getEstadoPresupuesto().getNombreEstadoPresupuesto());
    }


    @Test
    void evaluarEstadoPresupuesto_debeSerExcedido() {

        Presupuesto p = new Presupuesto();
        p.setMontoEstablecido(1000f);
        p.setMontoActual(1200f);
        p.setFechaFinal(LocalDate.now().plusDays(3));

        when(estadoPresupuestoRepo.findByNombreEstadoPresupuesto("Excedido")).thenReturn(Optional.of(excedido));

        presupuestoService.evaluarEstadoPresupuesto(p);

        assertEquals("Excedido", p.getEstadoPresupuesto().getNombreEstadoPresupuesto());
    }

    @Test
    void evaluarEstadoPresupuesto_debeSerCompletadoCuandoFechaFinalPaso() {

        Presupuesto p = new Presupuesto();
        p.setMontoEstablecido(1000f);
        p.setMontoActual(900f);                         // 90% → primero "En Riesgo"
        p.setFechaFinal(LocalDate.now().minusDays(1));  // Periodo finalizado

        // 1️⃣ Primero el servicio pedirá "En Riesgo"
        when(estadoPresupuestoRepo.findByNombreEstadoPresupuesto("En Riesgo"))
                .thenReturn(Optional.of(riesgo));

        // 2️⃣ Luego, como ya pasó la fecha y actual <= limite, pedirá "Completado"
        when(estadoPresupuestoRepo.findByNombreEstadoPresupuesto("Completado"))
                .thenReturn(Optional.of(completado));

        presupuestoService.evaluarEstadoPresupuesto(p);

        assertEquals("Completado", p.getEstadoPresupuesto().getNombreEstadoPresupuesto());
    }


    @Test
    void verificarPresupuestosCompletados_debeAsignarTrofeoSiHay3() {

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(repo.contarPresupuestosCompletados(1)).thenReturn(3L);

        boolean result = presupuestoService.verificarPresupuestosCompletados(1);

        assertTrue(result);
        verify(usuarioService, times(1)).asignarTrofeo(usuario, 9);
    }

    @Test
    void verificarPresupuestosCompletados_noDebeAsignarSiSonMenosDe3() {

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(repo.contarPresupuestosCompletados(1)).thenReturn(1L);

        boolean result = presupuestoService.verificarPresupuestosCompletados(1);

        assertFalse(result);
        verify(usuarioService, never()).asignarTrofeo(any(), any());
    }

    @Test
    void listarPresupuestosPorUsuario_debeConvertirAResumenDTO() {

        Presupuesto p = new Presupuesto();
        p.setIdPresupuesto(10);
        p.setMontoEstablecido(2000f);
        p.setMontoActual(500f);
        p.setPeriodo("Febrero");
        p.setFechaInicial(LocalDate.now());
        p.setFechaFinal(LocalDate.now().plusDays(30));
        p.setEstadoPresupuesto(activo);

        when(repo.findByUsuarioPresupuesto_Id(1))
                .thenReturn(List.of(p));

        List<PresupuestoResumenDTO> lista = presupuestoService.listarPresupuestosPorUsuario(1);

        assertEquals(1, lista.size());
        assertEquals("Febrero", lista.get(0).getPeriodo());
        assertEquals(2000f, lista.get(0).getMontoEstablecido());
        assertEquals("Activo", lista.get(0).getEstado());
    }
}

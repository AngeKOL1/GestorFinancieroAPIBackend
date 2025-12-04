package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.EditarTransaccionDTO;
import com.example.restapp.GestorFinanciero.dto.TransaccionDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.*;
import com.example.restapp.GestorFinanciero.service.impl.TransaccionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock private TransaccionRepo repo;
    @Mock private UsuarioRepo usuarioRepo;
    @Mock private TipoTransaccionRepo tipoTransaccionRepo;
    @Mock private MetaRepo metaRepo;
    @Mock private PresupuestoRepo presupuestoRepo;
    @Mock private IMetaService metaService;
    @Mock private IUsuarioService usuarioService;
    @Mock private IPresupuestoService presupuestoService;

    @InjectMocks
    private TransaccionService service;

    Usuario user;
    Meta meta;
    Presupuesto presupuesto;
    TipoTransaccion ingreso;
    TipoTransaccion gasto;

    @BeforeEach
    void setup() {

        user = new Usuario();
        user.setId(1);

        ingreso = new TipoTransaccion();
        ingreso.setIdTipoTransaccion(2);

        gasto = new TipoTransaccion();
        gasto.setIdTipoTransaccion(1);

        meta = new Meta();
        meta.setIdMeta(10);
        meta.setUsuarioMetas(user);
        meta.setMontoActual(100.00);

        presupuesto = new Presupuesto();
        presupuesto.setIdPresupuesto(20);
        presupuesto.setUsuarioPresupuesto(user);
        presupuesto.setMontoActual(300f);
        presupuesto.setTransacciones(new ArrayList<>());
    }


    @Test
    void crearTransaccion_metaIngreso_deberiaSumar() {

        TransaccionDTO dto = new TransaccionDTO();
        dto.setMonto(50f);
        dto.setDescripcion("Ingreso meta");
        dto.setIdUsuario(1);
        dto.setIdMeta(10);
        dto.setTipoTransaccionId(2);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user));
        when(tipoTransaccionRepo.findById(2)).thenReturn(Optional.of(ingreso));
        when(metaRepo.findById(10)).thenReturn(Optional.of(meta));

        service.CrearTransaccionDTO(dto);

        assertEquals(150f, meta.getMontoActual());
        verify(repo, times(1)).save(any());
        verify(metaService).validarCumplimientoDeMeta(meta);
    }

    @Test
    void crearTransaccion_metaGasto_deberiaRestar() {

        TransaccionDTO dto = new TransaccionDTO();
        dto.setMonto(40f);
        dto.setDescripcion("Gasto meta");
        dto.setIdUsuario(1);
        dto.setIdMeta(10);
        dto.setTipoTransaccionId(1);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user));
        when(tipoTransaccionRepo.findById(1)).thenReturn(Optional.of(gasto));
        when(metaRepo.findById(10)).thenReturn(Optional.of(meta));

        service.CrearTransaccionDTO(dto);

        assertEquals(60f, meta.getMontoActual());
        verify(repo).save(any());
        verify(metaService).validarCumplimientoDeMeta(meta);
    }

    @Test
    void crearTransaccion_metaGastoNoPermitido_deberiaLanzarError() {

        TransaccionDTO dto = new TransaccionDTO();
        dto.setMonto(200f);
        dto.setIdUsuario(1);
        dto.setIdMeta(10);
        dto.setTipoTransaccionId(1);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user));
        when(tipoTransaccionRepo.findById(1)).thenReturn(Optional.of(gasto));
        when(metaRepo.findById(10)).thenReturn(Optional.of(meta));

        assertThrows(IllegalArgumentException.class,
                () -> service.CrearTransaccionDTO(dto));
    }

    @Test
    void crearTransaccion_presupuestoGasto_deberiaSumar() {

        TransaccionDTO dto = new TransaccionDTO();
        dto.setMonto(50f);
        dto.setDescripcion("Compra comida");
        dto.setIdUsuario(1);
        dto.setPresupuestoId(20);
        dto.setTipoTransaccionId(1);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(user));
        when(tipoTransaccionRepo.findById(1)).thenReturn(Optional.of(gasto));
        when(presupuestoRepo.findById(20)).thenReturn(Optional.of(presupuesto));

        service.CrearTransaccionDTO(dto);

        assertEquals(350f, presupuesto.getMontoActual());
        verify(presupuestoService).evaluarEstadoPresupuesto(presupuesto);
        verify(presupuestoService).verificarPresupuestosCompletados(1);
    }

    @Test
    void editarTransaccion_deberiaActualizar() {

        Transaccion anterior = new Transaccion();
        anterior.setIdTransaccion(99);
        anterior.setUsuarioTransacciones(user);
        anterior.setMonto(100f);
        anterior.setDescripcion("Antes");

        EditarTransaccionDTO dto = new EditarTransaccionDTO();
        dto.setMonto(250f);
        dto.setDescripcion("Después");

        when(repo.findById(99)).thenReturn(Optional.of(anterior));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        Transaccion result = service.updateTransaccion(99, 1, dto);

        assertEquals(250f, result.getMonto());
        assertEquals("Después", result.getDescripcion());
    }


    @Test
    void eliminarTransaccion_meta_deberiaRestar() {

        MetaTransaccion rel = new MetaTransaccion();
        rel.setMeta(meta);

        Transaccion t = new Transaccion();
        t.setIdTransaccion(50);
        t.setUsuarioTransacciones(user);
        t.setMonto(30f);
        t.getMetaTransaccion().add(rel);

        when(repo.findById(50)).thenReturn(Optional.of(t));

        service.eliminarTransaccion(50, 1);

        assertEquals(70f, meta.getMontoActual());
        verify(metaRepo).save(meta);
        verify(repo).delete(t);
    }


    @Test
    void eliminarTransaccion_presupuesto_deberiaRestar() {

        Transaccion t = new Transaccion();
        t.setIdTransaccion(77);
        t.setMonto(50f);
        t.setUsuarioTransacciones(user);
        t.setTipoTransaccion(gasto);
        t.setPresupuesto(presupuesto);

        when(repo.findById(77)).thenReturn(Optional.of(t));

        service.eliminarTransaccion(77, 1);

        assertEquals(250f, presupuesto.getMontoActual());
        verify(presupuestoRepo).save(presupuesto);
        verify(repo).delete(t);
    }


    @Test
    void listarPorUsuario_deberiaRetornarLista() {

        when(repo.findByUsuarioTransacciones_Id(1))
                .thenReturn(List.of(new Transaccion()));

        List<Transaccion> lista = service.listarTransaccionesPorUsuario(1);

        assertEquals(1, lista.size());
    }

    @Test
    void listarPorPresupuesto_usuarioCorrecto() {

        when(presupuestoRepo.findById(20)).thenReturn(Optional.of(presupuesto));
        when(repo.findByPresupuesto_IdPresupuesto(20))
                .thenReturn(List.of(new Transaccion()));

        List<Transaccion> lista = service.listarTransaccionesPorPresupuesto(20, 1);

        assertEquals(1, lista.size());
    }
}

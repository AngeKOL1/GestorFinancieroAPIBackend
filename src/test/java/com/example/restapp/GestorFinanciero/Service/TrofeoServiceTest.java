package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.TrofeoEstadoDTO;
import com.example.restapp.GestorFinanciero.dto.TrofeosDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.Trofeos;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.models.UsuarioTrofeo;
import com.example.restapp.GestorFinanciero.repo.TrofeoRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioTrofeoRepo;
import com.example.restapp.GestorFinanciero.service.impl.TrofeoService;

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
class TrofeoServiceTest {

    @Mock
    private TrofeoRepo trofeoRepo;

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private UsuarioTrofeoRepo usuarioTrofeoRepo;

    @InjectMocks
    private TrofeoService trofeoService;

    private Trofeos t1, t2;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        t1 = new Trofeos();
        t1.setIdTrofeo(1);
        t1.setNombreTrofeo("Inicio");
        t1.setPrerequisito("Registrarse");
        t1.setXpRequerida(100);

        t2 = new Trofeos();
        t2.setIdTrofeo(2);
        t2.setNombreTrofeo("Maestro");
        t2.setPrerequisito("Completar 5 metas");
        t2.setXpRequerida(500);

        usuario = new Usuario();
        usuario.setId(10);
        usuario.setUsuarioTrofeo(new HashSet<>());
    }


    @Test
    void obtenerTrofeos_deberiaRetornarListaDTO() {

        when(trofeoRepo.findAll()).thenReturn(List.of(t1, t2));

        List<TrofeosDTO> lista = trofeoService.obtenerTrofeos();

        assertEquals(2, lista.size());
        assertEquals("Inicio", lista.get(0).getNombre());
        verify(trofeoRepo, times(1)).findAll();
    }


    @Test
    void obtenerUltimoTrofeoDTO_deberiaRetornarUltimoTrofeo() {

        UsuarioTrofeo ut = new UsuarioTrofeo();
        ut.setTrofeo(t2);
        ut.setFechaObtencionTrofeo(LocalDate.now());

        when(usuarioTrofeoRepo.findTopByUsuario_IdOrderByFechaObtencionTrofeoDesc(10))
                .thenReturn(Optional.of(ut));

        var dto = trofeoService.obtenerUltimoTrofeoDTO(10);

        assertEquals("Maestro", dto.getNombre());
        assertEquals(500, dto.getXp());
        verify(usuarioTrofeoRepo, times(1))
                .findTopByUsuario_IdOrderByFechaObtencionTrofeoDesc(10);
    }

    @Test
    void obtenerUltimoTrofeoDTO_deberiaLanzarExcepcionSiNoTieneTrofeos() {

        when(usuarioTrofeoRepo.findTopByUsuario_IdOrderByFechaObtencionTrofeoDesc(10))
                .thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> {
            trofeoService.obtenerUltimoTrofeoDTO(10);
        });
    }


    @Test
    void obtenerTrofeosConEstado_deberiaMarcarTrofeosObtenidos() {

        // Usuario tiene t1
        UsuarioTrofeo ut = new UsuarioTrofeo();
        ut.setTrofeo(t1);
        usuario.getUsuarioTrofeo().add(ut);

        when(usuarioRepo.findById(10)).thenReturn(Optional.of(usuario));
        when(trofeoRepo.findAll()).thenReturn(List.of(t1, t2));

        List<TrofeoEstadoDTO> lista = trofeoService.obtenerTrofeosConEstado(10);

        assertEquals(2, lista.size());

        assertTrue(lista.get(0).isObtenido());   
        assertFalse(lista.get(1).isObtenido());  

        verify(trofeoRepo, times(1)).findAll();
        verify(usuarioRepo, times(1)).findById(10);
    }

    @Test
    void obtenerTrofeosConEstado_deberiaLanzarExcepcionSiUsuarioNoExiste() {

        when(usuarioRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> {
            trofeoService.obtenerTrofeosConEstado(99);
        });
    }
}

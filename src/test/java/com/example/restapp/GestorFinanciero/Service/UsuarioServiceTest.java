package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.NivelUsuarioInfoDTO;
import com.example.restapp.GestorFinanciero.dto.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.*;
import com.example.restapp.GestorFinanciero.service.impl.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepo usuarioRepo;
    @Mock private RolRepo rolRepo;
    @Mock private NivelUsuarioRepo nivelUsuarioRepo;
    @Mock private TrofeoRepo trofeoRepo;
    @Mock private UsuarioTrofeoRepo usuarioTrofeoRepo;
    @Mock private MetaRepo metaRepo;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private NivelUsuario nivel1;
    private NivelUsuario nivel2;

    @BeforeEach
    void setup() {

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setXp(500);
        usuario.setUsuarioTrofeo(new HashSet<>());
        usuario.setMetas(new HashSet<>());
        usuario.setTransacciones(new ArrayList<>());
        usuario.setPresupuestos(new ArrayList<>());
        usuario.setMisCategoriasMetas(new ArrayList<>());

        nivel1 = new NivelUsuario();
        nivel1.setIdNivel(1);
        nivel1.setNivelActual(1);
        nivel1.setXpTotal(0);
        nivel1.setBanner("Bronce");

        nivel2 = new NivelUsuario();
        nivel2.setIdNivel(2);
        nivel2.setNivelActual(2);
        nivel2.setXpTotal(1000);
        nivel2.setBanner("Plata");
    }

    @Test
    void registrarUsuario_deberiaRegistrarCorrectamente() {

        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setCorreo("test@mail.com");
        dto.setNombre("Angelo");
        dto.setApellido("Muñoz");
        dto.setContrasena("1234");
        dto.setConfirmPassword("1234");

        usuario.setXp(0);  // ← IMPORTANTE para evitar NPE

        when(usuarioRepo.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("pwd");
        when(rolRepo.findByNombre("USUARIO")).thenReturn(Optional.of(new Rol()));
        when(nivelUsuarioRepo.findFirstByOrderByIdNivelAsc()).thenReturn(Optional.of(nivel1));
        when(trofeoRepo.findFirstByOrderByIdTrofeoAsc()).thenReturn(Optional.of(new Trofeos()));
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.registrarUsuario(dto);

        assertNotNull(result);
        verify(usuarioRepo, times(1)).save(any());
    }


    @Test
    void asignarNiveles_deberiaAsignarNivelCorrecto() {

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(nivelUsuarioRepo.findTopByXpTotalLessThanEqualOrderByXpTotalDesc(500))
                .thenReturn(Optional.of(nivel1));
        when(usuarioRepo.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.asignarNiveles(1);

        assertEquals(nivel1, result.getNivelUsuario());
        verify(usuarioRepo).save(usuario);
    }


    @Test
    void usuarioTieneTrofeo_trueSiExiste() {

        Trofeos t = new Trofeos();
        t.setIdTrofeo(10);

        UsuarioTrofeo ut = new UsuarioTrofeo();
        ut.setTrofeo(t);

        usuario.getUsuarioTrofeo().add(ut);

        assertTrue(usuarioService.usuarioTieneTrofeo(usuario, 10));
    }

    @Test
    void usuarioTieneTrofeo_falseSiNoExiste() {
        assertFalse(usuarioService.usuarioTieneTrofeo(usuario, 99));
    }


    @Test
    void asignarTrofeo_deberiaAsignarCuandoNoLoTiene() {

        Trofeos trofeo = new Trofeos();
        trofeo.setIdTrofeo(2);
        trofeo.setXpRequerida(200);

        when(trofeoRepo.findById(2)).thenReturn(Optional.of(trofeo));
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(nivelUsuarioRepo.findTopByXpTotalLessThanEqualOrderByXpTotalDesc(anyInt()))
                .thenReturn(Optional.of(nivel1));
        when(usuarioRepo.save(any())).thenReturn(usuario);

        usuarioService.asignarTrofeo(usuario, 2);

        assertEquals(700, usuario.getXp());
        verify(usuarioTrofeoRepo).save(any());
    }

    @Test
    void asignarTrofeo_noDebeAsignarSiYaLoTiene() {

        Trofeos trofeo = new Trofeos();
        trofeo.setIdTrofeo(2);

        UsuarioTrofeo ut = new UsuarioTrofeo();
        ut.setTrofeo(trofeo);

        usuario.getUsuarioTrofeo().add(ut);

        when(trofeoRepo.findById(2)).thenReturn(Optional.of(trofeo));

        usuarioService.asignarTrofeo(usuario, 2);

        verify(usuarioTrofeoRepo, never()).save(any());
    }


    @Test
    void verificarMetasEnCategoriasDiferentes_deberiaAsignarTrofeo() {

        when(metaRepo.contarCategoriasDistintasPorUsuario(1)).thenReturn(5L);
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));

        Trofeos trofeo = new Trofeos();
        trofeo.setIdTrofeo(6);
        trofeo.setXpRequerida(300); 

        when(trofeoRepo.findById(6)).thenReturn(Optional.of(trofeo));

        when(nivelUsuarioRepo.findTopByXpTotalLessThanEqualOrderByXpTotalDesc(anyInt()))
                .thenReturn(Optional.of(nivel1));

        when(usuarioRepo.save(any())).thenReturn(usuario);

        boolean r = usuarioService.verificarMetasEnCategoriasDiferentes(1);

        assertTrue(r);
        verify(usuarioTrofeoRepo, times(1)).save(any());
    }


    @Test
    void verificarMetasEnCategoriasDiferentes_noDebeAsignarTrofeo() {

        when(metaRepo.contarCategoriasDistintasPorUsuario(1)).thenReturn(2L);

        boolean r = usuarioService.verificarMetasEnCategoriasDiferentes(1);

        assertFalse(r);
    }


    @Test
    void obtenerInfoNivel_debeCalcularPorcentajeCorrecto() {

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(nivelUsuarioRepo.findTopByXpTotalLessThanEqualOrderByXpTotalDesc(500))
                .thenReturn(Optional.of(nivel1));
        when(nivelUsuarioRepo.findFirstByXpTotalGreaterThanOrderByXpTotalAsc(500))
                .thenReturn(Optional.of(nivel2));

        NivelUsuarioInfoDTO dto = usuarioService.obtenerInfoNivel(1);

        assertEquals(1, dto.getNivelActual());
        assertEquals("Bronce", dto.getBanner());
        assertEquals(500, dto.getXpActual());
        assertEquals(1000, dto.getXpNecesaria());
        assertTrue(dto.getPorcentaje() > 0);
    }
}

package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.CrearMetaDTO;
import com.example.restapp.GestorFinanciero.dto.EditarMetaDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.*;
import com.example.restapp.GestorFinanciero.service.impl.MetaService;
import com.example.restapp.GestorFinanciero.service.impl.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class MetaServiceTest {

    @Mock private MetaRepo repo;
    @Mock private UsuarioRepo usuarioRepo;
    @Mock private CategoriaMetaRepo categoriaRepo;
    @Mock private MisCategoriasMetaRepo misCategoriaRepo;
    @Mock private TipoMetaRepo tipoMetaRepo;
    @Mock private EstadoMetaRepo estadoMetaRepo;
    @Mock private UsuarioService usuarioService;

    @InjectMocks
    private MetaService metaService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setMetas(new HashSet<>());
    }

    // ------------------------------------------------------------------------------------------
    // ✔ TEST crearMetaDTO()
    // ------------------------------------------------------------------------------------------
    @Test
    void crearMetaDTO_debeCrearCorrectamente() {
        // ARRANGE
        CrearMetaDTO dto = new CrearMetaDTO();
        dto.setNombre("Ahorro Laptop");
        dto.setMontoObjetivo(1000.0);
        dto.setFechaFinal(LocalDate.now().plusDays(30));
        dto.setIdUsuario(1);
        dto.setNombreCategoria("Tecnología");
        dto.setNombreTipoMeta("Ahorro");
        dto.setNombreEstadoMeta("Activo");

        CategoriaMeta cat = new CategoriaMeta();
        cat.setIdCategoriaMeta(1);
        cat.setNombre("Tecnología");

        TipoMeta tipoMeta = new TipoMeta();
        tipoMeta.setIdTipoMeta(1);
        tipoMeta.setNombreTipoMeta("Ahorro");

        EstadoMeta estado = new EstadoMeta();
        estado.setIdEstadoMeta(1);
        estado.setNombreEstadoMeta("Activo");

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(categoriaRepo.findByNombre("Tecnología")).thenReturn(Optional.of(cat));
        when(tipoMetaRepo.findByNombreTipoMeta("Ahorro")).thenReturn(Optional.of(tipoMeta));
        when(estadoMetaRepo.findByNombreEstadoMeta("Activo")).thenReturn(Optional.of(estado));
        when(repo.save(any(Meta.class))).thenAnswer(i -> i.getArgument(0));


        Meta meta = metaService.crearMetaDTO(dto);

        // ASSERT
        assertNotNull(meta);
        assertEquals("Ahorro Laptop", meta.getNombre());
        assertEquals(1000.0, meta.getMontoObjetivo());
        verify(repo).save(any(Meta.class));
    }

    @Test
    void crearMetaDTO_debeFallarSiNoExisteUsuario() {
        CrearMetaDTO dto = new CrearMetaDTO();
        dto.setIdUsuario(1);

        when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> metaService.crearMetaDTO(dto));
    }

    @Test
    void crearMetaDTO_debeFallarSiNoSeSeleccionaNingunaCategoria() {
        CrearMetaDTO dto = new CrearMetaDTO();
        dto.setIdUsuario(1);
        dto.setMontoObjetivo(200.0);
        dto.setFechaFinal(LocalDate.now().plusDays(5));

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class, () -> metaService.crearMetaDTO(dto));
    }


    @Test
    void asignarXpPorMeta_debeDar100SiEsPrimeraMeta() {

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setXp(0);

        // primera meta → SIN metas
        usuario.setMetas(Collections.emptySet());

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));

        Integer xp = metaService.asignarXpPorMeta(1);

        assertEquals(100, xp);
        assertEquals(100, usuario.getXp());
    }



    @Test
    void asignarXpPorMeta_debeDar250SiNoEsPrimeraMeta() {

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setXp(0);

        // NO es primera meta → debe tener 1 meta
        Meta meta = new Meta();
        usuario.setMetas(Set.of(meta));

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));

        Integer xp = metaService.asignarXpPorMeta(1);

        assertEquals(250, xp);
        assertEquals(250, usuario.getXp());
    }




    @Test
    void validarCumplimientoDeMeta_debeAsignarTrofeoYActualizarEstado() {

        Meta meta = new Meta();
        meta.setMontoActual(1000.0);
        meta.setMontoObjetivo(800.0);
        meta.setFechaInicial(LocalDate.now().minusDays(3));
        meta.setFechaFinal(LocalDate.now().plusDays(3));
        meta.setUsuarioMetas(usuario);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(estadoMetaRepo.findById(1)).thenReturn(Optional.of(new EstadoMeta()));

        metaService.validarCumplimientoDeMeta(meta);

        verify(usuarioService).asignarTrofeo(usuario, 3);
    }


    @Test
    void verificarCumplimientoRapido_debeAsignarTrofeoPorRapidez() {
        Meta meta = new Meta();
        meta.setNombre("Meta rápida");
        meta.setFechaInicial(LocalDate.now().minusDays(3));
        meta.setMontoActual(500.0);
        meta.setMontoObjetivo(300.0);
        meta.setUsuarioMetas(usuario);

        when(usuarioService.usuarioTieneTrofeo(usuario, 13)).thenReturn(false);

        metaService.verificarCumplimientoRapido(meta);

        verify(usuarioService).asignarTrofeo(usuario, 13);
    }

    @Test
    void verificarCumplimientoRapido_noDebeAsignarSiNoCumpleObjetivo() {
        Meta meta = new Meta();
        meta.setMontoActual(100.00);
        meta.setMontoObjetivo(300.00);

        metaService.verificarCumplimientoRapido(meta);

        verify(usuarioService, never()).asignarTrofeo(any(), anyInt());
    }

    @Test
    void listarTransaccionesPorMeta_debeListarCorrectamente() {
        Meta meta = new Meta();
        meta.setIdMeta(10);
        meta.setUsuarioMetas(usuario);

        when(repo.findById(10)).thenReturn(Optional.of(meta));
        when(repo.findByMetaId(10)).thenReturn(List.of(new Transaccion()));

        List<Transaccion> lista = metaService.listarTransaccionesPorMeta(10, 1);

        assertEquals(1, lista.size());
    }

    @Test
    void listarTransaccionesPorMeta_noDebePermitirUsuarioAjeno() {
        Meta meta = new Meta();
        meta.setIdMeta(10);
        Usuario otro = new Usuario();
        otro.setId(99);
        meta.setUsuarioMetas(otro);

        when(repo.findById(10)).thenReturn(Optional.of(meta));

        assertThrows(IllegalArgumentException.class,
                () -> metaService.listarTransaccionesPorMeta(10, 1));
    }


    @Test
    void editarMeta_debeActualizarCorrectamente() {

        Meta meta = new Meta();
        meta.setIdMeta(10);
        meta.setUsuarioMetas(usuario);
        meta.setMontoActual(100.0);
        meta.setFechaInicial(LocalDate.now());

        EditarMetaDTO dto = new EditarMetaDTO();
        dto.setNombre("Nueva Meta");
        dto.setFechaFinal(LocalDate.now().plusDays(10));
        dto.setMontoObjetivo(300.0);

        when(repo.findById(10)).thenReturn(Optional.of(meta));
        when(repo.save(any(Meta.class))).thenAnswer(i -> i.getArgument(0));

        Meta result = metaService.editarMeta(10, 1, dto);

        assertEquals("Nueva Meta", result.getNombre());
        assertEquals(300.0, result.getMontoObjetivo());
    }

    @Test
    void editarMeta_noDebeActualizarSiUsuarioNoEsDueno() {

        Meta meta = new Meta();
        meta.setIdMeta(10);
        Usuario otro = new Usuario();
        otro.setId(99);
        meta.setUsuarioMetas(otro);

        when(repo.findById(10)).thenReturn(Optional.of(meta));

        assertThrows(IllegalArgumentException.class,
                () -> metaService.editarMeta(10, 1, new EditarMetaDTO()));
    }

    @Test
    void eliminarMeta_debeEliminarCorrectamente() {

        Meta meta = new Meta();
        meta.setIdMeta(10);
        meta.setUsuarioMetas(usuario);

        when(repo.findById(10)).thenReturn(Optional.of(meta));

        metaService.eliminarMeta(10, 1);

        verify(repo).delete(meta);
    }
}

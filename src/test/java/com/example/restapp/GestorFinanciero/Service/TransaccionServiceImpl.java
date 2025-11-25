package com.example.restapp.GestorFinanciero.Service;

import com.example.restapp.GestorFinanciero.DTO.TransaccionDTO;
import com.example.restapp.GestorFinanciero.models.Meta;
import com.example.restapp.GestorFinanciero.models.TipoTransaccion;
import com.example.restapp.GestorFinanciero.models.Transaccion;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.MetaRepo;
import com.example.restapp.GestorFinanciero.repo.TipoTransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.TransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.impl.TransaccionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransaccionServiceImplTest {

    @Mock
    private UsuarioRepo usuarioRepo;
    @Mock
    private TipoTransaccionRepo tipoTransaccionRepo;
    @Mock
    private MetaRepo metaRepo;
    @Mock
    private TransaccionRepo repo;

    @InjectMocks
    private TransaccionService transaccionService;

    private TransaccionDTO dto;
    private Usuario usuario;
    private TipoTransaccion tipoTransaccion;
    private Meta meta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1);

        tipoTransaccion = new TipoTransaccion();
        tipoTransaccion.setIdTipoTransaccion(2);
        
        meta = new Meta();
        meta.setIdMeta(3);
        meta.setUsuarioMetas(usuario);

        dto = new TransaccionDTO();
        dto.setMonto(100f);
        dto.setDescripcion("Compra");
        dto.setIdUsuario(1);
        dto.setTipoTransaccionId(2);
        dto.setIdMeta(3);
    }

    @Test
    void testCrearTransaccionDTO_Success() throws Exception {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(tipoTransaccionRepo.findById(2)).thenReturn(Optional.of(tipoTransaccion));
        when(metaRepo.findById(3)).thenReturn(Optional.of(meta));
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaccion resultado = transaccionService.CrearTransaccionDTO(dto);

        assertNotNull(resultado);
        assertEquals(dto.getMonto(), resultado.getMonto());
        assertEquals(dto.getDescripcion(), resultado.getDescripcion());
        assertEquals(LocalDate.now(), resultado.getFechaTransaccion(), "La fecha de la transacción debe ser hoy");
        assertEquals(usuario, resultado.getUsuarioTransacciones());
        assertEquals(tipoTransaccion, resultado.getTipoTransaccion());
        verify(repo, times(1)).save(any(Transaccion.class));
    }

    @Test
    void testCrearTransaccionDTO_UsuarioNoEncontrado() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            transaccionService.CrearTransaccionDTO(dto);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void testCrearTransaccionDTO_TipoTransaccionNoEncontrado() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(tipoTransaccionRepo.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            transaccionService.CrearTransaccionDTO(dto);
        });

        assertEquals("Tipo de transacción no encontrado", exception.getMessage());
    }

    @Test
    void testCrearTransaccionDTO_UsuarioNoAutorizado() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(99);
        meta.setUsuarioMetas(otroUsuario);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(tipoTransaccionRepo.findById(2)).thenReturn(Optional.of(tipoTransaccion));
        when(metaRepo.findById(3)).thenReturn(Optional.of(meta));

        Exception exception = assertThrows(Exception.class, () -> {
            transaccionService.CrearTransaccionDTO(dto);
        });

        assertEquals("Usuario no autorizado para usar esta meta", exception.getMessage());
    }
}

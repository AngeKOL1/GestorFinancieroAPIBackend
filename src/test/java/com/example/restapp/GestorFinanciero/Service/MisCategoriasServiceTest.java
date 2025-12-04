package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.MisCategoriasDTO;
import com.example.restapp.GestorFinanciero.dto.VerMisCategoriasDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.MisCategoriasMetas;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.MisCategoriasMetaRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.impl.MisCategoriasService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MisCategoriasServiceTest {

    @Mock
    private MisCategoriasMetaRepo repo;

    @Mock
    private UsuarioRepo usuarioRepo;

    @InjectMocks
    private MisCategoriasService service;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setMisCategoriasMetas(new ArrayList<>());
    }

    @Test
    void crearMiCategoria_debeCrearCorrectamente() {
        // Arrange
        MisCategoriasDTO dto = new MisCategoriasDTO();
        dto.setNombre("Viajes");
        dto.setDescripcion("Ahorro para vacaciones");

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(repo.existsByNombreAndUsuario_Id("Viajes", 1)).thenReturn(false);
        when(repo.save(any(MisCategoriasMetas.class))).thenAnswer(invocation -> {
            MisCategoriasMetas saved = invocation.getArgument(0);
            saved.setIdMisCategoriasMetas(10); 
            return saved;
        });

        // Act
        MisCategoriasMetas categoria = service.crearMiCategoria(dto, 1);

        // Assert
        assertNotNull(categoria);
        assertEquals("Viajes", categoria.getNombre());
        assertEquals("Ahorro para vacaciones", categoria.getDescripcion());
        assertEquals(1, usuario.getMisCategoriasMetas().size());

        verify(repo).save(any(MisCategoriasMetas.class));
    }

    @Test
    void crearMiCategoria_debeFallarSiUsuarioNoExiste() {
        // Arrange
        when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ModelNotFoundException.class,
                () -> service.crearMiCategoria(new MisCategoriasDTO(), 1));
    }

    @Test
    void crearMiCategoria_noDebeCrearSiNombreDuplicado() {
        // Arrange
        MisCategoriasDTO dto = new MisCategoriasDTO();
        dto.setNombre("Gym");

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(repo.existsByNombreAndUsuario_Id("Gym", 1)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.crearMiCategoria(dto, 1));
    }

    @Test
    void listarMisCategorias_debeListarCorrectamente() {
        // Arrange
        MisCategoriasMetas c1 = new MisCategoriasMetas();
        c1.setNombre("Ahorro");

        MisCategoriasMetas c2 = new MisCategoriasMetas();
        c2.setNombre("Comida");

        usuario.getMisCategoriasMetas().add(c1);
        usuario.getMisCategoriasMetas().add(c2);

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));

        // Act
        List<VerMisCategoriasDTO> lista = service.listarMisCategorias(1);

        // Assert
        assertEquals(2, lista.size());
        assertEquals("Ahorro", lista.get(0).getNombre());
        assertEquals("Comida", lista.get(1).getNombre());
    }

    @Test
    void listarMisCategorias_debeFallarSiUsuarioNoExiste() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class,
                () -> service.listarMisCategorias(1));
    }
}

package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.CategoriaMetaDTO;
import com.example.restapp.GestorFinanciero.models.CategoriaMeta;
import com.example.restapp.GestorFinanciero.repo.CategoriaMetaRepo;
import com.example.restapp.GestorFinanciero.service.impl.CategoriaMetaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaMetaServiceTest {

    @Mock
    private CategoriaMetaRepo repo;

    @InjectMocks
    private CategoriaMetaService service;

    @Test
    void listaCategoriasMetaPorNombre_debeRetornarDTOCorrectamente() {

        // ---------- ARRANGE ----------
        CategoriaMeta c1 = new CategoriaMeta();
        c1.setNombre("Salud");

        CategoriaMeta c2 = new CategoriaMeta();
        c2.setNombre("Educación");

        when(repo.findAll()).thenReturn(List.of(c1, c2));

        // ---------- ACT ----------
        List<CategoriaMetaDTO> resultado = service.listaCategoriasMetaPorNombre();

        // ---------- ASSERT ----------
        assertEquals(2, resultado.size());
        assertEquals("Salud", resultado.get(0).getNombreCategoria());
        assertEquals("Educación", resultado.get(1).getNombreCategoria());

        verify(repo, times(1)).findAll();
    }
}

package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.EstadoMetaDTO;
import com.example.restapp.GestorFinanciero.models.EstadoMeta;
import com.example.restapp.GestorFinanciero.repo.EstadoMetaRepo;
import com.example.restapp.GestorFinanciero.service.impl.EstadoMetaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoMetaServiceTest {

    @Mock
    private EstadoMetaRepo repo;

    @InjectMocks
    private EstadoMetaService service;

    @Test
    void listarNombresEstadoMeta_debeRetornarDTOCorrectamente() {

        EstadoMeta e1 = new EstadoMeta();
        e1.setNombreEstadoMeta("Pendiente");

        EstadoMeta e2 = new EstadoMeta();
        e2.setNombreEstadoMeta("Completada");

        when(repo.findAll()).thenReturn(List.of(e1, e2));

        List<EstadoMetaDTO> resultado = service.listarNombresEstadoMeta();

        assertEquals(2, resultado.size());
        assertEquals("Pendiente", resultado.get(0).getNombreEstadoMeta());
        assertEquals("Completada", resultado.get(1).getNombreEstadoMeta());

        verify(repo, times(1)).findAll();
    }
}

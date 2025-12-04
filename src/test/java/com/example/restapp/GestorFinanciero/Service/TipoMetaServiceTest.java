package com.example.restapp.GestorFinanciero.service;

import com.example.restapp.GestorFinanciero.dto.TipoMetaDTO;
import com.example.restapp.GestorFinanciero.models.TipoMeta;
import com.example.restapp.GestorFinanciero.repo.TipoMetaRepo;
import com.example.restapp.GestorFinanciero.service.impl.TipoMetaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoMetaServiceTest {

    @Mock
    private TipoMetaRepo repo;

    @InjectMocks
    private TipoMetaService service;

    @Test
    void listarNombresTipoMeta_deberiaRetornarSoloNombres() {

        // ARRANGE
        TipoMeta ahorro = new TipoMeta();
        ahorro.setIdTipoMeta(1);
        ahorro.setNombreTipoMeta("Ahorro");

        TipoMeta inversion = new TipoMeta();
        inversion.setIdTipoMeta(2);
        inversion.setNombreTipoMeta("Inversión");

        when(repo.findAll()).thenReturn(List.of(ahorro, inversion));

        // ACT
        List<TipoMetaDTO> resultado = service.listarNombresTipoMeta();

        // ASSERT
        assertEquals(2, resultado.size());
        assertEquals("Ahorro", resultado.get(0).getNombreTipoMeta());
        assertEquals("Inversión", resultado.get(1).getNombreTipoMeta());

        verify(repo, times(1)).findAll();
    }
}

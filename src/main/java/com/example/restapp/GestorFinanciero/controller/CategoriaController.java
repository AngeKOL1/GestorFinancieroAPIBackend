package com.example.restapp.GestorFinanciero.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapp.GestorFinanciero.dto.CategoriaMetaDTO;
import com.example.restapp.GestorFinanciero.dto.EstadoMetaDTO;
import com.example.restapp.GestorFinanciero.dto.TipoMetaDTO;
import com.example.restapp.GestorFinanciero.service.ICategoriaMetaService;
import com.example.restapp.GestorFinanciero.service.IEstadoMetaService;
import com.example.restapp.GestorFinanciero.service.ITipoMetaService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final ICategoriaMetaService service;
    private final ITipoMetaService tipoMetaService;
    private final IEstadoMetaService estadoMetaService;
    @GetMapping("metas")
    public ResponseEntity<List<CategoriaMetaDTO>> obtenerNombreCatMetas () {

        List<CategoriaMetaDTO> lista = service.listaCategoriasMetaPorNombre();

        return ResponseEntity.ok(lista);
    }
    @GetMapping("tipo-metas")
    public ResponseEntity<List<TipoMetaDTO>> obtenerNombreTipoMetas() {

        List<TipoMetaDTO> lista = tipoMetaService.listarNombresTipoMeta();

        return ResponseEntity.ok(lista);
    }
    @GetMapping("estado-metas")
    public ResponseEntity<List<EstadoMetaDTO>> obtenerNombreEstadoMetas() {

        List<EstadoMetaDTO> lista = estadoMetaService.listarNombresEstadoMeta();

        return ResponseEntity.ok(lista);
    }
}

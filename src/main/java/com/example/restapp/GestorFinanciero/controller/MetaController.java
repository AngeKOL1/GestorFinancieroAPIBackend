package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.dto.CrearMetaDTO;
import com.example.restapp.GestorFinanciero.models.Meta;
import com.example.restapp.GestorFinanciero.models.Transaccion;
import com.example.restapp.GestorFinanciero.service.IMetaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/metas")
@RequiredArgsConstructor
public class MetaController {

    private final IMetaService service;


    @GetMapping("/misMetas")
    public ResponseEntity<List<Meta>> findAllForUser(HttpServletRequest request) {
        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");
        return ResponseEntity.ok(service.listarMetasPorUsuario(authenticatedUserId));
    }

    @PostMapping
    public ResponseEntity<Meta> crearMeta(@RequestBody CrearMetaDTO dto,
                                          HttpServletRequest request) {
        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");
        dto.setIdUsuario(authenticatedUserId);

        Meta meta = service.crearMetaDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(meta);
    }

    @GetMapping("/transacciones/meta/{idMeta}")
    public ResponseEntity<List<Transaccion>> listarPorMeta(
            @PathVariable Integer idMeta,
            HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("authenticatedUserId");

        return ResponseEntity.ok(service.listarTransaccionesPorMeta(idMeta, userId));
    }
}


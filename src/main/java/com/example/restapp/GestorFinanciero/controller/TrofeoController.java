package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.dto.TrofeoEstadoDTO;
import com.example.restapp.GestorFinanciero.dto.TrofeosDTO;
import com.example.restapp.GestorFinanciero.service.ITrofeoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trofeos")
@RequiredArgsConstructor
public class TrofeoController {
    private final ITrofeoService service;

    @GetMapping
    public ResponseEntity<List<TrofeosDTO>> obtenerTrofeos() {

        List<TrofeosDTO> lista = service.obtenerTrofeos();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/mi-lista-trofeos")
    public ResponseEntity<List<TrofeoEstadoDTO>> obtenerTrofeos(HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("authenticatedUserId");

        List<TrofeoEstadoDTO> lista = service.obtenerTrofeosConEstado(userId);

        return ResponseEntity.ok(lista);
    }


    @GetMapping("/usuario/ultimo-trofeo")
    public ResponseEntity<TrofeosDTO> obtenerUltimoTrofeo(HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("authenticatedUserId");

        TrofeosDTO dto = service.obtenerUltimoTrofeoDTO(userId);

        return ResponseEntity.ok(dto);
    }

}

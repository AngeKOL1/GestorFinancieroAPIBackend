package com.example.restapp.GestorFinanciero.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapp.GestorFinanciero.dto.MisCategoriasDTO;
import com.example.restapp.GestorFinanciero.dto.VerMisCategoriasDTO;
import com.example.restapp.GestorFinanciero.models.MisCategoriasMetas;
import com.example.restapp.GestorFinanciero.service.IMisCategoriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mis-metas")
@RequiredArgsConstructor
public class MisCategoriasController {
    private final IMisCategoriaService service;
    @GetMapping
    public ResponseEntity<List<VerMisCategoriasDTO>> findAll(HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("authenticatedUserId");
        List<VerMisCategoriasDTO> list = service.listarMisCategorias(userId);
        return ResponseEntity.ok(list);
    }
    @PostMapping
    public ResponseEntity<MisCategoriasMetas> crearCategoria(
            @RequestBody MisCategoriasDTO dto,
            HttpServletRequest request
    ) {
        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        MisCategoriasMetas categoria = service.crearMiCategoria(dto, authenticatedUserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

}

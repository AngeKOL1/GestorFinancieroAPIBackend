package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.models.NivelUsuario;
import com.example.restapp.GestorFinanciero.service.INivelUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/niveles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NivelesUsuarioController {
    private final INivelUsuarioService service;
    @GetMapping
    public ResponseEntity<List<NivelUsuario>> findAll() throws Exception{
        List<NivelUsuario> list = service.findAll();
        return ResponseEntity.ok(list);
    }
}

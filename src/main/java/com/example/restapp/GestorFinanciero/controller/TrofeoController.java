package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.models.Trofeos;
import com.example.restapp.GestorFinanciero.service.ITrofeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trofeos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TrofeoController {
    private final ITrofeoService service;
    @GetMapping
    public ResponseEntity<List<Trofeos>> findAll() throws Exception {
        return ResponseEntity.ok(service.findAll());
    }
}

package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.models.Rol;
import com.example.restapp.GestorFinanciero.service.IRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RolController {

    private final IRolService service;

    @GetMapping
    public ResponseEntity<List<Rol>> findAll() throws Exception {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Rol> save(@RequestBody Rol rol) throws Exception {
        Rol obj = service.save(rol);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getIdRol()).toUri();
        return ResponseEntity.created(location).body(obj);
    }
}




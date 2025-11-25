package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.DTO.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final IUsuarioService service;
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() throws Exception{
        List<Usuario> list = service.findAll();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable("id") Integer id) throws Exception{
        Usuario obj =  service.findById(id);
        return ResponseEntity.ok(obj);
    }
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO dto) {
        try {
            Usuario nuevo = service.registrarUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}

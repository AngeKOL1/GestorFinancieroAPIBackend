package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.DTO.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
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
    public ResponseEntity<Usuario> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO dto) {
        Usuario nuevo = service.registrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
    @PutMapping("/asignarNivel")
    public ResponseEntity<Usuario> asignarNivel(HttpServletRequest request) throws Exception {
        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        Usuario actualizado = service.asignarNiveles(authenticatedUserId);

        return ResponseEntity.ok(actualizado);
    }
    @GetMapping("/xp")
    public ResponseEntity<Integer> obtenerXpUsuario(HttpServletRequest request){
        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");
        return ResponseEntity.ok(service.obtenerXPUsuario(authenticatedUserId));
    }
}

package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.DTO.EditarTransaccionDTO;
import com.example.restapp.GestorFinanciero.DTO.TransaccionDTO;
import com.example.restapp.GestorFinanciero.models.Transaccion;
import com.example.restapp.GestorFinanciero.service.ITransaccionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/transacciones")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TransaccionController {

    private final ITransaccionService service;

    @GetMapping
    public ResponseEntity<List<Transaccion>> findAll() throws Exception {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Transaccion> CrearTransaccionDTO(@RequestBody TransaccionDTO dto,
                                            HttpServletRequest request) throws Exception {
            Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");
            dto.setIdUsuario(authenticatedUserId);

            Transaccion transaccion = service.CrearTransaccionDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccion);
    }
   @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTransaccion(
            @PathVariable Integer id,
            @RequestBody EditarTransaccionDTO transaccion,
            HttpServletRequest request) throws Exception {

        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        Transaccion transaccionActualizada = service.updateTransaccion(id, authenticatedUserId, transaccion);

        return ResponseEntity.ok(transaccionActualizada);
    }
}

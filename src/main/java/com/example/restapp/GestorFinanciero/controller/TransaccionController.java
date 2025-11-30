package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.dto.EditarTransaccionDTO;
import com.example.restapp.GestorFinanciero.dto.TransaccionDTO;
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
@RequiredArgsConstructor
public class TransaccionController {

    private final ITransaccionService service;

    @GetMapping
    public ResponseEntity<List<Transaccion>> listarTransaccionesUsuario(
            HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("authenticatedUserId");

        return ResponseEntity.ok(service.listarTransaccionesPorUsuario(userId));
    }


    @PostMapping
    public ResponseEntity<Transaccion> CrearTransaccionDTO(@RequestBody TransaccionDTO dto,
                                            HttpServletRequest request) {
            Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");
            dto.setIdUsuario(authenticatedUserId);

            Transaccion transaccion = service.CrearTransaccionDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccion);
    }
   @PutMapping("/{id}")
    public ResponseEntity<Transaccion> actualizarTransaccion(
            @PathVariable Integer id,
            @RequestBody EditarTransaccionDTO transaccion,
            HttpServletRequest request){

        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        Transaccion transaccionActualizada = service.updateTransaccion(id, authenticatedUserId, transaccion);

        return ResponseEntity.ok(transaccionActualizada);
    }
    @DeleteMapping("/transacciones/{id}")
    public ResponseEntity<String> eliminarTransaccion(
            @PathVariable Integer id,
            HttpServletRequest request) {

        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        service.eliminarTransaccion(id, authenticatedUserId);

        return ResponseEntity.ok("Transacción eliminada correctamente");
    }

    @GetMapping("/transacciones/presupuesto/{idPresupuesto}")
    public ResponseEntity<List<Transaccion>> listarPorPresupuesto(
            @PathVariable Integer idPresupuesto,
            HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("authenticatedUserId");

        return ResponseEntity.ok(service.listarTransaccionesPorPresupuesto(idPresupuesto, userId));
    }



}

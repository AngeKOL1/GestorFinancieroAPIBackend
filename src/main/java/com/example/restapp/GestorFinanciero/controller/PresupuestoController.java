package com.example.restapp.GestorFinanciero.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapp.GestorFinanciero.dto.PresupuestoDTO;
import com.example.restapp.GestorFinanciero.dto.PresupuestoResumenDTO;
import com.example.restapp.GestorFinanciero.models.Presupuesto;
import com.example.restapp.GestorFinanciero.service.IPresupuestoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/presupuestos")
@RequiredArgsConstructor
public class PresupuestoController {

    private final IPresupuestoService presupuestoService;

    @PostMapping
    public ResponseEntity<Presupuesto> crearPresupuesto(
            @RequestBody PresupuestoDTO dto,
            HttpServletRequest request
    ) {
        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        Presupuesto presupuesto = presupuestoService.crearPresupuestoDto(dto, authenticatedUserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(presupuesto);
    }
    @GetMapping("/mis-presupuestos")
    public ResponseEntity<List<PresupuestoResumenDTO>> listarMisPresupuestos(HttpServletRequest request) {

        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        List<PresupuestoResumenDTO> presupuestos = presupuestoService.listarPresupuestosPorUsuario(authenticatedUserId);

        return ResponseEntity.ok(presupuestos);
    }

}

package com.example.restapp.GestorFinanciero.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapp.GestorFinanciero.dto.ReporteDTO;
import com.example.restapp.GestorFinanciero.models.Reporte;
import com.example.restapp.GestorFinanciero.service.IReporteService;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final IReporteService reporteService;

    @PostMapping("/crear-reporte")
    public ResponseEntity<Reporte> asignarNivel(
            HttpServletRequest request, 
            @RequestBody ReporteDTO reporteDTO
    ){
        Integer authenticatedUserId = (Integer) request.getAttribute("authenticatedUserId");

        Reporte reporte = reporteService.generarReporte(reporteDTO, authenticatedUserId);

        return ResponseEntity.ok(reporte);
    }

}
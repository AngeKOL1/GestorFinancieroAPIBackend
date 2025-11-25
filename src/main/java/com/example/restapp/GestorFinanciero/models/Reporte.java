package com.example.restapp.GestorFinanciero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reportes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReporte;
    @Column(nullable = false, length = 50)
    private String tipo;
    @Column(nullable = false)
    private LocalDate fechaGeneracion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-reportes")
    private Usuario usuarioReporte;
}

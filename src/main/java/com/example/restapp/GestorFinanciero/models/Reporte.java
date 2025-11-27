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



    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = true, length = 300)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "meta_id", nullable = true)
    private Meta meta;

    @Column(nullable = true)
    private Double montoActual;

    @Column(nullable = true)
    private Double montoObjetivo;

    @Column(nullable = true)
    private Double porcentajeAvance;

    @Column(nullable = true)
    private LocalDate fechaInicio;

    @Column(nullable = true)
    private LocalDate fechaFin;

    @Column(nullable = true)
    private LocalDate fechaCumplimientoMeta;

    @Column(nullable = true, length = 20)
    private String estadoMeta;

    @Column(nullable = true, length = 50)
    private String categoriaMeta;

    @Column(nullable = true, length = 50)
    private String tipoMeta;

    @Column(nullable = true, length = 500)
    private String observaciones;
}

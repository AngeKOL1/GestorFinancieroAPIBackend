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
@Table(name = "presupuestos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPresupuesto;
    @Column(nullable = false)
    private Float montoMaximo;
    @Column(nullable = false)
    private Float montoEstablecido;
    @Column(nullable = false)
    private Float montoMinimo;
    @Column(nullable = false)
    private String periodo;
    @Column(nullable = false)
    private LocalDate fechaInicial;
    @Column(nullable = false)
    private LocalDate fechaFinal;


    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-presupuestos")
    private Usuario usuarioPresupuesto;

    @OneToOne
    @JoinColumn(name = "meta_id", nullable = false)
    private Meta meta;

    @ManyToOne
    @JoinColumn(name = "estadopresupuesto_id", nullable = false)
    private EstadoPresupuesto estadoPresupuesto;

}

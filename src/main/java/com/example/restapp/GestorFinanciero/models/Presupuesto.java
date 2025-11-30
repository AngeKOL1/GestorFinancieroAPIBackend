package com.example.restapp.GestorFinanciero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(nullable = false)
    private Float montoActual = 0f;



    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-presupuestos")
    private Usuario usuarioPresupuesto;

    @OneToOne
    @JoinColumn(name = "meta_id", nullable = true)
    private Meta meta;

    @ManyToOne
    @JoinColumn(name = "estadopresupuesto_id", nullable = false)
    @JsonManagedReference(value = "estado-presupuesto")
    private EstadoPresupuesto estadoPresupuesto;

    @OneToMany(mappedBy = "presupuesto", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference(value = "presupuesto-transacciones")
    private List<Transaccion> transacciones = new ArrayList<>();

}

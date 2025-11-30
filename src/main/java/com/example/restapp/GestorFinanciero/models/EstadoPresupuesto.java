package com.example.restapp.GestorFinanciero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="estadopresupuesto")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EstadoPresupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstadoPresupuesto;
    //[Por completar, abandonado, completado]
    @Column(nullable = false, length = 30)
    private String nombreEstadoPresupuesto;
    @Column(nullable = false, length = 200)
    private String descripcionEstadoPresupuesto;

    @OneToMany(mappedBy = "estadoPresupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "estado-presupuesto")
    private List<Presupuesto> presupuestos = new ArrayList<>();
}

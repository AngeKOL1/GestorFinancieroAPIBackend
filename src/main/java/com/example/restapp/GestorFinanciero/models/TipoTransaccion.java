package com.example.restapp.GestorFinanciero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tipoTransaccion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TipoTransaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoTransaccion;
    //Gasto o ingreso
    @Column(nullable = false, length = 10)
    private String nombreTipoTransaccion;
    @Column(nullable = false, length = 100)
    private String descripcionTipoTransaccion;

    @OneToMany(mappedBy = "tipoTransaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "tipoTransaccion-transacciones")
    private List<Transaccion> transacciones = new ArrayList<>();

}

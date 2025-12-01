package com.example.restapp.GestorFinanciero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipoTransaccion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TipoTransaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoTransaccion;

    @Column(nullable = false, length = 10)
    private String nombreTipoTransaccion; 

    @Column(nullable = false, length = 100)
    private String descripcionTipoTransaccion;

    // 🔥 Evitar recursión infinita Transaccion → Tipo → Transacciones → Tipo...
    @OneToMany(mappedBy = "tipoTransaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Transaccion> transacciones = new ArrayList<>();
}

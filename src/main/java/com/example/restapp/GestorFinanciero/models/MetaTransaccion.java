package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="meta_transaccion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetaTransaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMetaTransaccion;

    @ManyToOne
    @JoinColumn(name = "meta_id", nullable = false)
    @JsonBackReference
    private Meta meta;

    @ManyToOne
    @JoinColumn(name = "transaccion_id", nullable = false)
    @JsonBackReference
    private Transaccion transaccion;

}

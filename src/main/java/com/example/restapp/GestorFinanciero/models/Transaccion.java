package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacciones")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTransaccion;
    @Column(nullable = false)
    private Float Monto;
    @Column(nullable = false)
    private LocalDate fechaTransaccion;
    @Column(nullable = false,  length = 300 )
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-transacciones")
    private Usuario usuarioTransacciones;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "transaccion-metaTransaccion")
    private Set<MetaTransaccion> metaTransaccion = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "tipoTransaccion_id", nullable = false)
    @JsonBackReference(value = "tipoTransaccion-transacciones")
    private TipoTransaccion tipoTransaccion;
}

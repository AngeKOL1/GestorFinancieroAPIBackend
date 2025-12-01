package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(nullable = false, length = 300)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuarioTransacciones;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MetaTransaccion> metaTransaccion = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "tipoTransaccion_id", nullable = false)
    private TipoTransaccion tipoTransaccion;

    @ManyToOne
    @JoinColumn(name = "presupuesto_id")
    @JsonIgnore
    private Presupuesto presupuesto;


    @JsonProperty("tipoTransaccionId")
    public Integer getTipoTransaccionId() {
        return tipoTransaccion != null ? tipoTransaccion.getIdTipoTransaccion() : null;
    }

    @JsonProperty("tipoTransaccionNombre")
    public String getTipoTransaccionNombre() {
        return tipoTransaccion != null ? tipoTransaccion.getNombreTipoTransaccion() : null;
    }
}

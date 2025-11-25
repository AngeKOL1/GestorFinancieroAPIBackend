package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//Es como los roles de las metas
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipoMeta")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TipoMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idTipoMeta;
    //[Prioritario, A largo plazo, Opcional]
    @Column(nullable = false, length = 30)
    private String nombreTipoMeta;
    @Column(nullable = false, length = 100)
    private String descripcionTipoMeta;

    @OneToMany(mappedBy = "tipoMeta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Meta> meta = new ArrayList<>();
}

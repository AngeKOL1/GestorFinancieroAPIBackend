package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="estadoMeta")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EstadoMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstadoMeta;
    @Column(nullable = false, length = 20)
    //[Completada, En curso, Abandonada]
    private String nombreEstadoMeta;
    @Column(nullable = false, length = 100)
    private String descripcionEstadoMeta;

    @OneToMany(mappedBy = "estadoMeta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Meta> meta = new ArrayList<>();
}

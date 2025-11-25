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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categoriaMetas")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoriaMeta;
    @Column(nullable = false, length = 80)
    private String nombre;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(nullable = false)
    //Estado de uso
    private boolean estado;

    @OneToMany(mappedBy = "categoriaMetas", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Meta> metas = new ArrayList<>();
}

package com.example.restapp.GestorFinanciero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logros")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Logro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLogro;
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false)
    private String descripcion;
    //Ajustable a cambios
    @Column(nullable = false , length = 500)
    private String urlIcono;

    @OneToMany(mappedBy = "logro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UsuarioLogro> usuarioLogro = new HashSet<>();
}

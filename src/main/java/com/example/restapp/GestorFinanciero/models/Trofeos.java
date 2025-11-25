package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trofeos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idTrofeo")
public class Trofeos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idTrofeo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombreTrofeo;
    @Column(nullable = false, length = 200)
    private String prerequisito;
    @Column(nullable = false)
    private Integer xpRequerida;

    @OneToMany(mappedBy = "trofeo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioTrofeo> usuarioTrofeo = new HashSet<>();
}

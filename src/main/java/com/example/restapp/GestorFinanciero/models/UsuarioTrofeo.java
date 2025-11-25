package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuario_trofeo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUsuarioTrofeo")
public class UsuarioTrofeo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idUsuarioTrofeo;

    @Column(nullable = false)
    private LocalDate fechaObtencionTrofeo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-trofeos")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "trofeo_id", nullable = false)
    @JsonBackReference(value = "trofeo-usuarios")
    private Trofeos trofeo;
}

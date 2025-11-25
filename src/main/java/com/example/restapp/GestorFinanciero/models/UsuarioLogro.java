package com.example.restapp.GestorFinanciero.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarioLogro")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioLogro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuarioLogro;
    @Column(nullable = false)
    private LocalDate fechaLogro;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-logros")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "logro_id", nullable = false)
    @JsonBackReference(value = "logro-usuarios")
    private Logro logro;
}

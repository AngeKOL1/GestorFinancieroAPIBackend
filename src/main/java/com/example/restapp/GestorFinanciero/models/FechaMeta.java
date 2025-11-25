package com.example.restapp.GestorFinanciero.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="fechaMeta")
@EqualsAndHashCode
public class FechaMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFechaMeta;
    @Column(nullable = false, length = 20)
    private Integer mes;
    @Column(nullable = false, length = 20)
    private Integer dia;
    @Column(nullable = false, length = 20)
    private Integer anio;
    @Column(nullable = false)
    private LocalDate fechaTotal;
    @OneToOne
    @JoinColumn(name = "fechaMeta_id", nullable = false, unique = true)
    @JsonBackReference
    private Meta meta;
}

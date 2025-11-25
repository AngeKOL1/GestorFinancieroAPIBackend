package com.example.restapp.GestorFinanciero.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, length = 100)
    private String correo;
    @Column(nullable = false, length = 200)
    private String contrasena;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, length = 100)
    private String apellido;
    @Column(nullable = false)
    private LocalDate fechaRegistro;
    @Column(nullable = false)
    private LocalDate ultConexion;
    @Column(nullable = false)
    private Float xp;

    @ManyToOne
    @JoinColumn(name = "nivelUsuario_id", nullable = false)
    @JsonBackReference(value = "nivelUsuario-usuarios")
    private NivelUsuario nivelUsuario;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "usuario")
    @JsonManagedReference(value = "usuario-roles")
    private Set<UsuarioRol> usuarioRoles = new HashSet<>();

    @OneToMany(mappedBy = "usuarioTransacciones", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "usuario-transacciones")
    private List<Transaccion> transacciones = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioPresupuesto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "usuario-presupuestos")
    private List<Presupuesto> presupuestos = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioReporte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "usuario-reportes")
    private List<Reporte> reportes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-logros")
    private Set<UsuarioLogro> usuarioLogro = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-trofeos")
    private Set<UsuarioTrofeo> usuarioTrofeo = new HashSet<>();

    @OneToMany(mappedBy = "usuarioMetas", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-metas")
    private Set<Meta> metas = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-misCategoriasMetas")
    private List<MisCategoriasMetas> misCategoriasMetas = new ArrayList<>();
}

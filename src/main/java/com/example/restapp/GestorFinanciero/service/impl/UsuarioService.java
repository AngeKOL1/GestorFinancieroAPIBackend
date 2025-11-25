package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.DTO.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.*;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UsuarioService extends GenericService<Usuario, Integer> implements IUsuarioService {

    private final UsuarioRepo usuarioRepo;
    private final RolRepo rolRepo;
    private final NivelUsuarioRepo nivelUsuarioRepo;
    private final TrofeoRepo trofeoRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected IGenericRepo<Usuario, Integer> getRepo() {
        return usuarioRepo;
    }

    @Override
    public Usuario save(Usuario usuario) {


        if (usuario.getUsuarioRoles() == null) usuario.setUsuarioRoles(new HashSet<>());
        if (usuario.getTransacciones() == null) usuario.setTransacciones(new ArrayList<>());
        if (usuario.getPresupuestos() == null) usuario.setPresupuestos(new ArrayList<>());
        if (usuario.getReportes() == null) usuario.setReportes(new ArrayList<>());
        if (usuario.getUsuarioLogro() == null) usuario.setUsuarioLogro(new HashSet<>());
        if (usuario.getUsuarioTrofeo() == null) usuario.setUsuarioTrofeo(new HashSet<>());
        if (usuario.getMetas() == null) usuario.setMetas(new HashSet<>());
        if (usuario.getMisCategoriasMetas() == null) usuario.setMisCategoriasMetas(new ArrayList<>());

        Rol rolUsuario = rolRepo.findByNombre("USUARIO")
                .orElseThrow(() -> new RuntimeException("Rol 'USUARIO' no encontrado"));

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rolUsuario);
        usuarioRol.setActivo(true);
        usuarioRol.setFechaAsignacion(LocalDate.now());
        usuario.getUsuarioRoles().add(usuarioRol);

        NivelUsuario nivelInicial = nivelUsuarioRepo.findFirstByOrderByIdNivelAsc()
                .orElseThrow(() -> new RuntimeException("Nivel inicial no encontrado"));
        usuario.setNivelUsuario(nivelInicial);

        Trofeos trofeoInicial = trofeoRepo.findFirstByOrderByIdTrofeoAsc()
                .orElseThrow(() -> new RuntimeException("Trofeo inicial no encontrado"));

        UsuarioTrofeo usuarioTrofeo = new UsuarioTrofeo();
        usuarioTrofeo.setUsuario(usuario);
        usuarioTrofeo.setTrofeo(trofeoInicial);
        usuarioTrofeo.setFechaObtencionTrofeo(LocalDate.now());
        usuario.getUsuarioTrofeo().add(usuarioTrofeo);

        usuario.setFechaRegistro(LocalDate.now());
        usuario.setUltConexion(LocalDate.now());

        return usuarioRepo.save(usuario);
    }

   @Override
    public Usuario registrarUsuario(UsuarioRegistroDTO dto) {

        if (!dto.getContrasena().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        if (usuarioRepo.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();

        usuario.setCorreo(dto.getCorreo());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setUltConexion(LocalDate.now());

        // 🔐 Primero tomamos la contraseña del DTO y la encriptamos
        String passwordEncriptada = passwordEncoder.encode(dto.getContrasena());
        usuario.setContrasena(passwordEncriptada);

        usuario.setUsuarioRoles(new HashSet<>());
        usuario.setTransacciones(new ArrayList<>());
        usuario.setPresupuestos(new ArrayList<>());
        usuario.setReportes(new ArrayList<>());
        usuario.setUsuarioLogro(new HashSet<>());
        usuario.setUsuarioTrofeo(new HashSet<>());
        usuario.setMetas(new HashSet<>());
        usuario.setMisCategoriasMetas(new ArrayList<>());

        return save(usuario);
    }


}

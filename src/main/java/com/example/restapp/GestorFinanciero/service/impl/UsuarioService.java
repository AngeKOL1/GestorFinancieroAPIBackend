package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.dto.NivelUsuarioInfoDTO;
import com.example.restapp.GestorFinanciero.dto.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.*;
import com.example.restapp.GestorFinanciero.repo.*;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService extends GenericService<Usuario, Integer> implements IUsuarioService {

    private final UsuarioRepo usuarioRepo;
    private final RolRepo rolRepo;
    private final NivelUsuarioRepo nivelUsuarioRepo;
    private final TrofeoRepo trofeoRepo;
    private final UsuarioTrofeoRepo usuarioTrofeoRepo;
    private final MetaRepo metaRepo;

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
                .orElseThrow(() -> new ModelNotFoundException("Rol 'USUARIO' no encontrado"));

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rolUsuario);
        usuarioRol.setActivo(true);
        usuarioRol.setFechaAsignacion(LocalDate.now());
        usuario.getUsuarioRoles().add(usuarioRol);

        NivelUsuario nivelInicial = nivelUsuarioRepo.findFirstByOrderByIdNivelAsc()
                .orElseThrow(() -> new ModelNotFoundException("Nivel inicial no encontrado"));
        usuario.setNivelUsuario(nivelInicial);

        Trofeos trofeoInicial = trofeoRepo.findFirstByOrderByIdTrofeoAsc()
                .orElseThrow(() -> new ModelNotFoundException("Trofeo inicial no encontrado"));

        UsuarioTrofeo usuarioTrofeo = new UsuarioTrofeo();
        usuarioTrofeo.setUsuario(usuario);
        usuarioTrofeo.setTrofeo(trofeoInicial);
        usuarioTrofeo.setFechaObtencionTrofeo(LocalDate.now());
        usuario.getUsuarioTrofeo().add(usuarioTrofeo);

        usuario.setFechaRegistro(LocalDate.now());
        usuario.setUltConexion(LocalDate.now());
        usuario.setXp(usuario.getXp()+100);

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

    @Override
    @Transactional
    public Usuario asignarNiveles(Integer idUsuario) {

        Usuario user = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        Integer xpUsuario = user.getXp();

        NivelUsuario nivel = nivelUsuarioRepo
                .findTopByXpTotalLessThanEqualOrderByXpTotalDesc(xpUsuario)
                .orElseThrow(() -> new ModelNotFoundException("No se encontró un nivel para ese XP"));

        user.setNivelUsuario(nivel);

        return usuarioRepo.save(user);
    }


    @Override
    public Integer obtenerXPUsuario(Integer idUsuario){
        Usuario user= usuarioRepo.findById(idUsuario)
                      .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));
        
        return user.getXp();
    }

    @Override
    @Transactional
    public void asignarTrofeo(Usuario user, Integer idTrofeo) {

        Trofeos trofeo = trofeoRepo.findById(idTrofeo)
                .orElseThrow(() -> new ModelNotFoundException("Trofeo no encontrado"));

        boolean yaLoTiene = user.getUsuarioTrofeo().stream()
                .anyMatch(ut -> ut.getTrofeo().getIdTrofeo().equals(idTrofeo));

        if (yaLoTiene) {
            System.out.println("Usuario " + user.getId() + " ya tiene el trofeo " + idTrofeo);
            return;
        }

        UsuarioTrofeo userTrofeo = new UsuarioTrofeo();
        userTrofeo.setFechaObtencionTrofeo(LocalDate.now());
        userTrofeo.setUsuario(user);
        userTrofeo.setTrofeo(trofeo);

        user.getUsuarioTrofeo().add(userTrofeo);
        trofeo.getUsuarioTrofeo().add(userTrofeo);


        user.setXp(user.getXp() + trofeo.getXpRequerida());

        usuarioTrofeoRepo.save(userTrofeo);
        usuarioRepo.save(user);

        asignarNiveles(user.getId());
    }




    @Override
    @Transactional
    public boolean verificarMetasEnCategoriasDiferentes(Integer idUsuario) {

        long categoriasDistintas = metaRepo.contarCategoriasDistintasPorUsuario(idUsuario);

        System.out.println("Categorías distintas detectadas para usuario " + idUsuario + ": " + categoriasDistintas);

        if (categoriasDistintas >= 5) {

            Usuario usuario = usuarioRepo.findById(idUsuario)
                    .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

            asignarTrofeo(usuario, 6); 

            return true;
        }

        return false;
    }

    @Override
    public boolean usuarioTieneTrofeo(Usuario user, Integer idTrofeo) {
        return user.getUsuarioTrofeo().stream()
                .anyMatch(t -> t.getTrofeo().getIdTrofeo().equals(idTrofeo));
    }

    @Override
    public NivelUsuarioInfoDTO obtenerInfoNivel(Integer idUsuario) {

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        Integer xpUsuario = usuario.getXp();

        NivelUsuario nivelActual = nivelUsuarioRepo
                .findTopByXpTotalLessThanEqualOrderByXpTotalDesc(xpUsuario)
                .orElseThrow(() -> new ModelNotFoundException("No se encontró un nivel para ese XP"));

        Optional<NivelUsuario> optSiguiente = nivelUsuarioRepo
                .findFirstByXpTotalGreaterThanOrderByXpTotalAsc(xpUsuario);

        NivelUsuarioInfoDTO dto = new NivelUsuarioInfoDTO();

        dto.setNivelActual(nivelActual.getNivelActual());
        dto.setXpActual(xpUsuario);
        dto.setBanner(nivelActual.getBanner());

        if (optSiguiente.isEmpty()) {
            dto.setXpNecesaria(nivelActual.getXpTotal());
            dto.setXpRestante(0);
            dto.setPorcentaje(100);
            return dto;
        }

        NivelUsuario nivelSiguiente = optSiguiente.get();

        int xpNecesariaAbsoluta = nivelSiguiente.getXpTotal();
        dto.setXpNecesaria(xpNecesariaAbsoluta);

        int xpRestanteAbsoluta = xpNecesariaAbsoluta - xpUsuario;
        dto.setXpRestante(Math.max(xpRestanteAbsoluta, 0));

        int xpBaseNivelActual = nivelActual.getXpTotal();           
        int xpEnEsteNivel = xpUsuario - xpBaseNivelActual;          
        int xpRangoNivel = nivelSiguiente.getXpTotal() - xpBaseNivelActual;

        int porcentaje = 0;
        if (xpRangoNivel > 0) {
            porcentaje = (int) ( (xpEnEsteNivel * 100.0) / xpRangoNivel );
        }

        porcentaje = Math.max(0, Math.min(100, porcentaje));

        dto.setPorcentaje(porcentaje);

        return dto;
    }


}

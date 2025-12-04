package com.example.restapp.GestorFinanciero.repo;

import com.example.restapp.GestorFinanciero.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioTrofeoRepoTest {

    @Autowired
    private UsuarioTrofeoRepo repo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private TrofeoRepo trofeoRepo;

    @Autowired
    private NivelUsuarioRepo nivelUsuarioRepo;

    @Test
    void debeRetornarUltimoTrofeoPorFecha() {

        // Crear nivel obligatorio (NOT NULL)
        NivelUsuario nivel = new NivelUsuario();
        nivel.setNivelActual(1);
        nivel.setXpTotal(0);
        nivel.setIcono("icon");
        nivel.setVentajas("ninguna");
        nivel.setBanner("Bronce");
        nivel = nivelUsuarioRepo.save(nivel);

        // Crear usuario
        Usuario u = new Usuario();
        u.setCorreo("test@mail.com");
        u.setContrasena("1234");
        u.setNombre("Angelo");
        u.setApellido("M");
        u.setFechaRegistro(LocalDate.now());
        u.setUltConexion(LocalDate.now());
        u.setXp(0);
        u.setNivelUsuario(nivel); // 🔥 IMPORTANTE

        usuarioRepo.save(u);

        // Crear trofeos
        Trofeos t1 = new Trofeos();
        t1.setNombreTrofeo("Bronce");
        t1.setPrerequisito("x");
        t1.setXpRequerida(100);
        trofeoRepo.save(t1);

        Trofeos t2 = new Trofeos();
        t2.setNombreTrofeo("Plata");
        t2.setPrerequisito("y");
        t2.setXpRequerida(200);
        trofeoRepo.save(t2);

        // Asociaciones usuario–trofeo
        UsuarioTrofeo ut1 = new UsuarioTrofeo();
        ut1.setUsuario(u);
        ut1.setTrofeo(t1);
        ut1.setFechaObtencionTrofeo(LocalDate.now().minusDays(2));
        repo.save(ut1);

        UsuarioTrofeo ut2 = new UsuarioTrofeo();
        ut2.setUsuario(u);
        ut2.setTrofeo(t2);
        ut2.setFechaObtencionTrofeo(LocalDate.now());
        repo.save(ut2);

        // Test
        var result = repo.findTopByUsuario_IdOrderByFechaObtencionTrofeoDesc(u.getId());

        assertTrue(result.isPresent());
        assertEquals("Plata", result.get().getTrofeo().getNombreTrofeo());
    }
}

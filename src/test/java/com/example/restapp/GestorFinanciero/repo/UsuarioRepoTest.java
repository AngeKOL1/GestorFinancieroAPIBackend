package com.example.restapp.GestorFinanciero.repo;

import com.example.restapp.GestorFinanciero.models.NivelUsuario;
import com.example.restapp.GestorFinanciero.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "com.example.restapp.GestorFinanciero.models")
class UsuarioRepoTest {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private NivelUsuarioRepo nivelUsuarioRepo;

    private NivelUsuario nivelPorDefecto;

    @BeforeEach
    void setUp() {
        NivelUsuario nivel = new NivelUsuario();
        nivel.setNivelActual(1);              // NOT NULL
        nivel.setXpTotal(0);                  // NOT NULL
        nivel.setIcono("icono-test");         // NOT NULL
        nivel.setVentajas("ventajas test");   // NOT NULL
        nivel.setBanner("Bronce");            // NOT NULL

        nivelPorDefecto = nivelUsuarioRepo.save(nivel);
    }

    @Test
    void debeEncontrarUsuarioConRoles() {

        Usuario u = new Usuario();
        u.setCorreo("admin@mail.com");
        u.setContrasena("1234");
        u.setNombre("Admin");
        u.setApellido("Test");
        u.setFechaRegistro(LocalDate.now());
        u.setUltConexion(LocalDate.now());
        u.setXp(0);

        // Relación obligatoria con NivelUsuario
        u.setNivelUsuario(nivelPorDefecto);

        usuarioRepo.save(u);

        var result = usuarioRepo.findByCorreoFetchRoles("admin@mail.com");

        assertTrue(result.isPresent());
        assertEquals("admin@mail.com", result.get().getCorreo());
    }
}

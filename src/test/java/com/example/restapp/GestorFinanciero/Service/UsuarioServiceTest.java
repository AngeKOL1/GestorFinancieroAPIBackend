package com.example.restapp.GestorFinanciero.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.restapp.GestorFinanciero.service.impl.UsuarioService;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testEncriptacionYVerificacionPassword() {
        String passwordPlano = "MiClaveSegura123";

        // Encriptamos la contraseña
        String passwordEncriptado = passwordEncoder.encode(passwordPlano);

        // Aseguramos que NO sea igual al original
        assertNotEquals(passwordPlano, passwordEncriptado);

        // Verificamos que el encoder reconoce la clave correcta
        assertTrue(passwordEncoder.matches(passwordPlano, passwordEncriptado));

        // Verificamos que otra contraseña no coincida
        assertFalse(passwordEncoder.matches("otraClave", passwordEncriptado));
    }
}

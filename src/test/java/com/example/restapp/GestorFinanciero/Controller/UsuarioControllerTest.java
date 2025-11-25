package com.example.restapp.GestorFinanciero.Controller;


import com.example.restapp.GestorFinanciero.DTO.UsuarioRegistroDTO;
import com.example.restapp.GestorFinanciero.controller.UsuarioController;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private IUsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarUsuario_Exitoso() throws Exception {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setNombre("Angelo");
        dto.setCorreo("angelo@test.com");
        dto.setContrasena("12345");

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Angelo");
        usuario.setCorreo("angelo@test.com");
        usuario.setContrasena("encryptedPassword");

        when(usuarioService.registrarUsuario(dto)).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.registrarUsuario(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Usuario);
        verify(usuarioService, times(1)).registrarUsuario(dto);
    }

    @Test
    void testRegistrarUsuario_CorreoDuplicado() throws Exception {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setNombre("Angelo");
        dto.setCorreo("angelo@test.com");
        dto.setContrasena("12345");

        when(usuarioService.registrarUsuario(dto))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado"));

        ResponseEntity<?> response = usuarioController.registrarUsuario(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("El correo ya está registrado", body.get("error"));
    }

    @Test
    void testRegistrarUsuario_ErrorServidor() throws Exception {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setNombre("Angelo");
        dto.setCorreo("angelo@test.com");
        dto.setContrasena("12345");

        when(usuarioService.registrarUsuario(dto))
                .thenThrow(new RuntimeException("Error inesperado en el servidor"));

        ResponseEntity<?> response = usuarioController.registrarUsuario(dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Error inesperado en el servidor", body.get("error"));
    }
}

package com.example.restapp.GestorFinanciero.controller;

import com.example.restapp.GestorFinanciero.Security.JwtRequest;
import com.example.restapp.GestorFinanciero.Security.JwtResponse;
import com.example.restapp.GestorFinanciero.Security.JwtTokenUtil;
import com.example.restapp.GestorFinanciero.Security.JwtUserDetailsService;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final UsuarioRepo usuarioRepo;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest req) throws Exception {
        authenticate(req.getCorreo(), req.getPassword());

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(req.getCorreo());
        Usuario usuario = usuarioRepo.findByCorreo(req.getCorreo())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        final String token = jwtTokenUtil.generateToken(userDetails, usuario.getId());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String correo, String contrasena) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(correo, contrasena));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
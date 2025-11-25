package com.example.restapp.GestorFinanciero.Security;

import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioRepo repo;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = repo.findByCorreoFetchRoles(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<GrantedAuthority> authorities = usuario.getUsuarioRoles().stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getRol().getNombre()))
                .collect(Collectors.toList());

        return new User(usuario.getCorreo(), usuario.getContrasena(), authorities);
    }

    

}


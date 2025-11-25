package com.example.restapp.GestorFinanciero.repo;

import com.example.restapp.GestorFinanciero.models.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepo extends IGenericRepo<Usuario, Integer>{
    Optional<Usuario> findByCorreo(String correo);

    @Query("SELECT u FROM Usuario u " +
            "LEFT JOIN FETCH u.usuarioRoles ur " +
            "LEFT JOIN FETCH ur.rol " +
            "WHERE u.correo = :correo")
    Optional<Usuario> findByCorreoFetchRoles(@Param("correo") String correo);
}

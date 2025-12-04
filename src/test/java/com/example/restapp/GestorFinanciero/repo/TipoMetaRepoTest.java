package com.example.restapp.GestorFinanciero.repo;

import com.example.restapp.GestorFinanciero.models.TipoMeta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TipoMetaRepoTest {

    @Autowired
    private TipoMetaRepo repo;

    @Test
    void debeGuardarYEncontrarPorNombre() {

        TipoMeta tipo = new TipoMeta();
        tipo.setNombreTipoMeta("Ahorro");
        tipo.setDescripcionTipoMeta("Meta destinada al ahorro");  // 🔥 CAMPO OBLIGATORIO

        repo.save(tipo);

        var result = repo.findByNombreTipoMeta("Ahorro");

        assertTrue(result.isPresent());
        assertEquals("Ahorro", result.get().getNombreTipoMeta());
    }

    @Test
    void noDebeEncontrarSiNoExiste() {

        var result = repo.findByNombreTipoMeta("Inexistente");

        assertTrue(result.isEmpty());
    }
}

package com.sptech.SistemasDistribuidos;

import com.sptech.SistemasDistribuidos.Entities.CharacterData;
import com.sptech.SistemasDistribuidos.Scraping.CharacterScraping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SistemasDistribuidosApplicationTests {

    @Autowired
    private CharacterScraping characterScraping;

    @Test
    @DisplayName("Se o personagem for Noctis o seu apelido deve incluir 'Noct'")
    void noctisDeveTeroApelidoCorreto() throws IOException {

        // Arrange
        String nomePersonagem = "Noctis Lucis Caelum";
        String parteDoApelidoEsperado = "Noct";

        // ACT
        CharacterData personagem = characterScraping.scrape("Noctis Lucis Caelum");

        // ASSERT
        assertNotNull(personagem, "O personagem não deve ser nulo");
        assertNotNull(personagem.getApelidos(), "O apelido do Noctis não deve ser nulo");
        assertTrue(personagem.getApelidos().contains(parteDoApelidoEsperado), "O apelido do Noctis deve conter '" + parteDoApelidoEsperado + "'");
    }
}

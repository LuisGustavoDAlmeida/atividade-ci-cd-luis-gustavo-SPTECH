package com.sptech.SistemasDistribuidos.Controller;

import com.sptech.SistemasDistribuidos.Entities.CharacterData;
import com.sptech.SistemasDistribuidos.Scraping.CharacterScraping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FfxvController {
    @Autowired
    private CharacterScraping scraping;
    @GetMapping("/{nome}")
    public CharacterData getInfo(@PathVariable String nome) throws IOException {
        return scraping.scrape(nome);
    }
}

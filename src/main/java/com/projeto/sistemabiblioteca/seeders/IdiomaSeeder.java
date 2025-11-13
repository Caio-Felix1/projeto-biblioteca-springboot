package com.projeto.sistemabiblioteca.seeders;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;

import jakarta.annotation.PostConstruct;

@Component
@Order(5)
@Profile("dev")
public class IdiomaSeeder {

    private final IdiomaRepository idiomaRepository;

    public IdiomaSeeder(IdiomaRepository idiomaRepository) {
        this.idiomaRepository = idiomaRepository;
    }

    @PostConstruct
    @Transactional
    public void seed() {
        // Verifica se já existem idiomas
        long count = idiomaRepository.count();
        if (count > 0) {
            System.out.println("⚠️ Idiomas já existem, seed ignorado.");
            return;
        }

        // Lista de idiomas para popular
        List<Idioma> idiomas = List.of(
                new Idioma("Português"),
                new Idioma("Inglês"),
                new Idioma("Espanhol"),
                new Idioma("Francês"),
                new Idioma("Alemão"),
                new Idioma("Mandarim"),
                new Idioma("Italiano")
        );

        idiomaRepository.saveAll(idiomas);
        System.out.println("✅ Seed de Idiomas populado com sucesso!");
    }
}

package com.projeto.sistemabiblioteca.seeders;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;

@Component
@Profile("dev")
public class PaisSeeder implements CommandLineRunner {

    private final PaisRepository paisRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public PaisSeeder(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public void run(String... args) {
        if (paisRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                Pais pais = new Pais(faker.country().name());
                paisRepository.save(pais);
            }
            System.out.println("✅ 10 países falsos criados!");
        } else {
            System.out.println("⚠️ Países já existem, seed ignorado.");
        }
    }
}

package com.projeto.sistemabiblioteca.seeders;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;

@Component
@Profile("dev")
public class AutorSeeder implements CommandLineRunner {

    private final AutorRepository autorRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public AutorSeeder(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public void run(String... args) {
        if (autorRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                String nome = faker.book().author(); // gera nomes de autores falsos
                Autor autor = new Autor(nome);
                if (!autorRepository.existsByNome(nome)) {
                	autorRepository.save(autor);
                }
            }
            System.out.println("✅ 10 autores falsos criados!");
        } else {
            System.out.println("⚠️ Autores já existem, seed ignorado.");
        }
    }
}

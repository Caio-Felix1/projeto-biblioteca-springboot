package com.projeto.sistemabiblioteca.seeders;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;

@Component
@Profile("dev")
public class EditoraSeeder implements CommandLineRunner {

    private final EditoraRepository editoraRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public EditoraSeeder(EditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    @Override
    public void run(String... args) {
        if (editoraRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                // Gerar nomes de editoras fictícias
                String nomeEditora = faker.book().publisher(); // retorna nomes de editoras
                Editora editora = new Editora(nomeEditora);
                editoraRepository.save(editora);
            }
            System.out.println("✅ 10 editoras falsas criadas!");
        } else {
            System.out.println("⚠️ Editoras já existem, seed ignorado.");
        }
    }
}

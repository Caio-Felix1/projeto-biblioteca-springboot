package com.projeto.sistemabiblioteca.seeders;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CategoriaSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public CategoriaSeeder(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                // Você pode usar faker para gerar palavras aleatórias ou nomes de livros
                String nomeCategoria = faker.book().genre(); // retorna nomes de gêneros literários
                Categoria categoria = new Categoria(nomeCategoria);
                categoriaRepository.save(categoria);
            }
            System.out.println("✅ 10 categorias falsas criadas!");
        } else {
            System.out.println("⚠️ Categorias já existem, seed ignorado.");
        }
    }
}

package com.projeto.sistemabiblioteca.seeders;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;

@Component
@Profile("dev")
public class TituloSeeder implements CommandLineRunner {

    private final TituloRepository tituloRepository;
    private final CategoriaRepository categoriaRepository;
    private final AutorRepository autorRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));
    private final Random random = new Random();

    public TituloSeeder(TituloRepository tituloRepository, CategoriaRepository categoriaRepository, AutorRepository autorRepository) {
        this.tituloRepository = tituloRepository;
        this.categoriaRepository = categoriaRepository;
        this.autorRepository = autorRepository;
    }

    @Override
    public void run(String... args) {
        if (tituloRepository.count() == 0) {
            List<Categoria> categorias = categoriaRepository.findAll();
            List<Autor> autores = autorRepository.findAll();

            if (categorias.isEmpty() || autores.isEmpty()) {
                System.out.println("⚠️ Nenhuma categoria ou autor encontrada. Rode primeiro os seeds de Categoria e Autor.");
                return;
            }

            for (int i = 0; i < 10; i++) {
                Titulo titulo = new Titulo(
                        faker.book().title(),
                        faker.lorem().paragraph()
                );

                // Adiciona de 1 a 3 categorias aleatórias
                int qtdCategorias = 1 + random.nextInt(Math.min(3, categorias.size()));
                for (int j = 0; j < qtdCategorias; j++) {
                    Categoria categoria = categorias.get(random.nextInt(categorias.size()));
                    titulo.adicionarCategoria(categoria);
                }

                // Adiciona de 1 a 2 autores aleatórios
                int qtdAutores = 1 + random.nextInt(Math.min(2, autores.size()));
                for (int j = 0; j < qtdAutores; j++) {
                    Autor autor = autores.get(random.nextInt(autores.size()));
                    titulo.adicionarAutor(autor);
                }

                tituloRepository.save(titulo);
            }

            System.out.println("✅ 10 títulos falsos criados com categorias e autores!");
        } else {
            System.out.println("⚠️ Títulos já existem, seed ignorado.");
        }
    }
}

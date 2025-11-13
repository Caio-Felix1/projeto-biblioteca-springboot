package com.projeto.sistemabiblioteca.seeders;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;

@Component
@Order(6)
@Profile("dev")
public class EdicaoSeeder implements CommandLineRunner {

    private final EdicaoRepository edicaoRepository;
    private final TituloRepository tituloRepository;
    private final EditoraRepository editoraRepository;
    private final IdiomaRepository idiomaRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));
    private final Random random = new Random();

    public EdicaoSeeder(
            EdicaoRepository edicaoRepository,
            TituloRepository tituloRepository,
            EditoraRepository editoraRepository,
            IdiomaRepository idiomaRepository
    ) {
        this.edicaoRepository = edicaoRepository;
        this.tituloRepository = tituloRepository;
        this.editoraRepository = editoraRepository;
        this.idiomaRepository = idiomaRepository;
    }

    @Override
    public void run(String... args) {
        if (edicaoRepository.count() == 0) {

            List<Titulo> titulos = tituloRepository.findAll();
            List<Editora> editoras = editoraRepository.findAll();
            List<Idioma> idiomas = idiomaRepository.findAll();

            if (titulos.isEmpty() || editoras.isEmpty() || idiomas.isEmpty()) {
                System.out.println("⚠️ Certifique-se de rodar os seeds de Titulo, Editora e Idioma antes de Edicao.");
                return;
            }

            for (int i = 0; i < 10; i++) {
                Titulo titulo = titulos.get(random.nextInt(titulos.size()));
                Editora editora = editoras.get(random.nextInt(editoras.size()));
                Idioma idioma = idiomas.get(random.nextInt(idiomas.size()));

                Edicao edicao = new Edicao(
                		"Edição temporária", // alterar depois
                        TipoCapa.values()[random.nextInt(TipoCapa.values().length)],
                        50 + random.nextInt(451), // 50 a 500 páginas
                        TamanhoEdicao.values()[random.nextInt(TamanhoEdicao.values().length)],
                        ClassificacaoIndicativa.values()[random.nextInt(ClassificacaoIndicativa.values().length)],
                        LocalDate.now().minusYears(random.nextInt(50)), // Data de publicação até 50 anos atrás
                        null,
                        titulo,
                        editora,
                        idioma
                );

                edicaoRepository.save(edicao);
            }

            System.out.println("✅ 10 edições falsas criadas!");
        } else {
            System.out.println("⚠️ Edições já existem, seed ignorado.");
        }
    }
}

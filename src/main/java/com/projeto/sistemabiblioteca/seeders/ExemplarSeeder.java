package com.projeto.sistemabiblioteca.seeders;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class ExemplarSeeder implements CommandLineRunner {

    private final ExemplarRepository exemplarRepository;
    private final EdicaoRepository edicaoRepository;
    private final Random random = new Random();
    private final Faker faker = new Faker();

    public ExemplarSeeder(ExemplarRepository exemplarRepository, EdicaoRepository edicaoRepository) {
        this.exemplarRepository = exemplarRepository;
        this.edicaoRepository = edicaoRepository;
    }

    @Override
    public void run(String... args) {
        if (exemplarRepository.count() == 0) {
            List<Edicao> edicoes = edicaoRepository.findAll();

            if (edicoes.isEmpty()) {
                System.out.println("⚠️ É necessário criar edições antes de popular exemplares.");
                return;
            }

            for (int i = 0; i < 20; i++) {
                Edicao edicao = edicoes.get(random.nextInt(edicoes.size()));
                EstadoFisico estadoFisico = EstadoFisico.values()[random.nextInt(EstadoFisico.values().length)];

                Exemplar exemplar = new Exemplar(estadoFisico, edicao);
                exemplarRepository.save(exemplar);
            }

            System.out.println("✅ 20 exemplares criados!");
        } else {
            System.out.println("⚠️ Exemplares já existem, seed ignorado.");
        }
    }
}

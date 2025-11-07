package com.projeto.sistemabiblioteca.seeders;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;

@Component
@Profile("dev")
public class EstadoSeeder implements CommandLineRunner {

    private final EstadoRepository estadoRepository;
    private final PaisRepository paisRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public EstadoSeeder(EstadoRepository estadoRepository, PaisRepository paisRepository) {
        this.estadoRepository = estadoRepository;
        this.paisRepository = paisRepository;
    }

    @Override
    public void run(String... args) {
        if (estadoRepository.count() == 0) {
            List<Pais> paises = paisRepository.findAll();

            if (paises.isEmpty()) {
                System.out.println("⚠️ Nenhum país encontrado. Rode primeiro o seed de países.");
                return;
            }

            for (Pais pais : paises) {
                // Cria de 2 a 5 estados por país
                int quantidadeEstados = 2 + faker.number().numberBetween(0, 4);
                for (int i = 0; i < quantidadeEstados; i++) {
                    Estado estado = new Estado(faker.address().state(), pais);
                    if (!estadoRepository.existsByNome(estado.getNome())) {
                    	estadoRepository.save(estado);
                    }
                }
            }
            System.out.println("✅ Estados criados para cada país!");
        } else {
            System.out.println("⚠️ Estados já existem, seed ignorado.");
        }
    }
}

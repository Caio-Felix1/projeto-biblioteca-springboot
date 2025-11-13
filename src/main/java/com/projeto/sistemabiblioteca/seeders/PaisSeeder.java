package com.projeto.sistemabiblioteca.seeders;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;

@Component
@Order(8)
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
        	/*
            for (int i = 0; i < 10; i++) {
                Pais pais = new Pais(faker.country().name());
                if (!paisRepository.existsByNome(pais.getNome())) {
                	paisRepository.save(pais);
                }
            }
            System.out.println("✅ 10 países falsos criados!");
            */
        	
        	// criando apenas o país Brasil
        	Pais pais = new Pais("Brasil");
            if (!paisRepository.existsByNome(pais.getNome())) {
            	paisRepository.save(pais);
            }
            System.out.println("✅ Brasil foi criado!");
        } else {
            //System.out.println("⚠️ Países já existem, seed ignorado.");
        	System.out.println("⚠️ Brasil já foi criado. Seed ignorado.");
        }
    }
}

package com.projeto.sistemabiblioteca.seeders;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.repositories.EnderecoRepository;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;

@Component
@Profile("dev")
public class EnderecoSeeder implements CommandLineRunner {

    private final EnderecoRepository enderecoRepository;
    private final EstadoRepository estadoRepository;
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public EnderecoSeeder(EnderecoRepository enderecoRepository, EstadoRepository estadoRepository) {
        this.enderecoRepository = enderecoRepository;
        this.estadoRepository = estadoRepository;
    }

    @Override
    public void run(String... args) {
        if (enderecoRepository.count() == 0) {
            List<Estado> estados = estadoRepository.findAll();

            if (estados.isEmpty()) {
                System.out.println("⚠️ Nenhum estado encontrado. Rode primeiro o seed de estados.");
                return;
            }

            for (Estado estado : estados) {
                // Cria de 1 a 3 endereços por estado
                int quantidadeEnderecos = 1 + faker.number().numberBetween(0, 3);
                for (int i = 0; i < quantidadeEnderecos; i++) {
                    Endereco endereco = new Endereco(
                            faker.address().streetName(),
                            faker.address().buildingNumber(),
                            faker.bool().bool() ? faker.address().secondaryAddress() : "",
                            faker.address().cityName(),
                            faker.address().zipCode(),
                            faker.address().cityName(),
                            estado
                    );
                    enderecoRepository.save(endereco);
                }
            }

            System.out.println("✅ Endereços criados para cada estado!");
        } else {
            System.out.println("⚠️ Endereços já existem, seed ignorado.");
        }
    }
}

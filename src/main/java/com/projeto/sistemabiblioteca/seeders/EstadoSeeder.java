package com.projeto.sistemabiblioteca.seeders;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.config.TestConfig;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;

@Component
@Order(9)
@Profile("dev")
public class EstadoSeeder implements CommandLineRunner {

    //private final TestConfig testConfig;

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
            /*
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
            */
            
            // criando apenas os estados brasileiros
            Pais pais = paises.get(0);
            List<String> estados = List.of(
                    "Acre",
                    "Alagoas",
                    "Amapá",
                    "Amazonas",
                    "Bahia",
                    "Ceará",
                    "Distrito Federal",
                    "Espírito Santo",
                    "Goiás",
                    "Maranhão",
                    "Mato Grosso",
                    "Mato Grosso do Sul",
                    "Minas Gerais",
                    "Pará",
                    "Paraíba",
                    "Paraná",
                    "Pernambuco",
                    "Piauí",
                    "Rio de Janeiro",
                    "Rio Grande do Norte",
                    "Rio Grande do Sul",
                    "Rondônia",
                    "Roraima",
                    "Santa Catarina",
                    "São Paulo",
                    "Sergipe",
                    "Tocantins"
                );
            
            estados.forEach(nomeEstado -> {
                Estado estado = new Estado(nomeEstado, pais);
                if (!estadoRepository.existsByNome(estado.getNome())) {
                	estadoRepository.save(estado);
                }
            });
      
            System.out.println("✅ Estados brasileiros foram criados!");
        } else {
            System.out.println("⚠️ Estados já existem, seed ignorado.");
        }
    }
}

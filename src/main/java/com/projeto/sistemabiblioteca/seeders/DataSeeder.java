package com.projeto.sistemabiblioteca.seeders;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.*;
import com.projeto.sistemabiblioteca.entities.enums.*;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.Locale;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder; // agora final
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public DataSeeder(PessoaRepository pessoaRepository, PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.passwordEncoder = passwordEncoder; // injetado corretamente
    }

    @Override
    public void run(String... args) {
        if (pessoaRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                Cpf cpf = new Cpf(faker.number().digits(11));
                String numero = faker.number().digits(faker.bool().bool() ? 10 : 11);
                Telefone telefone = new Telefone(numero);
                String email = "usuario" + faker.number().digits(3) + "@exemplo.com";
                Email emailValido = new Email(email);
                
                if (!pessoaRepository.existsByCpfValor(cpf.getValor()) && !pessoaRepository.existsByEmailEndereco(emailValido.getEndereco())) {
                
	                String senhaFixa = "SenhaFixa123";
	                String senhaCodificada = passwordEncoder.encode(senhaFixa); // agora funciona
	
	                Pessoa pessoa = new Pessoa(
	                        faker.name().fullName(),
	                        cpf,
	                        faker.bool().bool() ? Sexo.MASCULINO : Sexo.FEMININO,
	                        faker.bool().bool() ? FuncaoUsuario.CLIENTE : FuncaoUsuario.ADMINISTRADOR,
	                        faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
	                        LocalDate.now(),
	                        telefone,
	                        emailValido,
	                        senhaCodificada,
	                        StatusConta.ATIVA,
	                        null
	                );
	
	                pessoaRepository.save(pessoa);
                }
            }
            System.out.println("✅ 10 pessoas falsas criadas!");
        } else {
            System.out.println("⚠️ Pessoas já existem, seed ignorado.");
        }
    }
}

package com.projeto.sistemabiblioteca.seeders;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.repositories.EnderecoRepository;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

@Component
@Order(11)
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final PasswordEncoder passwordEncoder; // agora final
    private final Faker faker = new Faker(new Locale("pt-BR"));

    public DataSeeder(PessoaRepository pessoaRepository, EnderecoRepository enderecoRepository, PasswordEncoder passwordEncoder) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
        this.passwordEncoder = passwordEncoder; // injetado corretamente
    }

    @Override
    public void run(String... args) {
    	List<Endereco> enderecos = enderecoRepository.findAll();
    	
        if (enderecos.isEmpty()) {
            System.out.println("⚠️ Nenhum endereço encontrado. Rode primeiro o seed de endereços.");
            return;
        }
        
        if (enderecos.size() < 30) {
            System.out.println("⚠️ Não há endereços suficiente. Rode primeiro o seed de endereços.");
            return;
        }
    	
        if (pessoaRepository.count() == 0) {
            for (int i = 0; i < 30; i++) {
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
	                        faker.options().option(Sexo.MASCULINO, Sexo.FEMININO, Sexo.OUTRO),
	                        faker.options().option(FuncaoUsuario.CLIENTE, FuncaoUsuario.BIBLIOTECARIO, FuncaoUsuario.ADMINISTRADOR),
	                        faker.date().birthday(18, 80).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
	                        LocalDate.now(),
	                        telefone,
	                        emailValido,
	                        senhaCodificada,
	                        StatusConta.ATIVA,
	                        enderecos.get(i)
	                );
	
	                pessoaRepository.save(pessoa);
                }
            }
            System.out.println("✅ 30 pessoas falsas criadas!");
        } else {
            System.out.println("⚠️ Pessoas já existem, seed ignorado.");
        }
    }
}

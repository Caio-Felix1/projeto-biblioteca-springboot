package com.projeto.sistemabiblioteca.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.services.EstadoService;
import com.projeto.sistemabiblioteca.services.PaisService;
import com.projeto.sistemabiblioteca.services.PessoaService;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private EstadoService estadoService;
	
	@Override
	public void run(String... args) throws Exception {
		
		Pais pais = new Pais("Brasil");
		
		Estado estado = new Estado("São Paulo", pais);
		
		paisService.inserir(pais);
		estadoService.inserir(estado);
		
		// testar aprovacao e rejeição de cadastro
		Pessoa p1 = new Pessoa("teste1", new Cpf("00000000000"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste1@gmail.com"), "123", StatusConta.EM_ANALISE_APROVACAO, null);
		Pessoa p2 = new Pessoa("teste2", new Cpf("11111111111"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste2@gmail.com"), "123", StatusConta.EM_ANALISE_APROVACAO, null);
		
		// testar solicitação de exclusão de conta e rejeitar ou inativar conta depois
		Pessoa p3 = new Pessoa("teste3", new Cpf("22222222222"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste3@gmail.com"), "123", StatusConta.ATIVA, null);
		Pessoa p4 = new Pessoa("teste4", new Cpf("33333333333"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste4@gmail.com"), "123", StatusConta.ATIVA, null);
		
		// testar busca por funcionários ou por adms
		Pessoa p5 = new Pessoa("teste5", new Cpf("44444444444"), Sexo.MASCULINO, FuncaoUsuario.BIBLIOTECARIO, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste5@gmail.com"), "123", StatusConta.ATIVA, null);
		Pessoa p6 = new Pessoa("teste6", new Cpf("55555555555"), Sexo.MASCULINO, FuncaoUsuario.ADMINISTRADOR, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste6@gmail.com"), "123", StatusConta.ATIVA, null);
		
		pessoaService.inserir(p1);
		pessoaService.inserir(p2);
		pessoaService.inserir(p3);
		pessoaService.inserir(p4);
		pessoaService.inserir(p5);
		pessoaService.inserir(p6);
	}
}

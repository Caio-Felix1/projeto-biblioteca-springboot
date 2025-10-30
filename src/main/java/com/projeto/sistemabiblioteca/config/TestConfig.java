package com.projeto.sistemabiblioteca.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.services.EnderecoService;
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
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		Pais pais1 = new Pais("Brasil");
		Pais pais2 = new Pais("Estados Unidos");
		
		Estado estado1 = new Estado("São Paulo", pais1);
		Estado estado2 = new Estado("Rio de Janeiro", pais1);
		
		paisService.inserir(pais1);
		paisService.inserir(pais2);
		estadoService.inserir(estado1);
		estadoService.inserir(estado2);
		
		Endereco endereco = new Endereco("rua teste", "1234", "complemento teste", "bairro teste", "00000000", "cidade teste", estado1);
		enderecoService.inserir(endereco);
		
		String senhaHash = passwordEncoder.encode("1234");
		
		// testar aprovacao e rejeição de cadastro
		Pessoa p1 = new Pessoa("teste1", new Cpf("00000000000"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste1@gmail.com"), senhaHash, StatusConta.EM_ANALISE_APROVACAO, null);
		Pessoa p2 = new Pessoa("teste2", new Cpf("11111111111"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste2@gmail.com"), senhaHash, StatusConta.EM_ANALISE_APROVACAO, endereco);
		
		// testar solicitação de exclusão de conta e rejeitar ou inativar conta depois
		Pessoa p3 = new Pessoa("teste3", new Cpf("22222222222"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste3@gmail.com"), senhaHash, StatusConta.ATIVA, null);
		Pessoa p4 = new Pessoa("teste4", new Cpf("33333333333"), Sexo.MASCULINO, FuncaoUsuario.CLIENTE, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste4@gmail.com"), senhaHash, StatusConta.ATIVA, endereco);
		
		// testar busca por funcionários ou por adms
		Pessoa p5 = new Pessoa("teste5", new Cpf("44444444444"), Sexo.MASCULINO, FuncaoUsuario.BIBLIOTECARIO, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste5@gmail.com"), senhaHash, StatusConta.ATIVA, endereco);
		Pessoa p6 = new Pessoa("teste6", new Cpf("55555555555"), Sexo.MASCULINO, FuncaoUsuario.ADMINISTRADOR, LocalDate.parse("2000-10-10"), LocalDate.now(), new Telefone("1234567891"), new Email("teste6@gmail.com"), senhaHash, StatusConta.ATIVA, null);
		
		pessoaService.inserir(p1);
		pessoaService.inserir(p2);
		pessoaService.inserir(p3);
		pessoaService.inserir(p4);
		pessoaService.inserir(p5);
		pessoaService.inserir(p6);
		
	}
}

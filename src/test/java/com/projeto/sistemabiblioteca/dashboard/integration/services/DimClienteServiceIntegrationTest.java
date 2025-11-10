package com.projeto.sistemabiblioteca.dashboard.integration.services;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCliente;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimClienteRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimClienteService;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.repositories.EnderecoRepository;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimClienteServiceIntegrationTest {
	
	@Autowired
	private DimClienteService dimClienteService;
	
	@Autowired
	private DimClienteRepository dimClienteRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Test
	void deveCriarUmNovoDimCliente() {
		Pais pais = new Pais("Brasil");
		
		paisRepository.save(pais);
		
		Estado estado = new Estado("São Paulo", pais);
		
		estadoRepository.save(estado);
		
		Endereco endereco = new Endereco(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				estado);
		
		enderecoRepository.save(endereco);
		
		Pessoa pessoa = new Pessoa(
				"Maria Joana", 
				new Cpf("11111111111"), 
				Sexo.FEMININO, 
				FuncaoUsuario.CLIENTE,
				LocalDate.of(1990, 10, 10), 
				LocalDate.of(2025, 10, 10),
				new Telefone("1234567891"), 
				new Email("maria@gmail.com"), 
				"senhaHash",
				StatusConta.ATIVA,
				endereco);
		
		pessoaRepository.save(pessoa);
		
		DimCliente dimClienteNovo = dimClienteService.atualizar(pessoa);
		
		Assertions.assertTrue(dimClienteNovo.getSurrogateKey() != null && dimClienteNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(pessoa.getIdPessoa(), dimClienteNovo.getIdNatural());
		Assertions.assertEquals(Sexo.FEMININO, dimClienteNovo.getSexo());
		Assertions.assertEquals(LocalDate.of(1990, 10, 10), dimClienteNovo.getDtNascimento());
		Assertions.assertEquals(StatusConta.ATIVA, dimClienteNovo.getStatusConta());
		Assertions.assertEquals("cidade teste", dimClienteNovo.getCidade());
		Assertions.assertEquals("São Paulo", dimClienteNovo.getEstado());
		Assertions.assertEquals("Brasil", dimClienteNovo.getPais());
	}
	
	@Test
	void deveAtualizarDimClienteJaExistente() {
		Pais pais1 = new Pais("Brasil");
		Pais pais2 = new Pais("Estados Unidos");
		
		paisRepository.save(pais1);
		paisRepository.save(pais2);
		
		Estado estado1 = new Estado("São Paulo", pais1);
		Estado estado2 = new Estado("Nova York", pais2);
		
		estadoRepository.save(estado1);
		estadoRepository.save(estado2);
		
		Endereco endereco = new Endereco(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				estado1);
		
		enderecoRepository.save(endereco);
		
		Pessoa pessoa = new Pessoa(
				"Maria Joana", 
				new Cpf("11111111111"), 
				Sexo.FEMININO, 
				FuncaoUsuario.CLIENTE,
				LocalDate.of(1990, 10, 10), 
				LocalDate.of(2025, 10, 10),
				new Telefone("1234567891"), 
				new Email("maria@gmail.com"), 
				"senhaHash",
				StatusConta.ATIVA,
				endereco);
		
		pessoaRepository.save(pessoa);
		
		DimCliente dimCliente = new DimCliente(pessoa);
		
		dimClienteRepository.save(dimCliente);
		
		pessoa.setSexo(Sexo.MASCULINO);
		pessoa.definirDataNascimento(LocalDate.of(2000, 12, 12), LocalDate.of(2025, 10, 10));
		pessoa.solicitarExclusaoConta();
		
		pessoa.getEndereco().setCidade("cidade alterada");
		pessoa.getEndereco().setEstado(estado2);
		
		DimCliente dimClienteAtualizado = dimClienteService.atualizar(pessoa); 
		
		Assertions.assertEquals(dimCliente.getSurrogateKey(), dimClienteAtualizado.getSurrogateKey());
		Assertions.assertEquals(pessoa.getIdPessoa(), dimClienteAtualizado.getIdNatural());
		Assertions.assertEquals(Sexo.MASCULINO, dimClienteAtualizado.getSexo());
		Assertions.assertEquals(LocalDate.of(2000, 12, 12), dimClienteAtualizado.getDtNascimento());
		Assertions.assertEquals(StatusConta.EM_ANALISE_EXCLUSAO, dimClienteAtualizado.getStatusConta());
		Assertions.assertEquals("cidade alterada", dimClienteAtualizado.getCidade());
		Assertions.assertEquals("Nova York", dimClienteAtualizado.getEstado());
		Assertions.assertEquals("Estados Unidos", dimClienteAtualizado.getPais());
	}
}

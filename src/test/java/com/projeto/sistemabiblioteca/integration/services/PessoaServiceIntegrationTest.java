package com.projeto.sistemabiblioteca.integration.services;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeto.sistemabiblioteca.DTOs.EnderecoDTO;
import com.projeto.sistemabiblioteca.DTOs.PessoaDTO;
import com.projeto.sistemabiblioteca.DTOs.RegistroDTO;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.repositories.EnderecoRepository;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.services.PessoaService;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class PessoaServiceIntegrationTest {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private EnderecoDTO criarEnderecoDTO(Long id) {
		return new EnderecoDTO(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				id);
	}
	
	private Endereco criarEndereco() {
		return new Endereco("Rua Teste", "123", "Complemento", "Bairro", "00000000", "Cidade", null);
	}
	
	private Pessoa criarPessoa(String email, String cpf, FuncaoUsuario funcao, StatusConta status) {
		return new Pessoa(
                "Maria Joana",
                new Cpf(cpf),
                Sexo.FEMININO,
                funcao,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email(email),
                "senhaHash",
                status,
                null
            );
	}
	
	@Test
	void deveBuscarTodosFiltrandoPelaFuncao() {
        Pessoa pessoa1 = criarPessoa("maria@gmail.com", "11111111111", FuncaoUsuario.BIBLIOTECARIO, StatusConta.ATIVA);
        Pessoa pessoa2 = criarPessoa("joao@gmail.com", "22222222222", FuncaoUsuario.CLIENTE, StatusConta.ATIVA);
        
        pessoaRepository.save(pessoa1);
        pessoaRepository.save(pessoa2);
        
        
        Page<Pessoa> pessoas = pessoaService.buscarTodosComFuncaoIgualA(FuncaoUsuario.BIBLIOTECARIO, PageRequest.of(0, 5));
        
        Assertions.assertEquals(1, pessoas.getNumberOfElements());
        Assertions.assertEquals("maria@gmail.com", pessoas.getContent().get(0).getEmail().getEndereco());
	}
	
	@Test
	void deveBuscarTodosFiltrandoPeloStatusConta() {
        Pessoa pessoa1 = criarPessoa("maria@gmail.com", "11111111111", FuncaoUsuario.CLIENTE, StatusConta.EM_ANALISE_APROVACAO);
        Pessoa pessoa2 = criarPessoa("joao@gmail.com", "22222222222", FuncaoUsuario.CLIENTE, StatusConta.ATIVA);
        
        pessoaRepository.save(pessoa1);
        pessoaRepository.save(pessoa2);
        
        Page<Pessoa> pessoas = pessoaService.buscarTodosComStatusContaIgualA(StatusConta.EM_ANALISE_APROVACAO, PageRequest.of(0, 5));
        
        Assertions.assertEquals(1, pessoas.getNumberOfElements());
        Assertions.assertEquals("maria@gmail.com", pessoas.getContent().get(0).getEmail().getEndereco());
	}
	
	@Test
	void deveBuscarUsuarioPeloEmail() {
        Pessoa pessoa1 = criarPessoa("maria@gmail.com", "11111111111", FuncaoUsuario.CLIENTE, StatusConta.ATIVA);
        Pessoa pessoa2 = criarPessoa("joao@gmail.com", "22222222222", FuncaoUsuario.CLIENTE, StatusConta.ATIVA);
        pessoa2.setEmail(new Email("joao@gmail.com"));
        
        pessoaRepository.save(pessoa1);
        pessoaRepository.save(pessoa2);
        
        Pessoa pessoa = pessoaService.buscarPorEmail("maria@gmail.com");
        
        Assertions.assertEquals("maria@gmail.com", pessoa.getEmail().getEndereco());
	}
	
	@Test
	void deveBuscarUsuarioPeloCpf() {
        Pessoa pessoa1 = criarPessoa("maria@gmail.com", "11111111111", FuncaoUsuario.CLIENTE, StatusConta.ATIVA);
        Pessoa pessoa2 = criarPessoa("joao@gmail.com", "22222222222", FuncaoUsuario.CLIENTE, StatusConta.ATIVA);
        
        pessoaRepository.save(pessoa1);
        pessoaRepository.save(pessoa2);
        
        Pessoa pessoa = pessoaService.buscarPorCpf("11111111111");
        
        Assertions.assertEquals("11111111111", pessoa.getCpf().getValor());
	}
	
	@Test
	void deveCadastrarUsuario() {
		Estado estado = new Estado("São Paulo", null);
		Long estadoId = estadoRepository.save(estado).getIdEstado();
		
		RegistroDTO registroDTO = new RegistroDTO(
				"Maria Joana", 
				"11111111111", 
				Sexo.FEMININO, 
				LocalDate.of(1990, 10, 10), 
				"1234567891", 
				"maria@gmail.com", 
				"1234", 
				criarEnderecoDTO(estadoId));
		
        pessoaService.cadastrarUsuario(registroDTO);
        
        Page<Pessoa> pessoas = pessoaService.buscarTodos(PageRequest.of(0, 5));
        List<Endereco> enderecos = enderecoRepository.findAll();
        
        Assertions.assertEquals(1, pessoas.getNumberOfElements());
        Assertions.assertEquals(StatusConta.EM_ANALISE_APROVACAO, pessoas.getContent().get(0).getStatusConta());
        Assertions.assertEquals(1, enderecos.size());
        Assertions.assertEquals("rua teste", enderecos.get(0).getNomeLogradouro());
        Assertions.assertEquals(StatusAtivo.ATIVO, enderecos.get(0).getStatusAtivo());
	}
	
	@Test
	void deveCadastrarUsuarioPorAdmin() {
		Estado estado = new Estado("São Paulo", null);
		Long estadoId = estadoRepository.save(estado).getIdEstado();
		
		PessoaDTO pessoaDTO = new PessoaDTO(
				"Maria Joana", 
				"11111111111", 
				Sexo.FEMININO,
				FuncaoUsuario.BIBLIOTECARIO,
				LocalDate.of(1990, 10, 10), 
				"1234567891", 
				"maria@gmail.com", 
				"1234", 
				criarEnderecoDTO(estadoId));
		
        pessoaService.cadastrarUsuarioPorAdmin(pessoaDTO);
        
        Page<Pessoa> pessoas = pessoaService.buscarTodos(PageRequest.of(0, 5));
        List<Endereco> enderecos = enderecoRepository.findAll();
        
        Assertions.assertEquals(1, pessoas.getNumberOfElements());
        Assertions.assertEquals(StatusConta.ATIVA, pessoas.getContent().get(0).getStatusConta());
        Assertions.assertEquals(1, enderecos.size());
        Assertions.assertEquals("rua teste", enderecos.get(0).getNomeLogradouro());
        Assertions.assertEquals(StatusAtivo.ATIVO, enderecos.get(0).getStatusAtivo());
	}
	
	@Test
	void deveInativarUsuario() {
		Endereco endereco = criarEndereco();
		Long enderecoId = enderecoRepository.save(endereco).getIdEndereco();
		
        Pessoa pessoa = criarPessoa("maria@gmail.com", "11111111111", FuncaoUsuario.CLIENTE, StatusConta.ATIVA);
		pessoa.setEndereco(endereco);
        Long pessoaId = pessoaRepository.save(pessoa).getIdPessoa();
        
        pessoaService.inativar(pessoaId);
        
        Assertions.assertEquals(StatusConta.INATIVA, pessoaService.buscarPorId(pessoaId).getStatusConta());
        Assertions.assertEquals(StatusAtivo.INATIVO, enderecoRepository.findById(enderecoId).get().getStatusAtivo());
	}
	
	@Test
	void deveAtualizarUsuario() {
		Estado estado1 = new Estado("São Paulo", null);
		Estado estado2 = new Estado("Rio de Janeiro", null);
		
		estadoRepository.save(estado1);
	
		Long estado2Id = estadoRepository.save(estado2).getIdEstado();
		
		Endereco endereco = criarEndereco();
		endereco.setEstado(estado1);
		
		Long enderecoId = enderecoRepository.save(endereco).getIdEndereco();
		
		Pessoa pessoa = criarPessoa("maria@gmail.com", "11111111111", FuncaoUsuario.BIBLIOTECARIO, StatusConta.ATIVA);
		pessoa.setEndereco(endereco);
		
		Long pessoaId = pessoaRepository.save(pessoa).getIdPessoa();
				
		PessoaDTO pessoaDTO = new PessoaDTO(
				"João Silva", 
				"22222222222", 
				Sexo.MASCULINO,
				FuncaoUsuario.BIBLIOTECARIO,
				LocalDate.of(2000, 10, 10), 
				"98765432101", 
				"joao@gmail.com", 
				"123456789", 
				new EnderecoDTO(
						"rua alterada", 
						"4321", 
						"complemento alterado", 
						"bairro alterado",
						"11111111",
						"cidade alterada",
						estado2Id));
		
		Pessoa pessoaAtualizada = pessoaService.atualizar(pessoaId, pessoaDTO, pessoa);
		
		Assertions.assertEquals("João Silva", pessoaAtualizada.getNome());
		Assertions.assertEquals("22222222222", pessoaAtualizada.getCpf().getValor());
		Assertions.assertEquals(Sexo.MASCULINO, pessoaAtualizada.getSexo());
		Assertions.assertEquals(FuncaoUsuario.BIBLIOTECARIO, pessoaAtualizada.getFuncao());
		Assertions.assertEquals(LocalDate.of(2000, 10, 10), pessoaAtualizada.getDtNascimento());
		Assertions.assertEquals("98765432101", pessoaAtualizada.getTelefone().getNumero());
		Assertions.assertEquals("joao@gmail.com", pessoaAtualizada.getEmail().getEndereco());
		Assertions.assertTrue(passwordEncoder.matches("123456789", pessoaAtualizada.getSenhaHash()));

		Endereco enderecoAtualizado = enderecoRepository.findById(enderecoId).get();
		
		Assertions.assertEquals("rua alterada", enderecoAtualizado.getNomeLogradouro());
		Assertions.assertEquals("4321", enderecoAtualizado.getNumero());
		Assertions.assertEquals("complemento alterado", enderecoAtualizado.getComplemento());
		Assertions.assertEquals("bairro alterado", enderecoAtualizado.getBairro());
		Assertions.assertEquals("11111111", enderecoAtualizado.getCep());
		Assertions.assertEquals("cidade alterada", enderecoAtualizado.getCidade());
		Assertions.assertEquals("Rio de Janeiro", enderecoAtualizado.getEstado().getNome());
	}
}

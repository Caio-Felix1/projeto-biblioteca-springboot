package com.projeto.sistemabiblioteca.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeto.sistemabiblioteca.DTOs.EnderecoDTO;
import com.projeto.sistemabiblioteca.DTOs.PessoaDTO;
import com.projeto.sistemabiblioteca.DTOs.RegistroDTO;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.exceptions.AcessoNegadoException;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.services.EnderecoService;
import com.projeto.sistemabiblioteca.services.EstadoService;
import com.projeto.sistemabiblioteca.services.PessoaService;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {
	
	@Mock
	private PessoaRepository pessoaRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private EstadoService estadoService;
	
	@Mock
	private EnderecoService enderecoService;
	
	@InjectMocks
	private PessoaService pessoaService;
	
	
	private EnderecoDTO criarEnderecoDTO() {
		return new EnderecoDTO(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				1L);
	}
	
	private Endereco criarEndereco() {
		return new Endereco(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				null);
	}
	
	private Pessoa criarPessoa(FuncaoUsuario funcao) {		
		return new Pessoa(
				"Maria Joana", 
				new Cpf("11111111111"), 
				Sexo.FEMININO, 
				funcao,
				LocalDate.of(1990, 10, 10), 
				LocalDate.of(2025, 10, 10),
				new Telefone("1234567891"), 
				new Email("maria@gmail.com"), 
				"senhaHash",
				StatusConta.ATIVA,
				criarEndereco());
	}
	
	private PessoaDTO criarPessoaDTO(FuncaoUsuario funcao) {
		return new PessoaDTO(
				"João Silva", 
				"22222222222", 
				Sexo.MASCULINO,
				funcao,
				LocalDate.of(1990, 10, 10), 
				"1234567891", 
				"joao@gmail.com", 
				"1234", 
				criarEnderecoDTO());
	}
	
	@Test
	void deveCadastrarUsuario() {
		RegistroDTO registroDTO = new RegistroDTO(
				"Maria Joana", 
				"11111111111", 
				Sexo.FEMININO, 
				LocalDate.of(1990, 10, 10), 
				"1234567891", 
				"maria@gmail.com", 
				"1234", 
				criarEnderecoDTO());
		
		Estado estado = new Estado("São Paulo", new Pais("Brasil"));

		when(pessoaRepository.existsByEmailEndereco("maria@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("11111111111")).thenReturn(false);
		when(passwordEncoder.encode("1234")).thenReturn("senhaHash");
		when(estadoService.buscarPorId(any(Long.class))).thenReturn(estado);
		
		pessoaService.cadastrarUsuario(registroDTO);
		
		verify(passwordEncoder).encode("1234");
		verify(enderecoService).inserir(any(Endereco.class));
		verify(estadoService).buscarPorId(any(Long.class));
		verify(pessoaRepository).save(argThat(pessoa ->
		pessoa.getNome().equals("Maria Joana") &&
		pessoa.getStatusConta() == StatusConta.EM_ANALISE_APROVACAO &&
		pessoa.getEmail().getEndereco().equals("maria@gmail.com") &&
		pessoa.getSenhaHash().equals("senhaHash") &&
		pessoa.getEndereco().getStatusAtivo() == StatusAtivo.ATIVO &&
		pessoa.getEndereco().getEstado().getNome().equals(estado.getNome())));
	}
	
	@Test
	void deveCadastrarUsuarioPorAdm() {
		PessoaDTO pessoaDTO = criarPessoaDTO(FuncaoUsuario.BIBLIOTECARIO);
		
		Estado estado = new Estado("São Paulo", new Pais("Brasil"));
		/*
		Endereco endereco = new Endereco(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				estado);
		
		Pessoa pessoaEsperada = new Pessoa(
				"Maria Joana", 
				new Cpf("11111111111"), 
				Sexo.FEMININO, 
				FuncaoUsuario.CLIENTE,
				LocalDate.of(1990, 10, 10), 
				LocalDate.of(2025, 10, 10),
				new Telefone("1234567891"), 
				new Email("maria@gmail.com"), 
				"senhaHash",
				StatusConta.EM_ANALISE_APROVACAO,
				endereco);
		*/
		when(pessoaRepository.existsByEmailEndereco("joao@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("22222222222")).thenReturn(false);
		when(passwordEncoder.encode("1234")).thenReturn("senhaHash");
		when(estadoService.buscarPorId(any(Long.class))).thenReturn(estado);
		
		pessoaService.cadastrarUsuarioPorAdmin(pessoaDTO);
		
		verify(passwordEncoder).encode("1234");
		verify(enderecoService).inserir(any(Endereco.class));
		verify(estadoService).buscarPorId(any(Long.class));
		verify(pessoaRepository).save(argThat(pessoa ->
		pessoa.getNome().equals("João Silva") &&
		pessoa.getStatusConta() == StatusConta.ATIVA &&
		pessoa.getEmail().getEndereco().equals("joao@gmail.com") &&
		pessoa.getSenhaHash().equals("senhaHash") &&
		pessoa.getFuncao() == FuncaoUsuario.BIBLIOTECARIO &&
		pessoa.getEndereco().getStatusAtivo() == StatusAtivo.ATIVO &&
		pessoa.getEndereco().getEstado().getNome().equals(estado.getNome())));
	}
	
	@Test
	void deveInativarUsuario() {
		Pessoa pessoaTeste = criarPessoa(FuncaoUsuario.CLIENTE);
		
		when(pessoaRepository.findById(any(Long.class))).thenReturn(Optional.of(pessoaTeste));
		doAnswer(invoc -> {
			pessoaTeste.getEndereco().inativar();
			return null;
		}).when(enderecoService).inativar(isNull());
		
		pessoaService.inativar(1L);
		
		verify(enderecoService).inativar(isNull());
		verify(pessoaRepository).save(argThat(pessoa ->
		pessoa.getStatusConta() == StatusConta.INATIVA &&
		pessoa.getEndereco().getStatusAtivo() == StatusAtivo.INATIVO));
	}
	
	@Test
	void deveAtualizarQualquerUsuarioComUsuarioLogadoAdministrador() {
		Pessoa cliente = criarPessoa(FuncaoUsuario.CLIENTE);
		
		when(pessoaRepository.existsByEmailEndereco("joao@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("22222222222")).thenReturn(false);
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
	    when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(inv -> inv.getArgument(0));

	    Pessoa atualizado = pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), criarPessoa(FuncaoUsuario.ADMINISTRADOR));

	    Assertions.assertEquals("João Silva", atualizado.getNome());
	    verify(enderecoService).atualizar(isNull(), any());
	}
	
	@Test
	void deveAtualizarASiMesmoComUsuarioLogadoBibliotecario() {
		Pessoa bibliotecario = Mockito.spy(criarPessoa(FuncaoUsuario.BIBLIOTECARIO));
		when(bibliotecario.getIdPessoa()).thenReturn(1L);
		
		when(pessoaRepository.existsByEmailEndereco("joao@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("22222222222")).thenReturn(false);
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(bibliotecario));
	    when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(inv -> inv.getArgument(0));
	    
	    Pessoa atualizado = pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.BIBLIOTECARIO), bibliotecario);

	    Assertions.assertEquals("João Silva", atualizado.getNome());
	    verify(enderecoService).atualizar(isNull(), any());
	}
	
	@Test
	void deveAtualizarClienteComUsuarioLogadoBibliotecario() {
		Pessoa cliente = Mockito.spy(criarPessoa(FuncaoUsuario.CLIENTE));
		when(cliente.getIdPessoa()).thenReturn(1L);
		Pessoa bibliotecario = Mockito.spy(criarPessoa(FuncaoUsuario.BIBLIOTECARIO));
		when(bibliotecario.getIdPessoa()).thenReturn(2L);
		
		when(pessoaRepository.existsByEmailEndereco("joao@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("22222222222")).thenReturn(false);
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
	    when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(inv -> inv.getArgument(0));
	    
	    Pessoa atualizado = pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), bibliotecario);

	    Assertions.assertEquals("João Silva", atualizado.getNome());
	    verify(enderecoService).atualizar(isNull(), any());
	}
	
	@Test
	void deveAtualizarASiMesmoComUsuarioLogadoCliente() {
		Pessoa cliente = Mockito.spy(criarPessoa(FuncaoUsuario.CLIENTE));
		when(cliente.getIdPessoa()).thenReturn(1L);
		
		when(pessoaRepository.existsByEmailEndereco("joao@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("22222222222")).thenReturn(false);
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
	    when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(inv -> inv.getArgument(0));
	    
	    Pessoa atualizado = pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), cliente);

	    Assertions.assertEquals("João Silva", atualizado.getNome());
	    verify(enderecoService).atualizar(isNull(), any());
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarUsuarioInativo() {
		Pessoa cliente = criarPessoa(FuncaoUsuario.CLIENTE);
		cliente.inativarConta();
		
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), criarPessoa(FuncaoUsuario.ADMINISTRADOR)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um objeto com status da conta INATIVA");
	}
	
	@Test
	void deveLancarExcecaoQuandoClienteTentarAtualizarOutroUsuario() {
		Pessoa cliente1 = Mockito.spy(criarPessoa(FuncaoUsuario.CLIENTE));
		when(cliente1.getIdPessoa()).thenReturn(1L);
		Pessoa cliente2 = Mockito.spy(criarPessoa(FuncaoUsuario.CLIENTE));
		when(cliente2.getIdPessoa()).thenReturn(2L);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente1));
		
	    Assertions.assertThrows(AcessoNegadoException.class,
	    		() -> pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), cliente2),
	    		"Era esperado que fosse lançada uma exceção quando um cliente tenta atualizar outro usuário");
	}
	
	@Test
	void deveLancarExcecaoQuandoBibliotecarioTentarAtualizarSuaFuncaoParaCliente() {
		Pessoa bibliotecario = Mockito.spy(criarPessoa(FuncaoUsuario.BIBLIOTECARIO));
		when(bibliotecario.getIdPessoa()).thenReturn(1L);
		
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(bibliotecario));
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), bibliotecario),
	    		"Era esperado que fosse lançada uma exceção quando um bibliotecário tenta atualizar sua função para cliente");
	}
	
	@Test
	void deveLancarExcecaoQuandoAdministradorTentarAtualizarSuaFuncaoParaCliente() {
		Pessoa administrador = criarPessoa(FuncaoUsuario.ADMINISTRADOR);
		
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(administrador));
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), administrador),
	    		"Era esperado que fosse lançada uma exceção quando um administrador tenta atualizar sua função para cliente");
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarAFuncaoDeUmCliente() {
		Pessoa cliente = Mockito.spy(criarPessoa(FuncaoUsuario.CLIENTE));
		when(cliente.getIdPessoa()).thenReturn(1L);
		
	    when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.BIBLIOTECARIO), cliente),
	    		"Era esperado que fosse lançada uma exceção quando um cliente tenta atualizar sua função");
	}
}

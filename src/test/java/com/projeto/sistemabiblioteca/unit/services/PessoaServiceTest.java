package com.projeto.sistemabiblioteca.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
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
import com.projeto.sistemabiblioteca.exceptions.CpfJaCadastradoException;
import com.projeto.sistemabiblioteca.exceptions.EmailJaCadastradoException;
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
	void deveLancarExcecaoAoUtilizarUmEmailJaExistente() {
		String emailTeste = "teste@gmail.com";
		
		when(pessoaRepository.existsByEmailEndereco(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(EmailJaCadastradoException.class,
				() -> pessoaService.verificarEmailDisponivel(emailTeste),
				"Era esperado que fosse lançada uma exceção ao tentar utlizar um email já existente no banco de dados");
	}
	
	@Test
	void deveLancarExcecaoAoUtilizarUmCpfJaExistente() {
		String cpfTeste = "11111111111";
		
		when(pessoaRepository.existsByCpfValor(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(CpfJaCadastradoException.class,
				() -> pessoaService.verificarCpfDisponivel(cpfTeste),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar um CPF já existente no banco de dados");
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
		
		verify(pessoaRepository).existsByEmailEndereco(any(String.class));
		verify(pessoaRepository).existsByCpfValor(any(String.class));
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

		when(pessoaRepository.existsByEmailEndereco("joao@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("22222222222")).thenReturn(false);
		when(passwordEncoder.encode("1234")).thenReturn("senhaHash");
		when(estadoService.buscarPorId(any(Long.class))).thenReturn(estado);
		
		pessoaService.cadastrarUsuarioPorAdmin(pessoaDTO);
		
		verify(pessoaRepository).existsByEmailEndereco(any(String.class));
		verify(pessoaRepository).existsByCpfValor(any(String.class));
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
	void deveLancarExcecaoAoCadastrarUsuarioComEstadoInativo() {
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
		estado.inativar();

		when(pessoaRepository.existsByEmailEndereco("maria@gmail.com")).thenReturn(false);
		when(pessoaRepository.existsByCpfValor("11111111111")).thenReturn(false);
		when(passwordEncoder.encode("1234")).thenReturn("senhaHash");
		when(estadoService.buscarPorId(any(Long.class))).thenReturn(estado);
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> pessoaService.cadastrarUsuario(registroDTO),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar um usuário com endereço contendo estado inativo");
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
	void deveLancarExcecaoAoInativarUsuarioComStatusInativo() {
		Pessoa pessoaTeste = criarPessoa(FuncaoUsuario.CLIENTE);
		pessoaTeste.inativarConta();
		
		when(pessoaRepository.findById(any(Long.class))).thenReturn(Optional.of(pessoaTeste));
		
		Assertions.assertThrows(IllegalStateException.class,
				() -> pessoaService.inativar(1L),
				"Era esperado que fosse lançada uma exceção ao tentar inativar um usuário já inativo");
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
	
	// mais testes com adm
	
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
	void deveLancarExcecaoQuandoBibliotecarioTentarAtualizarOutroBibliotecario() {
		Pessoa bibliotecario1 = Mockito.spy(criarPessoa(FuncaoUsuario.BIBLIOTECARIO));
		when(bibliotecario1.getIdPessoa()).thenReturn(1L);
		Pessoa bibliotecario2 = Mockito.spy(criarPessoa(FuncaoUsuario.BIBLIOTECARIO));
		when(bibliotecario2.getIdPessoa()).thenReturn(2L);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(bibliotecario1));
		
	    Assertions.assertThrows(AcessoNegadoException.class,
	    		() -> pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), bibliotecario2),
	    		"Era esperado que fosse lançada uma exceção quando um bibliotecário tenta atualizar outro bibliotecário");
	}
	
	@Test
	void deveLancarExcecaoQuandoBibliotecarioTentarAlterarAdministrador() {
		Pessoa bibliotecario = Mockito.spy(criarPessoa(FuncaoUsuario.BIBLIOTECARIO));
		when(bibliotecario.getIdPessoa()).thenReturn(1L);
		Pessoa administrador = Mockito.spy(criarPessoa(FuncaoUsuario.ADMINISTRADOR));
		when(administrador.getIdPessoa()).thenReturn(2L);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(administrador));
		
	    Assertions.assertThrows(AcessoNegadoException.class,
	    		() -> pessoaService.atualizar(1L, criarPessoaDTO(FuncaoUsuario.CLIENTE), bibliotecario),
	    		"Era esperado que fosse lançada uma exceção quando um bibliotecário tenta atualizar um administrador");
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
	
	@Test
	void deveAprovarContaDeCliente() {
		Pessoa cliente = new Pessoa(
				null, 
				null, 
				null, 
				FuncaoUsuario.CLIENTE,
				LocalDate.of(1990, 10, 10), 
				LocalDate.of(2025, 10, 10),
				null, 
				null, 
				null,
				StatusConta.EM_ANALISE_APROVACAO,
				null);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
		
		pessoaService.aprovarConta(1L);
		
		verify(pessoaRepository).findById(any(Long.class));
		
		Assertions.assertEquals(StatusConta.ATIVA, cliente.getStatusConta());
	}
	
	@Test
	void deveLancarExcecaoAoAprovarContaDeUsuarioDiferenteDeCliente() {
		Pessoa bibliotecario = criarPessoa(FuncaoUsuario.BIBLIOTECARIO);
		Pessoa administrador = criarPessoa(FuncaoUsuario.ADMINISTRADOR);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(bibliotecario));
		when(pessoaRepository.findById(2L)).thenReturn(Optional.of(administrador));
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.aprovarConta(1L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aprovarConta em um usuário com função de usuário BIBLIOTECARIO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.aprovarConta(2L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aprovarConta em um usuário com função de usuário ADMINISTRADOR");
	}
	
	@Test
	void deveRejeitarContaDeCliente() {
		Pessoa cliente = new Pessoa(
				null, 
				null, 
				null, 
				FuncaoUsuario.CLIENTE,
				LocalDate.of(1990, 10, 10), 
				LocalDate.of(2025, 10, 10),
				null, 
				null, 
				null,
				StatusConta.EM_ANALISE_APROVACAO,
				criarEndereco());
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
		
		pessoaService.rejeitarConta(1L);
		
		verify(pessoaRepository).findById(any(Long.class));
		
		Assertions.assertEquals(StatusConta.REJEITADA, cliente.getStatusConta());
	}
	
	@Test
	void deveLancarExcecaoAoRejeitarContaDeUsuarioDiferenteDeCliente() {
		Pessoa bibliotecario = criarPessoa(FuncaoUsuario.BIBLIOTECARIO);
		Pessoa administrador = criarPessoa(FuncaoUsuario.ADMINISTRADOR);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(bibliotecario));
		when(pessoaRepository.findById(2L)).thenReturn(Optional.of(administrador));
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.rejeitarConta(1L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarConta em um usuário com função de usuário BIBLIOTECARIO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.rejeitarConta(2L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarConta em um usuário com função de usuário ADMINISTRADOR");
	}
	
	@Test
	void deveSolicitarExclusaoDaContaDeCliente() {
		Pessoa cliente = criarPessoa(FuncaoUsuario.CLIENTE);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
		
		pessoaService.solicitarExclusaoConta(1L, "Cliente pediu a exclusão de sua conta.");
		
		verify(pessoaRepository).findById(any(Long.class));
		
		Assertions.assertEquals(StatusConta.EM_ANALISE_EXCLUSAO, cliente.getStatusConta());
		Assertions.assertEquals("Cliente pediu a exclusão de sua conta.", cliente.getMotivoSolicitacaoExclusao());
	}
	
	@Test
	void deveLancarExcecaoAoSolicitarExclusaoDaContaDeUsuarioDiferenteDeCliente() {
		Pessoa bibliotecario = criarPessoa(FuncaoUsuario.BIBLIOTECARIO);
		Pessoa administrador = criarPessoa(FuncaoUsuario.ADMINISTRADOR);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(bibliotecario));
		when(pessoaRepository.findById(2L)).thenReturn(Optional.of(administrador));
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.solicitarExclusaoConta(1L, "Usuário pediu a exclusão de sua conta."),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusaoConta em um usuário com função de usuário BIBLIOTECARIO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.solicitarExclusaoConta(2L, "Usuário pediu a exclusão de sua conta."),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusaoConta em um usuário com função de usuário ADMINISTRADOR");
	}
	
	@Test
	void deveRejeitarSolicitacaoDeExclusaoDaContaDeCliente() {
		Pessoa cliente = criarPessoa(FuncaoUsuario.CLIENTE);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(cliente));
		
		pessoaService.solicitarExclusaoConta(1L, "Cliente pediu a exclusão de sua conta.");
		
		Assertions.assertEquals("Cliente pediu a exclusão de sua conta.", cliente.getMotivoSolicitacaoExclusao());
		
		pessoaService.rejeitarSolicitacaoExclusao(1L);
		
		verify(pessoaRepository, times(2)).findById(any(Long.class));
		
		Assertions.assertEquals(StatusConta.ATIVA, cliente.getStatusConta());
		Assertions.assertEquals(null, cliente.getMotivoSolicitacaoExclusao());
	}
	
	@Test
	void deveLancarExcecaoAoRejeitarSolicitacaoDeExclusaoDaContaDeUsuarioDiferenteDeCliente() {
		Pessoa bibliotecario = criarPessoa(FuncaoUsuario.BIBLIOTECARIO);
		Pessoa administrador = criarPessoa(FuncaoUsuario.ADMINISTRADOR);
		
		when(pessoaRepository.findById(1L)).thenReturn(Optional.of(bibliotecario));
		when(pessoaRepository.findById(2L)).thenReturn(Optional.of(administrador));
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.rejeitarSolicitacaoExclusao(1L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um usuário com função de usuário BIBLIOTECARIO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> pessoaService.rejeitarSolicitacaoExclusao(2L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um usuário com função de usuário ADMINISTRADOR");
	}
}

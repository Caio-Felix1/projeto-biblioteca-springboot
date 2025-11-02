package com.projeto.sistemabiblioteca.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.sistemabiblioteca.DTOs.EmprestimoCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;
import com.projeto.sistemabiblioteca.repositories.EmprestimoRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;
import com.projeto.sistemabiblioteca.services.EmprestimoService;
import com.projeto.sistemabiblioteca.services.ExemplarService;
import com.projeto.sistemabiblioteca.services.PessoaService;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {
	
	@Mock
	private EmprestimoRepository emprestimoRepository;
	
	@Mock
	private MultaRepository multaRepository;
	
	@Mock
	private PessoaService pessoaService;
	
	@Mock
	private ExemplarService exemplarService;
	
	@InjectMocks
	private EmprestimoService emprestimoService;
	
	private Pessoa criarClienteComStatusContaEmAnaliseAprovacao() {
		return new Pessoa(
				"Maria Joana", 
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
	}
	
	private Pessoa criarClienteComStatusContaAtiva() {
		Pessoa pessoa = criarClienteComStatusContaEmAnaliseAprovacao();
		pessoa.aprovarConta();
		return pessoa;
	}
	
	private Pessoa criarClienteComStatusContaRejeitada() {
		Pessoa pessoa = criarClienteComStatusContaEmAnaliseAprovacao();
		pessoa.rejeitarConta();
		return pessoa;
	}
	
	private Pessoa criarClienteComStatusContaEmAnaliseExclusao() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		pessoa.solicitarExclusaoConta();
		return pessoa;
	}
	
	private Pessoa criarClienteComStatusContaInativa() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		pessoa.inativarConta();
		return pessoa;
	}
	
	private Pessoa criarBibliotecario() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		pessoa.setFuncao(FuncaoUsuario.BIBLIOTECARIO);
		return pessoa;
	}
	
	private Pessoa criarAdministrador() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		pessoa.setFuncao(FuncaoUsuario.ADMINISTRADOR);
		return pessoa;
	}
	
	private Exemplar criarExemplarAlugado() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.alugar();
		return exemplar;
	}
	
	private Emprestimo criarEmprestimoComStatusReservado() {
		return new Emprestimo(LocalDate.of(2025, 10, 10), null, criarExemplarAlugado(), Multa.criarMultaVazia());
	}
	
	private Emprestimo criarEmprestimoComStatusSeparado() {
		Emprestimo emprestimo = criarEmprestimoComStatusReservado();
		emprestimo.separarExemplar(LocalDate.of(2025, 10, 10));
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusEmAndamento() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		emprestimo.retirarExemplar(LocalDate.of(2025, 10, 10), LocalDate.of(2025, 10, 19));
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusAtrasado() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		emprestimo.registrarAtraso(emprestimo.getDtDevolucaoPrevista().plusDays(1));
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusDevolvidoComAtraso() {
		Emprestimo emprestimo = criarEmprestimoComStatusAtrasado();
		emprestimo.devolverExemplar(emprestimo.getDtDevolucaoPrevista().plusDays(1));
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusCancelado() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		emprestimo.cancelarReserva();
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusExemplarPerdido() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		emprestimo.registrarPerdaDoExemplar();
		return emprestimo;
	}
	
	@Test
	void deveCadastrarEmprestimo() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		
		Exemplar exemplar = new Exemplar(EstadoFisico.EXCELENTE, null);
		
		EmprestimoCreateDTO emprestimoCreateDTO = new EmprestimoCreateDTO(
				1L,
				List.of(1L));
		
		when(pessoaService.buscarPorId(any(Long.class))).thenReturn(pessoa);
		when(emprestimoRepository.countByPessoaAndStatusIn(any(Pessoa.class), any())).thenReturn(2L);
		when(emprestimoRepository.existsByPessoaAndMultaStatusPagamento(any(Pessoa.class), any(StatusPagamento.class))).thenReturn(false);
		when(exemplarService.buscarPrimeiroExemplarPorEdicaoEStatus(any(Long.class), any(StatusExemplar.class))).thenReturn(exemplar);
		
		List<Emprestimo> emprestimos = emprestimoService.cadastrarEmprestimos(emprestimoCreateDTO);
		
		verify(pessoaService).buscarPorId(any(Long.class));
		verify(emprestimoRepository).countByPessoaAndStatusIn(any(Pessoa.class), any());
		verify(exemplarService).buscarPrimeiroExemplarPorEdicaoEStatus(any(Long.class), any(StatusExemplar.class));
		verify(exemplarService).inserir(any(Exemplar.class));
		verify(multaRepository).save(any(Multa.class));
		verify(emprestimoRepository).save(argThat(emp ->
				emp.getDtInicioEmprestimo().equals(LocalDate.now()) &&
				emp.getPessoa().getNome().equals("Maria Joana") &&
				emp.getExemplar().getEstadoFisico() == EstadoFisico.EXCELENTE));
		
		Assertions.assertEquals(1, emprestimos.size());
	}
	
	@Test
	void deveLancarExcecaoAoCadastrarEmprestimoComUsuarioComStatusContaInvalido() {
		Pessoa pessoa1 = criarClienteComStatusContaEmAnaliseAprovacao();
		Pessoa pessoa2 = criarClienteComStatusContaEmAnaliseExclusao();
		Pessoa pessoa3 = criarClienteComStatusContaRejeitada();
		Pessoa pessoa4 = criarClienteComStatusContaInativa();
		
		when(pessoaService.buscarPorId(1L)).thenReturn(pessoa1);
		when(pessoaService.buscarPorId(2L)).thenReturn(pessoa2);
		when(pessoaService.buscarPorId(3L)).thenReturn(pessoa3);
		when(pessoaService.buscarPorId(4L)).thenReturn(pessoa4);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(1L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuario com status da conta EM_ANALISE_APROVACAO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(2L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuario com status da conta EM_ANALISE_EXCLUSAO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(3L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuario com status da conta REJEITADA");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(4L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuario com status da conta INATIVA");
	}
	
	@Test
	void deveLancarExcecaoAoCadastrarEmprestimoComUsuarioDiferenteDeCliente() {
		Pessoa pessoa1 = criarBibliotecario();
		Pessoa pessoa2 = criarAdministrador();
		
		when(pessoaService.buscarPorId(1L)).thenReturn(pessoa1);
		when(pessoaService.buscarPorId(2L)).thenReturn(pessoa2);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(1L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuario com função de usuário BIBLIOTECARIO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(2L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuario com função de usuário ADMINISTRADOR");
	}
	
	@Test
	void deveLancarExcecaoAoCadastrarEmprestimoComUsuarioComOMaximoDeEmprestimosAtivosPorUsuario() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		
		when(pessoaService.buscarPorId(1L)).thenReturn(pessoa);
		when(emprestimoRepository.countByPessoaAndStatusIn(any(Pessoa.class), any())).thenReturn(5L);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(1L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuario com 5 empréstimos ativos");
	}
	
	@Test
	void deveLancarExcecaoQuandoQtdEmprestimosMaisPedidosExcederLimite() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		
		when(pessoaService.buscarPorId(1L)).thenReturn(pessoa);
		when(emprestimoRepository.countByPessoaAndStatusIn(any(Pessoa.class), any())).thenReturn(3L);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(1L, List.of(1L, 2L, 3L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com a soma da quantidade de empréstimos com pedidos ultrapassando o limite de 5 empréstimos ativos por usuário");
	}
	
	@Test
	void deveLancarExcecaoAoCadastrarEmprestimoComUsuarioComMultaPendente() {
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		
		when(pessoaService.buscarPorId(1L)).thenReturn(pessoa);
		when(emprestimoRepository.countByPessoaAndStatusIn(any(Pessoa.class), any())).thenReturn(4L);
		when(emprestimoRepository.existsByPessoaAndMultaStatusPagamento(any(Pessoa.class), any(StatusPagamento.class))).thenReturn(true);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.cadastrarEmprestimos(new EmprestimoCreateDTO(1L, List.of(1L))),
	    		"Era esperado que fosse lançada uma exceção ao tentar cadastrar um empréstimo com usuário com multa pendente");
	}
	
	@Test
	void deveAtualizarEmprestimo() {
		Pessoa pessoaMock = Mockito.mock(Pessoa.class);
		Exemplar exemplarMock = Mockito.mock(Exemplar.class);
		
		Emprestimo emprestimo = Mockito.spy(new Emprestimo(LocalDate.of(2025, 10, 10), pessoaMock, exemplarMock, null));
		when(emprestimo.getPessoa().getIdPessoa()).thenReturn(1L);
		when(emprestimo.getExemplar().getIdExemplar()).thenReturn(1L);
		
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		Exemplar exemplar = new Exemplar(EstadoFisico.EXCELENTE, null);
		
		EmprestimoUpdateDTO emprestimoUpdateDTO = new EmprestimoUpdateDTO(
				2L,
				2L);
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		when(pessoaService.buscarPorId(2L)).thenReturn(pessoa);
		when(emprestimoRepository.countByPessoaAndStatusIn(any(Pessoa.class), any())).thenReturn(2L);
		when(emprestimoRepository.existsByPessoaAndMultaStatusPagamento(any(Pessoa.class), any(StatusPagamento.class))).thenReturn(false);
		when(exemplarService.buscarPorId(2L)).thenReturn(exemplar);
		when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
		
		Emprestimo emprestimoAtualizado = emprestimoService.atualizar(1L, emprestimoUpdateDTO);
		
		verify(emprestimoRepository).findById(any(Long.class));
		verify(pessoaService).buscarPorId(any(Long.class));
		verify(exemplarService).buscarPorId(any(Long.class));
		verify(exemplarService).inserir(any(Exemplar.class));
		verify(emprestimoRepository).save(any(Emprestimo.class));
		
		Assertions.assertEquals("Maria Joana", emprestimoAtualizado.getPessoa().getNome());
		Assertions.assertEquals(EstadoFisico.EXCELENTE, emprestimoAtualizado.getExemplar().getEstadoFisico());
	}
	
	@Test
	void deveLancarExcecaoAoAtualizarEmprestimoComUsuarioComStatusInvalido() {		
		Pessoa pessoaMock = Mockito.mock(Pessoa.class);
		
		Emprestimo emprestimo = Mockito.spy(new Emprestimo(LocalDate.of(2025, 10, 10), pessoaMock, null, null));
		when(emprestimo.getPessoa().getIdPessoa()).thenReturn(1L);
		
		Pessoa pessoa1 = criarClienteComStatusContaEmAnaliseAprovacao();
		Pessoa pessoa2 = criarClienteComStatusContaEmAnaliseExclusao();
		Pessoa pessoa3 = criarClienteComStatusContaRejeitada();
		Pessoa pessoa4 = criarClienteComStatusContaInativa();
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		when(pessoaService.buscarPorId(2L)).thenReturn(pessoa1);
		when(pessoaService.buscarPorId(3L)).thenReturn(pessoa2);
		when(pessoaService.buscarPorId(4L)).thenReturn(pessoa3);
		when(pessoaService.buscarPorId(5L)).thenReturn(pessoa4);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(2L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com status da conta EM_ANALISE_APROVACAO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(3L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com status da conta EM_ANALISE_EXCLUSAO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(4L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com status da conta REJEITADA");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(5L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com status da conta INATIVA");
	}
	
	@Test
	void deveLancarExcecaoAoAtualizarEmprestimoComUsuarioDiferenteDeCliente() {		
		Pessoa pessoaMock = Mockito.mock(Pessoa.class);
		
		Emprestimo emprestimo = Mockito.spy(new Emprestimo(LocalDate.of(2025, 10, 10), pessoaMock, null, null));
		when(emprestimo.getPessoa().getIdPessoa()).thenReturn(1L);
		
		Pessoa pessoa1 = criarBibliotecario();
		Pessoa pessoa2 = criarAdministrador();
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		when(pessoaService.buscarPorId(2L)).thenReturn(pessoa1);
		when(pessoaService.buscarPorId(3L)).thenReturn(pessoa2);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(2L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com função de usuário BIBLIOTECARIO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(3L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com função de usuário ADMINISTRADOR");
	}
	
	@Test
	void deveLancarExcecaoAoAtualizarEmprestimoComUsuarioComOMaximoDeEmprestimosAtivos() {
		Pessoa pessoaMock = Mockito.mock(Pessoa.class);
		
		Emprestimo emprestimo = Mockito.spy(new Emprestimo(LocalDate.of(2025, 10, 10), pessoaMock, null, null));
		when(emprestimo.getPessoa().getIdPessoa()).thenReturn(1L);
		
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		when(pessoaService.buscarPorId(2L)).thenReturn(pessoa);
		when(emprestimoRepository.countByPessoaAndStatusIn(any(Pessoa.class), any())).thenReturn(5L);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(2L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com 5 empréstimos ativos");
	}
	
	@Test
	void deveLancarExcecaoAoAtualizarEmprestimoComUsuarioComMultaPendente() {
		Pessoa pessoaMock = Mockito.mock(Pessoa.class);
		
		Emprestimo emprestimo = Mockito.spy(new Emprestimo(LocalDate.of(2025, 10, 10), pessoaMock, null, null));
		when(emprestimo.getPessoa().getIdPessoa()).thenReturn(1L);
		
		Pessoa pessoa = criarClienteComStatusContaAtiva();
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		when(pessoaService.buscarPorId(2L)).thenReturn(pessoa);
		when(emprestimoRepository.countByPessoaAndStatusIn(any(Pessoa.class), any())).thenReturn(4L);
		when(emprestimoRepository.existsByPessoaAndMultaStatusPagamento(any(Pessoa.class), any(StatusPagamento.class))).thenReturn(true);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(2L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com usuario com multa pendente");
	}
	
	@Test
	void deveLancarExcecaoAoAtualizarEmprestimoComExemplarComStatusInvalido() {
		Pessoa pessoaMock = Mockito.mock(Pessoa.class);
		Exemplar exemplarMock = Mockito.mock(Exemplar.class);
		
		Emprestimo emprestimo = Mockito.spy(new Emprestimo(LocalDate.of(2025, 10, 10), pessoaMock, exemplarMock, null));
		when(emprestimo.getPessoa().getIdPessoa()).thenReturn(1L);
		when(emprestimo.getExemplar().getIdExemplar()).thenReturn(1L);
		
		Exemplar exemplar1 = new Exemplar(null, null);
		exemplar1.alugar();
		
		Exemplar exemplar2 = new Exemplar(null, null);
		exemplar2.remover();
		
		Exemplar exemplar3 = new Exemplar(null, null);
		exemplar3.registrarPerda();
		
		Exemplar exemplar4 = new Exemplar(null, null);
		exemplar4.solicitarExclusao();
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		when(exemplarService.buscarPorId(2L)).thenReturn(exemplar1);
		when(exemplarService.buscarPorId(3L)).thenReturn(exemplar2);
		when(exemplarService.buscarPorId(4L)).thenReturn(exemplar3);
		when(exemplarService.buscarPorId(5L)).thenReturn(exemplar4);
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(1L, 2L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com exemplar com status ALUGADO");
		
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(1L, 3L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com exemplar com status REMOVIDO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(1L, 4L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com exemplar com status PERDIDO");
	    
	    Assertions.assertThrows(IllegalArgumentException.class,
	    		() -> emprestimoService.atualizar(1L, new EmprestimoUpdateDTO(1L, 5L)),
	    		"Era esperado que fosse lançada uma exceção ao tentar atualizar um empréstimo com exemplar com status EM_ANALISE_EXCLUSAO");
	}
	
	@Test
	void devePagarMulta() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusExemplarPerdido();
		Emprestimo emprestimo2 = criarEmprestimoComStatusDevolvidoComAtraso();
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo1));
		when(emprestimoRepository.findById(2L)).thenReturn(Optional.of(emprestimo2));
		
		emprestimoService.pagarMulta(1L);
		emprestimoService.pagarMulta(2L);
		
		verify(emprestimoRepository, times(2)).findById(any(Long.class));
		verify(multaRepository, times(2)).save(any(Multa.class));
		
		Assertions.assertEquals(50, emprestimo1.getMulta().getValor());
		Assertions.assertEquals(StatusPagamento.PAGO, emprestimo1.getMulta().getStatusPagamento());
		
		Assertions.assertEquals(1.0, emprestimo2.getMulta().getValor());
		Assertions.assertEquals(StatusPagamento.PAGO, emprestimo2.getMulta().getStatusPagamento());
	}
	
	@Test
	void deveLancarExcecaoAoPagarMultaComEmprestimoComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado();
		Emprestimo emprestimo3 = criarEmprestimoComStatusEmAndamento();
		Emprestimo emprestimo4 = criarEmprestimoComStatusCancelado();
		Emprestimo emprestimo5 = criarEmprestimoComStatusAtrasado();
		
		when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo1));
		when(emprestimoRepository.findById(2L)).thenReturn(Optional.of(emprestimo2));
		when(emprestimoRepository.findById(3L)).thenReturn(Optional.of(emprestimo3));
		when(emprestimoRepository.findById(4L)).thenReturn(Optional.of(emprestimo4));
		when(emprestimoRepository.findById(5L)).thenReturn(Optional.of(emprestimo5));
		
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> emprestimoService.pagarMulta(1L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um empréstimo com status RESERVADO");
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> emprestimoService.pagarMulta(2L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um empréstimo com status SEPARADO");
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> emprestimoService.pagarMulta(3L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um empréstimo com status EM_ANDAMENTO");
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> emprestimoService.pagarMulta(4L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um empréstimo com status CANCELADO");
	    
	    Assertions.assertThrows(IllegalStateException.class,
	    		() -> emprestimoService.pagarMulta(5L),
	    		"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um empréstimo com status ATRASADO");
	}
}

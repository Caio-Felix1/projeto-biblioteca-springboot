package com.projeto.sistemabiblioteca.unit.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

public class EmprestimoTest {
	
	private final LocalDate dtInicioEmprestimoPadrao = LocalDate.parse("2025-10-10");
	private final LocalDate hojePadrao = LocalDate.parse("2025-10-12");
	private final LocalDate dtDevolucaoPrevista = LocalDate.parse("2025-10-15");
	
	private Exemplar criarExemplarAlugado() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.alugar();
		return exemplar;
	}
	
	private Emprestimo criarEmprestimoComStatusReservado() {
		return new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
	}
	
	private Emprestimo criarEmprestimoComStatusSeparado() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusEmAndamento() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusAtrasado() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		emprestimo.registrarAtraso(dtDevolucaoPrevista.plusDays(1));
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusDevolvido() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		emprestimo.devolverExemplar(hojePadrao);
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusCancelado() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.cancelarReserva();
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusExemplarPerdido() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		emprestimo.registrarPerdaDoExemplar();
		return emprestimo;
	}
	
	
	@Test
	void deveInstanciarEmprestimoComDataDeInicioDoEmprestimoValida() {
		Emprestimo emprestimo = Assertions.assertDoesNotThrow(
				() -> criarEmprestimoComStatusReservado(),
				"Era esperado que a instanciação funcionasse com a data de início do empréstimo " + dtInicioEmprestimoPadrao);
		
		Assertions.assertEquals(StatusEmprestimo.RESERVADO, emprestimo.getStatus(),
				"Era esperado que o valor retornado fosse RESERVADO");
		
		Assertions.assertEquals(dtInicioEmprestimoPadrao, emprestimo.getDtInicioEmprestimo(),
				"Era esperado que o valor retornado fosse " + dtInicioEmprestimoPadrao);
		
		Assertions.assertEquals(StatusExemplar.ALUGADO, emprestimo.getExemplar().getStatus(),
				"Era esperado que o valor retornado fosse ALUGADO");
	}
	
	@Test
	void deveLancarExcecaoAoInstanciarEmprestimoComDataDeInicioDoEmprestimoNula() {
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> new Emprestimo(null, null, null, null),
				"Era esperado que fosse lançada uma exceção para instanciação com data de início do empréstimo nula");
	}
	
	// testes aqui para definir data de devolução
	
	@ParameterizedTest
	@ValueSource(strings = {
			"2025-10-16", // um dia após a data de devolução prevista anterior
			"2026-01-08", // 90 dias após o início do empréstimo
	})
	void deveDefinirDataDevolucaoPrevistaComDataDeDevolucaoPrevistaValida(String dtStringTeste) {
		LocalDate dtDevolucaoPrevistaNova = LocalDate.parse(dtStringTeste);
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertDoesNotThrow(() -> emprestimo.definirDataDevolucaoPrevista(dtDevolucaoPrevistaNova, hojePadrao),
				"Era esperado que funcionasse o método definirDataDevolucaoPrevista com a data de devolução prevista " + dtDevolucaoPrevistaNova);
		
		Assertions.assertEquals(dtDevolucaoPrevistaNova, emprestimo.getDtDevolucaoPrevista(),
				"Era esperado que o valor retornado fosse " + dtDevolucaoPrevistaNova);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"2025-10-14", // data anterior à data de devolução prevista já existente
			"2025-10-15", // mesma data que a data de devolução prevista já existente
			"2025-10-16", // data de devolução prevista anterior à data de hoje teste
			"2025-10-17", // data de devolução prevista igual a data de hoje teste
			"2026-01-09" // 91 dias após o início do empréstimo
	})
	void deveLancarExcecaoAoDefinirDataDevolucaoPrevistaComDataDeDevolucaoPrevistaInvalida(String dtStringTeste) {
		LocalDate dtDevolucaoPrevistaNova = LocalDate.parse(dtStringTeste);
		LocalDate hojeTeste = (dtStringTeste.equals("2025-10-16") || dtStringTeste.equals("2025-10-17")) ? LocalDate.parse("2025-10-17") : hojePadrao;
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> emprestimo.definirDataDevolucaoPrevista(dtDevolucaoPrevistaNova, hojeTeste),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método definirDataDevolucaoPrevista com a data de devolução prevista " + dtDevolucaoPrevistaNova);
	}
	
	@Test
	void deveLancarExcecaoAoDefinirDataDevolucaoPrevistaComDataDeDevolucaoPrevistaNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> emprestimo.definirDataDevolucaoPrevista(null, hojePadrao),
				"Era esperado que funcionasse o método definirDataDevolucaoPrevista com a data de devolução prevista nula");
	}
	
	@Test
	void deveLancarExcecaoAoDefinirDataDevolucaoPrevistaComDataDeHojeNula() {
		LocalDate dtDevolucaoPrevistaNova = dtDevolucaoPrevista.plusDays(1);
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> emprestimo.definirDataDevolucaoPrevista(dtDevolucaoPrevistaNova, null),
				"Era esperado que funcionasse o método definirDataDevolucaoPrevista com a data de hoje nula");
	}
	
	@Test
	void deveLancarExcecaoAoDefinirDataDevolucaoPrevistaComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado(); // reservado
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado(); // separado
		Emprestimo emprestimo3 = criarEmprestimoComStatusAtrasado(); // atrasado
		Emprestimo emprestimo4 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo5 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo6 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.definirDataDevolucaoPrevista(dtDevolucaoPrevista, hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método definirDataDevolucaoPrevista em um objeto com status RESERVADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.definirDataDevolucaoPrevista(dtDevolucaoPrevista, hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método definirDataDevolucaoPrevista em um objeto com status SEPARADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.definirDataDevolucaoPrevista(dtDevolucaoPrevista, hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método definirDataDevolucaoPrevista em um objeto com status ATRASADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.definirDataDevolucaoPrevista(dtDevolucaoPrevista, hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método definirDataDevolucaoPrevista em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.definirDataDevolucaoPrevista(dtDevolucaoPrevista, hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método definirDataDevolucaoPrevista em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo6.definirDataDevolucaoPrevista(dtDevolucaoPrevista, hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método definirDataDevolucaoPrevista em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@Test
	void deveSepararExemplarComStatusValido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado();
		
		Assertions.assertDoesNotThrow(() -> emprestimo1.separarExemplar(hojePadrao),
				"Era esperado que funcionasse o método separarExemplar no objeto com status RESERVADO");
		
		Assertions.assertEquals(StatusEmprestimo.SEPARADO, emprestimo1.getStatus(),
				"Era esperado que o valor retornado fosse SEPARADO");
		
		Assertions.assertEquals(hojePadrao, emprestimo1.getDtSeparacaoExemplar(),
				"Era esperado que o valor retornado fosse " + hojePadrao);
		
		Assertions.assertDoesNotThrow(() -> emprestimo2.separarExemplar(hojePadrao),
				"Era esperado que funcionasse o método separarExemplar no objeto com status SEPARADO");
		
		Assertions.assertEquals(StatusEmprestimo.SEPARADO, emprestimo2.getStatus(),
				"Era esperado que o valor retornado fosse SEPARADO");
		
		Assertions.assertEquals(hojePadrao, emprestimo2.getDtSeparacaoExemplar(),
				"Era esperado que o valor retornado fosse " + hojePadrao);
	}
	
	@Test
	void deveLancarExcecaoAoSepararExemplarComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusEmAndamento(); // em andamento
		Emprestimo emprestimo2 = criarEmprestimoComStatusAtrasado(); // atrasado
		Emprestimo emprestimo3 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo4 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo5 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido

		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.separarExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método separarExemplar em um objeto com status EM_ANDAMENTO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.separarExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método separarExemplar em um objeto com status ATRASADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.separarExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método separarExemplar em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.separarExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método separarExemplar em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.separarExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método separarExemplar em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@Test
	void deveLancarExcecaoAoSepararExemplarComDataDeHojeInvalida() {
		LocalDate hoje = dtInicioEmprestimoPadrao.minusDays(1);
		Emprestimo emprestimo = criarEmprestimoComStatusReservado();
		
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> emprestimo.separarExemplar(hoje),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método separarExemplar com data de hoje anterior à data de início do empréstimo.");
	}
	
	@Test
	void deveLancarExcecaoAoSepararExemplarComDataDeHojeNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusReservado();
		
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> emprestimo.separarExemplar(null),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método separarExemplar com data de hoje nula");
	}
	
	@Test
	void deveRetirarExemplarComDoisParametrosComStatusValido() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		
		Assertions.assertDoesNotThrow(() -> emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista),
				"Era esperado que funcionasse o método retirarExemplar no objeto com status SEPARADO");
		
		Assertions.assertEquals(StatusEmprestimo.EM_ANDAMENTO, emprestimo.getStatus(),
				"Era esperado que o valor retornado fosse EM_ANDAMENTO");
		
		Assertions.assertEquals(hojePadrao, emprestimo.getDtRetiradaExemplar(),
				"Era esperado que o valor retornado fosse " + hojePadrao);
	}
	
	@Test
	void deveLancarExcecaoAoRetirarExemplarComDoisParametrosComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado(); // reservado
		Emprestimo emprestimo2 = criarEmprestimoComStatusEmAndamento(); // em andamento
		Emprestimo emprestimo3 = criarEmprestimoComStatusAtrasado(); // atrasado
		Emprestimo emprestimo4 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo5 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo6 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.retirarExemplar(hojePadrao, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar em um objeto com status RESERVADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.retirarExemplar(hojePadrao, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar em um objeto com status EM_ANDAMENTO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.retirarExemplar(hojePadrao, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar em um objeto com status ATRASADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.retirarExemplar(hojePadrao, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.retirarExemplar(hojePadrao, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo6.retirarExemplar(hojePadrao, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@Test
	void deveLancarExcecaoAoRetirarExemplarComDoisParametrosComDataDeDevolucaoPrevistaNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();

		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.retirarExemplar(hojePadrao, null),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar com data de devolução prevista nula");
	}
	
	@Test
	void deveLancarExcecaoAoRetirarExemplarComDoisParametrosComDataDeHojeInvalida() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		LocalDate hoje = emprestimo.getDtSeparacaoExemplar().minusDays(1);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.retirarExemplar(null, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar com data de hoje nula");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.retirarExemplar(hoje, dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método retirarExemplar com data de hoje anterior à data de separação do exemplar");
	}
	
	@Test
	void deveRetirarExemplarComUmParametro() {
		Edicao edicao = new Edicao(null, 100, null, null, null, null, null, null);
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		emprestimo.getExemplar().setEdicao(edicao);
		
		LocalDate dtRetiradaExemplarResultante = hojePadrao.plusDays(9);
		
		Assertions.assertDoesNotThrow(() -> emprestimo.retirarExemplar(hojePadrao),
				"Era esperado que funcionasse o método retirarExemplar no objeto com status SEPARADO");
		
		Assertions.assertEquals(StatusEmprestimo.EM_ANDAMENTO, emprestimo.getStatus(),
				"Era esperado que o valor retornado fosse EM_ANDAMENTO");
		
		Assertions.assertEquals(hojePadrao, emprestimo.getDtRetiradaExemplar(),
				"Era esperado que o valor retornado fosse " + hojePadrao);
		
		Assertions.assertEquals(dtRetiradaExemplarResultante, emprestimo.getDtDevolucaoPrevista(),
				"Era esperado que o valor retornado fosse " + dtRetiradaExemplarResultante);
	}
	
	@Test
	void deveDevolverExemplarComStatusValido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusEmAndamento();
		Emprestimo emprestimo2 = criarEmprestimoComStatusAtrasado();
		LocalDate dtDevolucaoAtrasado = emprestimo2.getDtDevolucaoPrevista().plusDays(1);
		
		Assertions.assertDoesNotThrow(() -> emprestimo1.devolverExemplar(hojePadrao),
				"Era esperado que funcionasse o método devolverExemplar no objeto com status EM_ANDAMENTO");
		
		Assertions.assertEquals(StatusEmprestimo.DEVOLVIDO, emprestimo1.getStatus(),
				"Era esperado que o valor retornado fosse DEVOLVIDO");
		
		Assertions.assertEquals(hojePadrao, emprestimo1.getDtDevolvidoExemplar(),
				"Era esperado que o valor retornado fosse " + hojePadrao);
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, emprestimo1.getExemplar().getStatus(),
				"Era esperado que o valor retornado fosse DISPONIVEL");
		
		Assertions.assertDoesNotThrow(() -> emprestimo2.devolverExemplar(dtDevolucaoAtrasado),
				"Era esperado que funcionasse o método devolverExemplar no objeto com status EM_ANDAMENTO");
		
		Assertions.assertEquals(StatusEmprestimo.DEVOLVIDO, emprestimo2.getStatus(),
				"Era esperado que o valor retornado fosse DEVOLVIDO");
		
		Assertions.assertEquals(dtDevolucaoAtrasado, emprestimo2.getDtDevolvidoExemplar(),
				"Era esperado que o valor retornado fosse " + dtDevolucaoAtrasado);
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, emprestimo2.getExemplar().getStatus(),
				"Era esperado que o valor retornado fosse DISPONIVEL");
	}
	
	@Test
	void deveLancarExcecaoAoDevolverExemplarComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado(); // reservado
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado(); // separado
		Emprestimo emprestimo3 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo4 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo5 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.devolverExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar em um objeto com status RESERVADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.devolverExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar em um objeto com status SEPARADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.devolverExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.devolverExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.devolverExemplar(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@Test
	void deveLancarExcecaoAoDevolverExemplarComDataDeHojeInvalida() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusEmAndamento();
		LocalDate hoje1 = emprestimo1.getDtRetiradaExemplar().minusDays(1);
		
		Emprestimo emprestimo2 = criarEmprestimoComStatusAtrasado();
		LocalDate hoje2 = emprestimo2.getDtDevolucaoPrevista().minusDays(1);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo1.devolverExemplar(null),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar com data de hoje nula");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo1.devolverExemplar(hoje1),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar com data de hoje anterior à data de retirada do exemplar");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo2.devolverExemplar(hoje2),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolverExemplar com data de hoje anterior à data de devolução prevista quando foi registrado o atraso");
	}
	
	@Test
	void deveRegistrarPerdaDoExemplarComStatusValido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusEmAndamento(); 
		Emprestimo emprestimo2 = criarEmprestimoComStatusAtrasado(); 
		Emprestimo emprestimo3 = criarEmprestimoComStatusAtrasado();
		emprestimo3.getMulta().perdoarMulta();
		
		Assertions.assertDoesNotThrow(() -> emprestimo1.registrarPerdaDoExemplar(),
				"Era esperado que funcionasse o método registrarPerdaDoExemplar no objeto com status EM_ANDAMENTO");
		
		Assertions.assertEquals(StatusEmprestimo.EXEMPLAR_PERDIDO, emprestimo1.getStatus(),
				"Era esperado que o valor retornado fosse EXEMPLAR_PERDIDO");
		
		Assertions.assertEquals(StatusPagamento.PENDENTE, emprestimo1.getMulta().getStatusPagamento(),
				"Era esperado que o valor retornado fosse PENDENTE");
		
		Assertions.assertEquals(50.0, emprestimo1.getMulta().getValor(),
				"Era esperado que o valor retornado fosse 50.0");
		
		Assertions.assertEquals(StatusExemplar.PERDIDO, emprestimo1.getExemplar().getStatus(),
				"Era esperado que o valor retornado fosse PERDIDO");
		
		Assertions.assertDoesNotThrow(() -> emprestimo2.registrarPerdaDoExemplar(),
				"Era esperado que funcionasse o método registrarPerdaDoExemplar no objeto com status ATRASADO");
		
		Assertions.assertEquals(StatusEmprestimo.EXEMPLAR_PERDIDO, emprestimo2.getStatus(),
				"Era esperado que o valor retornado fosse EXEMPLAR_PERDIDO");
		
		Assertions.assertEquals(StatusPagamento.PENDENTE, emprestimo2.getMulta().getStatusPagamento(),
				"Era esperado que o valor retornado fosse PENDENTE");
		
		Assertions.assertEquals(50.0, emprestimo2.getMulta().getValor(),
				"Era esperado que o valor retornado fosse 50.0");
		
		Assertions.assertEquals(StatusExemplar.PERDIDO, emprestimo2.getExemplar().getStatus(),
				"Era esperado que o valor retornado fosse PERDIDO");
		
		Assertions.assertDoesNotThrow(() -> emprestimo3.registrarPerdaDoExemplar(),
				"Era esperado que funcionasse o método registrarPerdaDoExemplar no objeto com status ATRASADO");
		
		Assertions.assertEquals(StatusEmprestimo.EXEMPLAR_PERDIDO, emprestimo3.getStatus(),
				"Era esperado que o valor retornado fosse EXEMPLAR_PERDIDO");
		
		Assertions.assertEquals(StatusPagamento.PERDOADO, emprestimo3.getMulta().getStatusPagamento(),
				"Era esperado que o valor retornado fosse PERDOADO");
		
		Assertions.assertEquals(1.0, emprestimo3.getMulta().getValor(),
				"Era esperado que o valor retornado fosse 1.0");
		
		Assertions.assertEquals(StatusExemplar.PERDIDO, emprestimo3.getExemplar().getStatus(),
				"Era esperado que o valor retornado fosse PERDIDO");
	}
	
	@Test
	void deveLancarExcecaoAoRegistrarPerdaDoExemplarComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado(); // reservado
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado(); // separado
		Emprestimo emprestimo3 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo4 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo5 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.registrarPerdaDoExemplar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarPerdaDoExemplar em um objeto com status RESERVADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.registrarPerdaDoExemplar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarPerdaDoExemplar em um objeto com status SEPARADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.registrarPerdaDoExemplar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarPerdaDoExemplar em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.registrarPerdaDoExemplar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarPerdaDoExemplar em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.registrarPerdaDoExemplar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarPerdaDoExemplar em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@Test
	void deveRegistrarAtrasoComStatusValido() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertDoesNotThrow(() -> emprestimo.registrarAtraso(dtDevolucaoPrevista.plusDays(1)),
				"Era esperado que funcionasse o método registrarAtraso no objeto com status EM_ANDAMENTO");
		
		Assertions.assertEquals(StatusEmprestimo.ATRASADO, emprestimo.getStatus(),
				"Era esperado que o valor retornado fosse ATRASADO");
		
		Assertions.assertEquals(StatusPagamento.PENDENTE, emprestimo.getMulta().getStatusPagamento(),
				"Era esperado que o valor retornado fosse PENDENTE");
		
		Assertions.assertEquals(1.0, emprestimo.getMulta().getValor(),
				"Era esperado que o valor retornado fosse 1.0");
	}
	
	@Test
	void deveLancarExcecaoAoRegistrarAtrasoComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado(); // reservado
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado(); // separado
		Emprestimo emprestimo3 = criarEmprestimoComStatusAtrasado(); // atrasado
		Emprestimo emprestimo4 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo5 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo6 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido
		
		LocalDate dtAtraso = dtDevolucaoPrevista.plusDays(1);
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.registrarAtraso(dtAtraso),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso em um objeto com status RESERVADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.registrarAtraso(dtAtraso),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso em um objeto com status SEPARADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.registrarAtraso(dtAtraso),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso em um objeto com status ATRASADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.registrarAtraso(dtAtraso),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.registrarAtraso(dtAtraso),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo6.registrarAtraso(dtAtraso),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@Test
	void deveLancarExcecaoAoRegistrarAtrasoComDataDeHojeInvalida() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.registrarAtraso(null),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso com data de hoje nula");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo.registrarAtraso(dtDevolucaoPrevista.minusDays(1)),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso com data de hoje anterior à data de devolução prevista");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo.registrarAtraso(dtDevolucaoPrevista),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarAtraso com data de hoje igual a data de devolução prevista");
	}
	
	@Test
	void deveCancelarReservaComStatusValido() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		
		Assertions.assertDoesNotThrow(() -> emprestimo.cancelarReserva(),
				"Era esperado que funcionasse o método cancelarReserva no objeto com status SEPARADO");
		
		Assertions.assertEquals(StatusEmprestimo.CANCELADO, emprestimo.getStatus(),
				"Era esperado que o valor retornado fosse CANCELADO");
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, emprestimo.getExemplar().getStatus(),
				"Era esperado que o valor retornado fosse DISPONIVEL");
	}
	
	@Test
	void deveLancarExcecaoAoCancelarReservaComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado(); // reservado
		Emprestimo emprestimo2 = criarEmprestimoComStatusEmAndamento(); // em andamento
		Emprestimo emprestimo3 = criarEmprestimoComStatusAtrasado(); // atrasado            
		Emprestimo emprestimo4 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo5 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo6 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.cancelarReserva(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método cancelarReserva em um objeto com status RESERVADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.cancelarReserva(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método cancelarReserva em um objeto com status EM_ANDAMENTO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.cancelarReserva(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método cancelarReserva em um objeto com status ATRASADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.cancelarReserva(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método cancelarReserva em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.cancelarReserva(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método cancelarReserva em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo6.cancelarReserva(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método cancelarReserva em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"2025-10-14", // data de hoje anterior à data de devolução prevista
			"2025-10-15", // data de hoje igual a data de devolução prevista
			"2025-10-16" // data de hoje posterior à data de devolução prevista
	})
	void deveCalcularDiasDeAtrasoComDataDeHojeValida(String dtStringTeste) {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		LocalDate hoje = LocalDate.parse(dtStringTeste);
		
		if (!hoje.isAfter(dtDevolucaoPrevista)) {
			Assertions.assertEquals(0, emprestimo.calcularDiasDeAtraso(hoje),
					"Era esperado que o valor retornado fosse 0");
		}
		else {
			int resultadoEsperado = (int) ChronoUnit.DAYS.between(dtDevolucaoPrevista, hoje);
			Assertions.assertEquals(resultadoEsperado, emprestimo.calcularDiasDeAtraso(hoje),
					"Era esperado que o valor retornado fosse " + resultadoEsperado);
		}
	}
	
	@Test
	void deveLancarExcecaoAoCalcularDiasDeAtrasoComDataDeHojeNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.calcularDiasDeAtraso(null),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasDeAtraso com data de hoje nula");
	}
	
	@Test
	void deveLancarExcecaoAoCalcularDiasDeAtrasoComDataDeDevolucaoPrevistaNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado(); // não possui data de devolução prevista
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo.calcularDiasDeAtraso(dtDevolucaoPrevista.plusDays(1)),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasDeAtraso com data de devolução prevista nula");
	}
	
	@Test
	void deveRetornarTrueQuandoPrazoDeRetiradaEstiverExpirado() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		int prazoRetirada = 2;
		LocalDate hoje = hojePadrao.plusDays(3);
		
		Assertions.assertTrue(emprestimo.isPrazoDeRetiradaExpirado(hoje, prazoRetirada),
				"Era esperado que o valor retornado fosse true");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"2025-10-13", // 1 dia desde a separação do exemplar
			"2025-10-14" // 2 dias desde a separação do exemplar
	})
	void deveRetornarFalseQuandoPrazoDeRetiradaNaoEstiverExpirado(String dtStringTeste) {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		int prazoRetirada = 2;
		LocalDate hoje = LocalDate.parse(dtStringTeste);
		
		Assertions.assertFalse(emprestimo.isPrazoDeRetiradaExpirado(hoje, prazoRetirada),
				"Era esperado que o valor retornado fosse false");
	}
	
	@Test
	void deveLancarExcecaoAoVerificarSePrazoDeRetiradaNaoEstaExpiradoComDataDeHojeInvalida() {
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		int prazoRetirada = 2;
		LocalDate hoje = emprestimo.getDtSeparacaoExemplar().minusDays(1);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.isPrazoDeRetiradaExpirado(null, prazoRetirada),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método isPrazoDeRetiradaExpirado com data de hoje nula");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.isPrazoDeRetiradaExpirado(hoje, prazoRetirada),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método isPrazoDeRetiradaExpirado com data de hoje anterior à data de separação do exemplar");
	}
	
	@Test
	void deveLancarExcecaoAoVerificarSePrazoDeRetiradaNaoEstaExpiradoComDataDeSeparacaoDoExemplarNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusReservado(); // não possui data de separação do exemplar
		int prazoRetirada = 2;
		LocalDate hoje = hojePadrao.plusDays(3);
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo.isPrazoDeRetiradaExpirado(hoje, prazoRetirada),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método isPrazoDeRetiradaExpirado com data de separação do exemplar nula");
	}
	
	@Test
	void deveLancarExcecaoAoVerificarSePrazoDeRetiradaNaoEstaExpiradoComPrazoDeRetiradaMenorOuIgualAZero() {
		Emprestimo emprestimo = criarEmprestimoComStatusReservado(); // não possui data de separação do exemplar
		LocalDate hoje = hojePadrao.plusDays(3);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.isPrazoDeRetiradaExpirado(hoje, 0),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método isPrazoDeRetiradaExpirado com prazo de retirada igual a zero");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.isPrazoDeRetiradaExpirado(hoje, -1),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método isPrazoDeRetiradaExpirado com prazo de retirada menor que zero");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			1,
			100,
			101,
			500,
			501,
			1000,
			1001,
			3000,
			3001
	})
	void deveCalcularDataDeDevolucaoPrevista(int qtdPaginas) {
		Edicao edicao = new Edicao(null, qtdPaginas, null, null, null, null, null, null);
		Emprestimo emprestimo = criarEmprestimoComStatusSeparado();
		emprestimo.getExemplar().setEdicao(edicao);
		LocalDate dtSeparacaoExemplar = emprestimo.getDtSeparacaoExemplar();
		
		LocalDate dtDevolucaoPrevistaEsperada;
		if (qtdPaginas <= 100) {
			dtDevolucaoPrevistaEsperada = dtSeparacaoExemplar.plusDays(9);
			Assertions.assertEquals(dtDevolucaoPrevistaEsperada, emprestimo.calcularDataDeDevolucaoPrevista(),
					"Era esperado que o valor retornado fosse " + dtDevolucaoPrevistaEsperada);
		}
		else if (qtdPaginas <= 500) {
			dtDevolucaoPrevistaEsperada = dtSeparacaoExemplar.plusDays(16);
			Assertions.assertEquals(dtDevolucaoPrevistaEsperada, emprestimo.calcularDataDeDevolucaoPrevista(),
					"Era esperado que o valor retornado fosse " + dtDevolucaoPrevistaEsperada);
		}
		else if (qtdPaginas <= 1000) {
			dtDevolucaoPrevistaEsperada = dtSeparacaoExemplar.plusDays(23);
			Assertions.assertEquals(dtDevolucaoPrevistaEsperada, emprestimo.calcularDataDeDevolucaoPrevista(),
					"Era esperado que o valor retornado fosse " + dtDevolucaoPrevistaEsperada);
		}
		else if (qtdPaginas <= 3000){
			dtDevolucaoPrevistaEsperada = dtSeparacaoExemplar.plusDays(30);
			Assertions.assertEquals(dtDevolucaoPrevistaEsperada, emprestimo.calcularDataDeDevolucaoPrevista(),
					"Era esperado que o valor retornado fosse " + dtDevolucaoPrevistaEsperada);
		}
		else {
			dtDevolucaoPrevistaEsperada = dtSeparacaoExemplar.plusDays(37);
			Assertions.assertEquals(dtDevolucaoPrevistaEsperada, emprestimo.calcularDataDeDevolucaoPrevista(),
					"Era esperado que o valor retornado fosse " + dtDevolucaoPrevistaEsperada);
		}
	}
	
	@Test
	void deveLancarExcecaoAoCalcularDataDeDevolucaoPrevistaComDataDeDevolucaoPrevistaJaExistente() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento(); // possui data de devolução prevista
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo.calcularDataDeDevolucaoPrevista(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDataDeDevolucaoPrevista com data de devolução já existente");
	}
	
	@Test
	void deveLancarExcecaoAoCalcularDataDeDevolucaoPrevistaComDataDeSeparacaoDoExemplarNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusReservado(); // não possui data de separação do exemplar
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo.calcularDataDeDevolucaoPrevista(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDataDeDevolucaoPrevista com data de separação do exemplar nula");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"2025-10-14", // data anterior à data de devolução prevista
			"2025-10-15", // data igual à data de devolução prevista
			"2025-10-16" // data posterior à data de devolução prevista
	})
	void deveCalcularDiasRestantesComStatusValidoEDataDeHojeValida(String dtStringTeste) {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		LocalDate hoje = LocalDate.parse(dtStringTeste);
		
		if (!hoje.isAfter(dtDevolucaoPrevista)) {
			int resultadoEsperado = (int) ChronoUnit.DAYS.between(hoje, dtDevolucaoPrevista);
			Assertions.assertEquals(resultadoEsperado, emprestimo.calcularDiasRestantes(hoje),
					"Era esperado que o valor retornado fosse " + resultadoEsperado);
		}
		else {
			Assertions.assertEquals(0, emprestimo.calcularDiasRestantes(hoje),
					"Era esperado que o valor retornado fosse 0");
		}
	}
	
	@Test
	void deveLancarExcecaoAoCalcularDiasRestantesComStatusInvalido() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado(); // reservado
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado(); // separado
		Emprestimo emprestimo3 = criarEmprestimoComStatusAtrasado(); // atrasado
		Emprestimo emprestimo4 = criarEmprestimoComStatusDevolvido(); // devolvido
		Emprestimo emprestimo5 = criarEmprestimoComStatusCancelado(); // cancelado
		Emprestimo emprestimo6 = criarEmprestimoComStatusExemplarPerdido(); // exemplar_perdido
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo1.calcularDiasRestantes(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasRestantes em um objeto com status RESERVADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo2.calcularDiasRestantes(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasRestantes em um objeto com status SEPARADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo3.calcularDiasRestantes(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasRestantes em um objeto com status ATRASADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo4.calcularDiasRestantes(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasRestantes em um objeto com status DEVOLVIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo5.calcularDiasRestantes(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasRestantes em um objeto com status CANCELADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> emprestimo6.calcularDiasRestantes(hojePadrao),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasRestantes em um objeto com status EXEMPLAR_PERDIDO");
	}
	
	@Test
	void deveLancarExcecaoAoCalcularDiasRestantesComDataDeHojeNula() {
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> emprestimo.calcularDiasRestantes(null),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularDiasRestantes com data de hoje nula");
	}
}
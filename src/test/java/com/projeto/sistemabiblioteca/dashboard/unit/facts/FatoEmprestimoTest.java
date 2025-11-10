package com.projeto.sistemabiblioteca.dashboard.unit.facts;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.facts.FatoEmprestimo;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

public class FatoEmprestimoTest {
	
	private final LocalDate dtInicioEmprestimoPadrao = LocalDate.parse("2025-10-10");
	private final LocalDate hojePadrao = LocalDate.parse("2025-10-12");
	private final LocalDate dtDevolucaoPrevista = LocalDate.parse("2025-10-15");
	
	private Exemplar criarExemplarAlugado() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.alugar();
		return exemplar;
	}
	
	@Test
	void deveConverterEmprestimoSemAtrasoParaFatoEmprestimoCorretamente() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		emprestimo.devolverExemplar(hojePadrao);
		
		FatoEmprestimo fatoEmprestimo = new FatoEmprestimo(emprestimo, 1L, 2L, 3L, 4L, 5L, 6L);
		
		Assertions.assertEquals(dtInicioEmprestimoPadrao, fatoEmprestimo.getDtInicioEmprestimo());
		Assertions.assertEquals(hojePadrao, fatoEmprestimo.getDtSeparacaoExemplar());
		Assertions.assertEquals(hojePadrao, fatoEmprestimo.getDtRetiradaExemplar());
		Assertions.assertEquals(dtDevolucaoPrevista, fatoEmprestimo.getDtDevolucaoPrevista());
		Assertions.assertEquals(hojePadrao, fatoEmprestimo.getDtDevolvidoExemplar());
		Assertions.assertEquals(StatusEmprestimo.DEVOLVIDO, fatoEmprestimo.getStatus());
		Assertions.assertEquals(0, fatoEmprestimo.getDiasAtraso());
		Assertions.assertEquals(0.0, fatoEmprestimo.getValorMulta());
		Assertions.assertEquals(StatusPagamento.NAO_APLICAVEL, fatoEmprestimo.getStatusPagamento());
		Assertions.assertEquals(1L, fatoEmprestimo.getSkCliente());
		Assertions.assertEquals(2L, fatoEmprestimo.getSkExemplar());
		Assertions.assertEquals(3L, fatoEmprestimo.getSkEdicao());
		Assertions.assertEquals(4L, fatoEmprestimo.getSkIdioma());
		Assertions.assertEquals(5L, fatoEmprestimo.getSkEditora());
		Assertions.assertEquals(6L, fatoEmprestimo.getSkTitulo());
	}
	
	@Test
	void deveConverterEmprestimoComDevolucaoAtrasadaParaFatoEmprestimoCorretamente() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		emprestimo.registrarAtraso(dtDevolucaoPrevista.plusDays(1));
		emprestimo.devolverExemplar(dtDevolucaoPrevista.plusDays(1));
		
		FatoEmprestimo fatoEmprestimo = new FatoEmprestimo(emprestimo, 1L, 2L, 3L, 4L, 5L, 6L);
		
		Assertions.assertEquals(dtInicioEmprestimoPadrao, fatoEmprestimo.getDtInicioEmprestimo());
		Assertions.assertEquals(hojePadrao, fatoEmprestimo.getDtSeparacaoExemplar());
		Assertions.assertEquals(hojePadrao, fatoEmprestimo.getDtRetiradaExemplar());
		Assertions.assertEquals(dtDevolucaoPrevista, fatoEmprestimo.getDtDevolucaoPrevista());
		Assertions.assertEquals(dtDevolucaoPrevista.plusDays(1), fatoEmprestimo.getDtDevolvidoExemplar());
		Assertions.assertEquals(StatusEmprestimo.DEVOLVIDO, fatoEmprestimo.getStatus());
		Assertions.assertEquals(1, fatoEmprestimo.getDiasAtraso());
		Assertions.assertEquals(1.0, fatoEmprestimo.getValorMulta());
		Assertions.assertEquals(StatusPagamento.PENDENTE, fatoEmprestimo.getStatusPagamento());
		Assertions.assertEquals(1L, fatoEmprestimo.getSkCliente());
		Assertions.assertEquals(2L, fatoEmprestimo.getSkExemplar());
		Assertions.assertEquals(3L, fatoEmprestimo.getSkEdicao());
		Assertions.assertEquals(4L, fatoEmprestimo.getSkIdioma());
		Assertions.assertEquals(5L, fatoEmprestimo.getSkEditora());
		Assertions.assertEquals(6L, fatoEmprestimo.getSkTitulo());
	}
}

package com.projeto.sistemabiblioteca.unit.entities;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;

public class PessoaTest {
	
	private StatusConta statusContaPadrao = StatusConta.ATIVA;
	
	private Pessoa criarPessoaComStatusContaAtiva() {
		return new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
	}
	
	private Pessoa criarPessoaComStatusContaInativa() {
		Pessoa pessoa = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa.inativarConta();
		return pessoa;
	}
	
	private Pessoa criarPessoaComStatusContaEmAnaliseAprovacao() {
		return new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
	}
	
	private Pessoa criarPessoaComStatusContaRejeitada() {
		Pessoa pessoa = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		pessoa.rejeitarConta();
		return pessoa;
	}
	
	private Pessoa criarPessoaComStatusContaEmAnaliseExclusao() {
		Pessoa pessoa = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa.solicitarExclusaoConta();;
		return pessoa;
	}
	
	@Test
	void deveInstanciarPessoaComStatusContaValido() {		
		Pessoa pessoa1 = Assertions.assertDoesNotThrow(
				() -> criarPessoaComStatusContaAtiva(),
				"Era esperado que a instanciação funcionasse com status ATIVA");
		
		Assertions.assertEquals(StatusConta.ATIVA, pessoa1.getStatusConta(),
				"Era esperado que o valor retornado fosse ATIVA");
		
		Pessoa pessoa2 = Assertions.assertDoesNotThrow(
				() -> criarPessoaComStatusContaEmAnaliseAprovacao(),
				"Era esperado que a instanciação funcionasse com status EM_ANALISE_APROVACAO");
				
		Assertions.assertEquals(StatusConta.EM_ANALISE_APROVACAO, pessoa2.getStatusConta(),
				"Era esperado que o valor retornado fosse EM_ANALISE_APROVACAO");
	}
	
	@Test
	void deveLancarExcecaoParaInstanciacaoComStatusContaInvalido() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_EXCLUSAO, null),
			"Era esperado que fosse lançada uma exceção para instanciação com status EM_ANALISE_EXCLUSAO"
		);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> 
			new Pessoa(null, null, null, null, null, null, null, null, StatusConta.INATIVA, null),
			"Era esperado que fosse lançada uma exceção para instanciação com status INATIVA"
		);
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			new Pessoa(null, null, null, null, null, null, null, null, StatusConta.REJEITADA, null),
			"Era esperado que fosse lançada uma exceção para instanciação com status REJEITADA"
		);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"2020-12-01", // hoje
			"2020-12-02", // futuro
			"2003-12-01", // menor de idade - 17 anos
			"2002-12-01", // maior de idade - 18 anos
			"2001-12-01" // maior de idade - 19 anos
	})
	void deveValidarIdadeMinimaComDatasDeNascimentoValidasEInvalidas(String dtStringTeste) {
		LocalDate hoje = LocalDate.parse("2020-12-01");
		LocalDate nascimento = LocalDate.parse(dtStringTeste);
		
		Pessoa pessoa = criarPessoaComStatusContaAtiva();
		pessoa.setDtNascimento(nascimento);
		
		int idadeMinima = 18;
		
		if (nascimento.isAfter(hoje.minusYears(idadeMinima))) {
			Exception excecao = Assertions.assertThrows(IllegalArgumentException.class, 
					() -> pessoa.validarIdadeMinima(hoje, idadeMinima),
					"Era esperado que fosse lançada uma exceção para idade inferior a " + idadeMinima + " anos");
			
			Assertions.assertEquals("Erro: usuário deve ter no mínimo " + idadeMinima  + " anos de idade.", excecao.getMessage(),
					"Era esperado que a mensagem da exceção fosse 'Erro: usuário deve ter no mínimo " + idadeMinima + " anos de idade.'");
		}
		else {
			Assertions.assertDoesNotThrow(() -> pessoa.validarIdadeMinima(hoje, idadeMinima),
					"Era esperado que não houvesse erro para idade igual ou maior que " + idadeMinima + " anos");
		}
	}
	
	@Test
	void deveLancarExcecaoAoValidarIdadeMinimaComValorDaIdadeMinimaMenorOuIgualAZero() {
		LocalDate hoje = LocalDate.parse("2020-12-01");
		LocalDate nascimento = LocalDate.parse("2002-12-01");
		
		Pessoa pessoa = criarPessoaComStatusContaAtiva();
		pessoa.setDtNascimento(nascimento);
		
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> pessoa.validarIdadeMinima(hoje, 0),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método validarIdadeMinima com valor da idade mínima igual a zero");
		
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> pessoa.validarIdadeMinima(hoje, -1),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método validarIdadeMinima com valor da idade mínima negativo");
	}
	
	@Test
	void deveInativarContaComStatusContaValido() {
		Pessoa pessoa1 = criarPessoaComStatusContaAtiva();
		Pessoa pessoa2 = criarPessoaComStatusContaEmAnaliseExclusao();
		
		Assertions.assertDoesNotThrow(() -> pessoa1.inativarConta(),
				"Era esperado que funcionasse o método inativarConta no objeto com status ATIVA");
		
		Assertions.assertEquals(StatusConta.INATIVA, pessoa1.getStatusConta(),
				"Era esperado que o valor retornado fosse INATIVA");
		
		Assertions.assertDoesNotThrow(() -> pessoa2.inativarConta(),
				"Era esperado que funcionasse o método inativarConta no objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertEquals(StatusConta.INATIVA, pessoa1.getStatusConta(),
				"Era esperado que o valor retornado fosse INATIVA");
	}
	
	@Test
	void deveLancarExcecaoAoInativarContaComStatusContaInvalido() {
		Pessoa pessoa1 = criarPessoaComStatusContaInativa();
		Pessoa pessoa2 = criarPessoaComStatusContaRejeitada();
		Pessoa pessoa3 = criarPessoaComStatusContaEmAnaliseAprovacao();
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.inativarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método inativarConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.inativarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método inativarConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.inativarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método inativarConta em um objeto com status EM_ANALISE_APROVACAO");
	}
	
	@Test
	void deveAprovarContaComStatusContaEmAnaliseAprovacao() {
		Pessoa pessoa = criarPessoaComStatusContaEmAnaliseAprovacao();
		
		Assertions.assertDoesNotThrow(() -> pessoa.aprovarConta(),
				"Era esperado que funcionasse o método aprovarConta no objeto com status EM_ANALISE_APROVACAO");
		
		Assertions.assertEquals(StatusConta.ATIVA, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse ATIVA");
	}
	
	@Test
	void deveLancarExcecaoAoAprovarContaComStatusContaInvalido() {
		Pessoa pessoa1 = criarPessoaComStatusContaInativa();
		Pessoa pessoa2 = criarPessoaComStatusContaRejeitada();
		Pessoa pessoa3 = criarPessoaComStatusContaEmAnaliseExclusao();
		Pessoa pessoa4 = criarPessoaComStatusContaAtiva();
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.aprovarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aprovarConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.aprovarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aprovarConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.aprovarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aprovarConta em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.aprovarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aprovarConta em um objeto com status ATIVA");
	}
	
	@Test
	void deveRejeitarContaComStatusContaEmAnaliseAprovacao() {
		Pessoa pessoa = criarPessoaComStatusContaEmAnaliseAprovacao();
		
		Assertions.assertDoesNotThrow(() -> pessoa.rejeitarConta(),
				"Era esperado que funcionasse o método rejeitarConta no objeto com status EM_ANALISE_APROVACAO");
		
		Assertions.assertEquals(StatusConta.REJEITADA, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse REJEITADA");
	}
	
	@Test
	void deveLancarExcecaoAoRejeitarContaComStatusContaInvalido() {
		Pessoa pessoa1 = criarPessoaComStatusContaInativa();
		Pessoa pessoa2 = criarPessoaComStatusContaRejeitada();
		Pessoa pessoa3 = criarPessoaComStatusContaEmAnaliseExclusao();
		Pessoa pessoa4 = criarPessoaComStatusContaAtiva();
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.rejeitarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.rejeitarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.rejeitarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarConta em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.rejeitarConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarConta em um objeto com status ATIVA");
	}
	
	@Test
	void deveSolicitarExclusaoContaComStatusContaAtiva() {
		Pessoa pessoa = criarPessoaComStatusContaAtiva();
		
		Assertions.assertDoesNotThrow(() -> pessoa.solicitarExclusaoConta(),
				"Era esperado que funcionasse o método solicitarExclusaoConta no objeto com status ATIVA");
		
		Assertions.assertEquals(StatusConta.EM_ANALISE_EXCLUSAO, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse EM_ANALISE_EXCLUSAO");
	}
	
	@Test
	void deveLancarExcecaoAoSolicitarExclusaoContaComStatusContaInvalido() {
		Pessoa pessoa1 = criarPessoaComStatusContaInativa();
		Pessoa pessoa2 = criarPessoaComStatusContaRejeitada();
		Pessoa pessoa3 = criarPessoaComStatusContaEmAnaliseExclusao();
		Pessoa pessoa4 = criarPessoaComStatusContaEmAnaliseAprovacao();
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.solicitarExclusaoConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.solicitarExclusaoConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.solicitarExclusaoConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.solicitarExclusaoConta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status EM_ANALISE_APROVACAO");
	}
	
	@Test
	void deveRejeitarSolicitacaoExclusaoComStatusContaEmAnaliseExclusao() {
		Pessoa pessoa = criarPessoaComStatusContaEmAnaliseExclusao();
		
		Assertions.assertDoesNotThrow(() -> pessoa.rejeitarSolicitacaoExclusao(),
				"Era esperado que funcionasse o método rejeitarSolicitacaoExclusao no objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertEquals(StatusConta.ATIVA, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse ATIVA");
	}
	
	@Test
	void deveLancarExcecaoAoRejeitarSolicitacaoExclusaoComStatusContaInvalido() {
		Pessoa pessoa1 = criarPessoaComStatusContaInativa();
		Pessoa pessoa2 = criarPessoaComStatusContaRejeitada();
		Pessoa pessoa3 = criarPessoaComStatusContaAtiva();
		Pessoa pessoa4 = criarPessoaComStatusContaEmAnaliseAprovacao();
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status ATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status EM_ANALISE_APROVACAO");
	}
}

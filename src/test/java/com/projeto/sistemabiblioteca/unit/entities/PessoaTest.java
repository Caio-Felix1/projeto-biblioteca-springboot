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
	
	@Test
	void deveInstanciarPessoaComStatusAtiva() {		
		Pessoa pessoa = Assertions.assertDoesNotThrow(
				() -> new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null),
				"Era esperado que a instanciação funcionasse com status ATIVA");
		
		Assertions.assertEquals(StatusConta.ATIVA, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse ATIVA");
	}
	
	@Test
	void deveInstanciarPessoaComStatusEmAnaliseAprovacao() {
		Pessoa pessoa = Assertions.assertDoesNotThrow(
				() -> new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null),
				"Era esperado que a instanciação funcionasse com status EM_ANALISE_APROVACAO");
				
		Assertions.assertEquals(StatusConta.EM_ANALISE_APROVACAO, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse EM_ANALISE_APROVACAO");
	}
	
	@Test
	void deveLancarExcecaoParaInstanciacaoComStatusInvalido() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_EXCLUSAO, null),
			"Era esperado que fosse lançada exceção para instanciação com status EM_ANALISE_EXCLUSAO"
		);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> 
			new Pessoa(null, null, null, null, null, null, null, null, StatusConta.INATIVA, null),
			"Era esperado que fosse lançada exceção para instanciação com status INATIVA"
		);
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			new Pessoa(null, null, null, null, null, null, null, null, StatusConta.REJEITADA, null),
			"Era esperado que fosse lançada exceção para instanciação com status REJEITADA"
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
	void deveValidarDataDeNascimento(String dtStringTeste) {
		LocalDate hoje = LocalDate.parse("2020-12-01");
		LocalDate nascimento = LocalDate.parse(dtStringTeste);
		Pessoa pessoa = new Pessoa(null, null, null, null, nascimento, null, null, null, statusContaPadrao, null);
		int idadeMinima = 18;
		
		if (nascimento.isAfter(hoje.minusYears(idadeMinima))) {
			Exception excecao = Assertions.assertThrows(IllegalArgumentException.class, 
					() -> pessoa.validarIdadeMinima(hoje, idadeMinima),
					"Era esperado que fosse lançada exceção para idade inferior a " + idadeMinima + " anos");
			
			Assertions.assertEquals("Erro: usuário deve ter no mínimo " + idadeMinima  + " anos de idade.", excecao.getMessage(),
					"Era esperado que a mensagem da exceção fosse 'Erro: usuário deve ter no mínimo " + idadeMinima + " anos de idade.'");
		}
		else {
			Assertions.assertDoesNotThrow(() -> pessoa.validarIdadeMinima(hoje, idadeMinima),
					"Era esperado que não houvesse erro para idade igual ou maior que " + idadeMinima + " anos");
		}
	}
	
	@Test
	void deveValidarValorDaIdadeMinima() {
		LocalDate hoje = LocalDate.parse("2020-12-01");
		LocalDate nascimento = LocalDate.parse("2002-12-01");
		Pessoa pessoa = new Pessoa(null, null, null, null, nascimento, null, null, null, statusContaPadrao, null);
		int idadeMinima1 = 0;
		int idadeMinima2 = -1;
		
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> pessoa.validarIdadeMinima(hoje, idadeMinima1),
				"Era esperado que fosse lançada exceção para valor da idade mínima igual a 0");
		
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> pessoa.validarIdadeMinima(hoje, idadeMinima2),
				"Era esperado que fosse lançada exceção para valor da idade mínima negativo");
	}
	
	@Test
	void deveInativarContaComStatusValido() {
		Pessoa pessoa1 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		Pessoa pessoa2 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa2.solicitarExclusaoConta();
		
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
	void deveLancarExcecaoAoInativarContaComStatusInvalido() {
		Pessoa pessoa1 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa1.inativarConta();
		Pessoa pessoa2 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		pessoa2.rejeitarConta();
		Pessoa pessoa3 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.inativarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método inativarConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.inativarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método inativarConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.inativarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método inativarConta em um objeto com status EM_ANALISE_APROVACAO");
	}
	
	@Test
	void deveAprovarContaComStatusEmAnaliseAprovacao() {
		Pessoa pessoa = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		
		Assertions.assertDoesNotThrow(() -> pessoa.aprovarConta(),
				"Era esperado que funcionasse o método aprovarConta no objeto com status EM_ANALISE_APROVACAO");
		
		Assertions.assertEquals(StatusConta.ATIVA, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse ATIVA");
	}
	
	@Test
	void deveLancarExcecaoAoAprovarContaComStatusInvalido() {
		Pessoa pessoa1 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa1.inativarConta();
		Pessoa pessoa2 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		pessoa2.rejeitarConta();
		Pessoa pessoa3 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa3.solicitarExclusaoConta();
		Pessoa pessoa4 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.aprovarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método aprovarConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.aprovarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método aprovarConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.aprovarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método aprovarConta em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.aprovarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método aprovarConta em um objeto com status ATIVA");
	}
	
	@Test
	void deveRejeitarContaComStatusEmAnaliseAprovacao() {
		Pessoa pessoa = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		
		Assertions.assertDoesNotThrow(() -> pessoa.rejeitarConta(),
				"Era esperado que funcionasse o método rejeitarConta no objeto com status EM_ANALISE_APROVACAO");
		
		Assertions.assertEquals(StatusConta.REJEITADA, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse REJEITADA");
	}
	
	@Test
	void deveLancarExcecaoAoRejeitarContaComStatusInvalido() {
		Pessoa pessoa1 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa1.inativarConta();
		Pessoa pessoa2 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		pessoa2.rejeitarConta();
		Pessoa pessoa3 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa3.solicitarExclusaoConta();
		Pessoa pessoa4 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.rejeitarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.rejeitarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.rejeitarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarConta em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.rejeitarConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarConta em um objeto com status ATIVA");
	}
	
	@Test
	void deveSolicitarExclusaoContaComStatusAtiva() {
		Pessoa pessoa = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		
		Assertions.assertDoesNotThrow(() -> pessoa.solicitarExclusaoConta(),
				"Era esperado que funcionasse o método solicitarExclusaoConta no objeto com status ATIVA");
		
		Assertions.assertEquals(StatusConta.EM_ANALISE_EXCLUSAO, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse EM_ANALISE_EXCLUSAO");
	}
	
	@Test
	void deveLancarExcecaoAoSolicitarExclusaoContaComStatusInvalido() {
		Pessoa pessoa1 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa1.inativarConta();
		Pessoa pessoa2 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		pessoa2.rejeitarConta();
		Pessoa pessoa3 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa3.solicitarExclusaoConta();
		Pessoa pessoa4 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.solicitarExclusaoConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.solicitarExclusaoConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.solicitarExclusaoConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.solicitarExclusaoConta(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método solicitarExclusaoConta em um objeto com status EM_ANALISE_APROVACAO");
	}
	
	@Test
	void deveRejeitarSolicitacaoExclusaoComStatusEmAnaliseExclusao() {
		Pessoa pessoa = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa.solicitarExclusaoConta();
		
		Assertions.assertDoesNotThrow(() -> pessoa.rejeitarSolicitacaoExclusao(),
				"Era esperado que funcionasse o método rejeitarSolicitacaoExclusao no objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertEquals(StatusConta.ATIVA, pessoa.getStatusConta(),
				"Era esperado que o valor retornado fosse ATIVA");
	}
	
	@Test
	void deveLancarExcecaoAoRejeitarSolicitacaoExclusaoComStatusInvalido() {
		Pessoa pessoa1 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		pessoa1.inativarConta();
		Pessoa pessoa2 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		pessoa2.rejeitarConta();
		Pessoa pessoa3 = new Pessoa(null, null, null, null, null, null, null, null, statusContaPadrao, null);
		Pessoa pessoa4 = new Pessoa(null, null, null, null, null, null, null, null, StatusConta.EM_ANALISE_APROVACAO, null);
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa1.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status INATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa2.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status REJEITADA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa3.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status ATIVA");
		
		Assertions.assertThrows(IllegalStateException.class, () -> pessoa4.rejeitarSolicitacaoExclusao(),
				"Era esperado que fosse lançada exceção ao tentar utilizar o método rejeitarSolicitacaoExclusao em um objeto com status EM_ANALISE_APROVACAO");
	}
}

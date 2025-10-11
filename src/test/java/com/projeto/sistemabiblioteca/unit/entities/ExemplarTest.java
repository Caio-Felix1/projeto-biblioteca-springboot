package com.projeto.sistemabiblioteca.unit.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;

public class ExemplarTest {
	
	private Exemplar criarExemplarComStatusDisponivel() {
		return new Exemplar(null, null);
	}
	
	private Exemplar criarExemplarComStatusAlugado() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.alugar();
		return exemplar;
	}
	
	private Exemplar criarExemplarComStatusEmAnaliseExclusao() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.solicitarExclusao();;
		return exemplar;
	}
	
	private Exemplar criarExemplarComStatusPerdido() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.registrarPerda();
		return exemplar;
	}
	
	private Exemplar criarExemplarComStatusRemovido() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.remover();
		return exemplar;
	}
	
	
	@Test
	void deveInstanciarExemplarComStatusDisponivel() {
		Exemplar exemplar = Assertions.assertDoesNotThrow(
				() -> criarExemplarComStatusDisponivel(),
				"Era esperado que a instanciação funcionasse corretamente");
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, exemplar.getStatus(),
				"Era esperado que o valor retornado fosse DISPONIVEL");
	}
	
	@Test
	void deveAlugarComStatusDisponivel() {
		Exemplar exemplar = criarExemplarComStatusDisponivel();
		
		Assertions.assertDoesNotThrow(() -> exemplar.alugar(),
				"Era esperado que funcionasse o método alugar no objeto com status DISPONIVEL");
		
		Assertions.assertEquals(StatusExemplar.ALUGADO, exemplar.getStatus(),
				"Era esperado que o valor retornado fosse ALUGADO");
	}
	
	@Test
	void deveLancarExcecaoAoAlugarComStatusInvalido() {
		Exemplar exemplar1 = criarExemplarComStatusAlugado();
		Exemplar exemplar2 = criarExemplarComStatusEmAnaliseExclusao();
		Exemplar exemplar3 = criarExemplarComStatusPerdido();
		Exemplar exemplar4 = criarExemplarComStatusRemovido();
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar1.alugar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método alugar em um objeto com status ALUGADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar2.alugar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método alugar em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar3.alugar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método alugar em um objeto com status PERDIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar4.alugar(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método alugar em um objeto com status REMOVIDO");
	}
	
	@Test
	void deveDevolverComStatusValido() {
		Exemplar exemplar1 = criarExemplarComStatusAlugado();
		Exemplar exemplar2 = criarExemplarComStatusPerdido();
		
		Assertions.assertDoesNotThrow(() -> exemplar1.devolver(),
				"Era esperado que funcionasse o método devolver no objeto com status ALUGADO");
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, exemplar1.getStatus(),
				"Era esperado que o valor retornado fosse DISPONIVEL");
		
		Assertions.assertDoesNotThrow(() -> exemplar2.devolver(),
				"Era esperado que funcionasse o método devolver no objeto com status PERDIDO");
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, exemplar2.getStatus(),
				"Era esperado que o valor retornado fosse DISPONIVEL");
	}
	
	@Test
	void deveLancarExcecaoAoDevolverComStatusInvalido() {
		Exemplar exemplar1 = criarExemplarComStatusDisponivel();
		Exemplar exemplar2 = criarExemplarComStatusEmAnaliseExclusao();
		Exemplar exemplar3 = criarExemplarComStatusRemovido();
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar1.devolver(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolver em um objeto com status DISPONIVEL");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar2.devolver(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolver em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar3.devolver(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método devolver em um objeto com status REMOVIDO");
	}
	
	@Test
	void deveRegistrarPerdaComStatusValido() {
		Exemplar exemplar1 = criarExemplarComStatusDisponivel();
		Exemplar exemplar2 = criarExemplarComStatusEmAnaliseExclusao();
		Exemplar exemplar3 = criarExemplarComStatusAlugado();
		
		Assertions.assertDoesNotThrow(() -> exemplar1.registrarPerda(),
				"Era esperado que funcionasse o método registrarPerda no objeto com status DISPONIVEL");
		
		Assertions.assertEquals(StatusExemplar.PERDIDO, exemplar1.getStatus(),
				"Era esperado que o valor retornado fosse PERDIDO");
		
		Assertions.assertDoesNotThrow(() -> exemplar2.registrarPerda(),
				"Era esperado que funcionasse o método registrarPerda no objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertEquals(StatusExemplar.PERDIDO, exemplar2.getStatus(),
				"Era esperado que o valor retornado fosse PERDIDO");
		
		Assertions.assertDoesNotThrow(() -> exemplar3.registrarPerda(),
				"Era esperado que funcionasse o método registrarPerda no objeto com status ALUGADO");
		
		Assertions.assertEquals(StatusExemplar.PERDIDO, exemplar3.getStatus(),
				"Era esperado que o valor retornado fosse PERDIDO");
	}
	
	@Test
	void deveLancarExcecaoAoRegistrarPerdaComStatusInvalido() {
		Exemplar exemplar1 = criarExemplarComStatusPerdido();
		Exemplar exemplar2 = criarExemplarComStatusRemovido();
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar1.registrarPerda(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarPerda em um objeto com status PERDIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar2.registrarPerda(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método registrarPerda em um objeto com status REMOVIDO");	
	}
	
	@Test
	void deveSolicitarExclusaoComStatusDisponivel() {
		Exemplar exemplar = criarExemplarComStatusDisponivel();
		
		Assertions.assertDoesNotThrow(() -> exemplar.solicitarExclusao(),
				"Era esperado que funcionasse o método solicitarExclusao no objeto com status DISPONIVEL");
		
		Assertions.assertEquals(StatusExemplar.EM_ANALISE_EXCLUSAO, exemplar.getStatus(),
				"Era esperado que o valor retornado fosse EM_ANALISE_EXCLUSAO");
	}
	
	@Test
	void deveLancarExcecaoAoSolicitarExclusaoComStatusInvalido() {
		Exemplar exemplar1 = criarExemplarComStatusAlugado();
		Exemplar exemplar2 = criarExemplarComStatusEmAnaliseExclusao();
		Exemplar exemplar3 = criarExemplarComStatusPerdido();
		Exemplar exemplar4 = criarExemplarComStatusRemovido();
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar1.solicitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusao em um objeto com status ALUGADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar2.solicitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusao em um objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar3.solicitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusao em um objeto com status PERDIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar4.solicitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método solicitarExclusao em um objeto com status REMOVIDO");
	}
	
	@Test
	void deveRejeitarExclusaoComStatusEmAnaliseExclusao() {
		Exemplar exemplar = criarExemplarComStatusEmAnaliseExclusao();
		
		Assertions.assertDoesNotThrow(() -> exemplar.rejeitarExclusao(),
				"Era esperado que funcionasse o método rejeitarExclusao no objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, exemplar.getStatus(),
				"Era esperado que o valor retornado fosse DISPONIVEL");
	}
	
	@Test
	void deveLancarExcecaoAoRejeitarExclusaoComStatusInvalido() {
		Exemplar exemplar1 = criarExemplarComStatusDisponivel();
		Exemplar exemplar2 = criarExemplarComStatusAlugado();
		Exemplar exemplar3 = criarExemplarComStatusPerdido();
		Exemplar exemplar4 = criarExemplarComStatusRemovido();
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar1.rejeitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarExclusao em um objeto com status DISPONIVEL");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar2.rejeitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarExclusao em um objeto com status ALUGADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar3.rejeitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarExclusao em um objeto com status PERDIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar4.rejeitarExclusao(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método rejeitarExclusao em um objeto com status REMOVIDO");
	}
	
	@Test
	void deveRemoverComStatusValido() {
		Exemplar exemplar1 = criarExemplarComStatusDisponivel();
		Exemplar exemplar2 = criarExemplarComStatusEmAnaliseExclusao();
		
		Assertions.assertDoesNotThrow(() -> exemplar1.remover(),
				"Era esperado que funcionasse o método remover no objeto com status DISPONIVEL");
		
		Assertions.assertEquals(StatusExemplar.REMOVIDO, exemplar1.getStatus(),
				"Era esperado que o valor retornado fosse REMOVIDO");
		
		Assertions.assertDoesNotThrow(() -> exemplar2.remover(),
				"Era esperado que funcionasse o método remover no objeto com status EM_ANALISE_EXCLUSAO");
		
		Assertions.assertEquals(StatusExemplar.REMOVIDO, exemplar2.getStatus(),
				"Era esperado que o valor retornado fosse REMOVIDO");
	}
	
	@Test
	void deveLancarExcecaoAoRemoverComStatusInvalido() {
		Exemplar exemplar1 = criarExemplarComStatusAlugado();
		Exemplar exemplar2 = criarExemplarComStatusPerdido();
		Exemplar exemplar3 = criarExemplarComStatusRemovido();
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar1.remover(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método remover em um objeto com status ALUGADO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar2.remover(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método remover em um objeto com status PERDIDO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> exemplar3.remover(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método remover em um objeto com status REMOVIDO");
	}
}

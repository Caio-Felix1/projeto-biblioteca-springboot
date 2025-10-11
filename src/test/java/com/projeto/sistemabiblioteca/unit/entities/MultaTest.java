package com.projeto.sistemabiblioteca.unit.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

public class MultaTest {
	
	private Multa criarMultaComStatusPagamentoNaoAplicavel() {
		return Multa.criarMultaVazia();
	}
	
	private Multa criarMultaComStatusPagamentoPendente() {
		Multa multa = Multa.criarMultaVazia();
		multa.aplicarMultaPorPerda();
		return multa;
	}
	
	private Multa criarMultaComStatusPagamentoPago() {
		Multa multa = Multa.criarMultaVazia();
		multa.aplicarMultaPorPerda();
		multa.pagarMulta();
		return multa;
	}
	
	private Multa criarMultaComStatusPagamentoPerdoado() {
		Multa multa = Multa.criarMultaVazia();
		multa.aplicarMultaPorPerda();
		multa.perdoarMulta();
		return multa;
	}
	
	@Test
	void deveInstanciarMultaComStatusPagamentoNaoAplicavelEComValorZero() {
		Multa multa = Assertions.assertDoesNotThrow(
				() -> criarMultaComStatusPagamentoNaoAplicavel(),
				"Era esperado que a instanciação funcionasse corretamente");
		
		Assertions.assertEquals(StatusPagamento.NAO_APLICAVEL, multa.getStatusPagamento(),
				"Era esperado que o valor retornado fosse NAO_APLICAVEL");
		
		Assertions.assertEquals(0.0, multa.getValor(),
				"Era esperado que o valor retornado fosse 0.0");
	}
	
	@Test
	void deveAplicarMultaPorPerdaComStatusPagamentoNaoAplicavel() {
		Multa multa = criarMultaComStatusPagamentoNaoAplicavel();
		
		Assertions.assertDoesNotThrow(() -> multa.aplicarMultaPorPerda(),
				"Era esperado que funcionasse o método aplicarMultaPorPerda no objeto com status NAO_APLICAVEL");
		
		Assertions.assertEquals(StatusPagamento.PENDENTE, multa.getStatusPagamento(),
				"Era esperado que o valor retornado fosse PENDENTE");
		
		Assertions.assertEquals(50.0, multa.getValor(),
				"Era esperado que o valor retornado fosse 50.0");
	}
	
	@Test
	void deveLancarExcecaoAoAplicarMultaPorPerdaComStatusInvalido() {
		Multa multa1 = criarMultaComStatusPagamentoPendente();
		Multa multa2 = criarMultaComStatusPagamentoPago();
		Multa multa3 = criarMultaComStatusPagamentoPerdoado();
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa1.aplicarMultaPorPerda(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMultaPorPerda em um objeto com status PENDENTE");
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa2.aplicarMultaPorPerda(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMultaPorPerda em um objeto com status PAGO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa3.aplicarMultaPorPerda(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMultaPorPerda em um objeto com status PERDOADO");
	}
	
	@Test
	void deveCalcularMultaPorPerda() {
		Multa multa = criarMultaComStatusPagamentoNaoAplicavel();
		
		Assertions.assertEquals(50.0, multa.calcularMultaPorPerda(),
				"Era esperado que o valor retornado fosse 50.0");
	}
	
	@Test
	void deveAplicarMultaComStatusValido() {
		Multa multa = criarMultaComStatusPagamentoNaoAplicavel();
		
		Assertions.assertDoesNotThrow(() -> multa.aplicarMulta(1),
				"Era esperado que funcionasse o método aplicarMulta no objeto com status NAO_APLICAVEL");
		
		Assertions.assertEquals(StatusPagamento.PENDENTE, multa.getStatusPagamento(),
				"Era esperado que o valor retornado fosse PENDENTE");
		
		Assertions.assertEquals(1.0, multa.getValor(),
				"Era esperado que o valor retornado fosse 1.0");
		
		Assertions.assertDoesNotThrow(() -> multa.aplicarMulta(51),
				"Era esperado que funcionasse o método aplicarMulta no objeto com status PENDENTE");
		
		Assertions.assertEquals(StatusPagamento.PENDENTE, multa.getStatusPagamento(),
				"Era esperado que o valor retornado fosse PENDENTE");
		
		Assertions.assertEquals(50.0, multa.getValor(),
				"Era esperado que o valor retornado fosse 50.0");
	}
	
	@Test
	void deveLancarExcecaoAoAplicarMultaComStatusInvalido() {
		Multa multa1 = criarMultaComStatusPagamentoPago();
		Multa multa2 = criarMultaComStatusPagamentoPerdoado();
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa1.aplicarMulta(1),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMulta em um objeto com status PAGO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa2.aplicarMulta(1),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMulta em um objeto com status PERDOADO");
	}
	
	@Test
	void deveLancarExcecaoAoAplicarMultaComValorDeDiasMenorOuIgualAZero() {
		Multa multa = criarMultaComStatusPagamentoNaoAplicavel();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> multa.aplicarMulta(-1),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMulta com valor de dias negativo");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> multa.aplicarMulta(0),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMulta com valor de dias igual a zero");
	}
	
	@Test
	void deveLancarExcecaoAoAplicarMultaComValorDiminuindo() {
		Multa multa = criarMultaComStatusPagamentoNaoAplicavel();
		multa.aplicarMulta(20);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> multa.aplicarMulta(19),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método aplicarMulta com o número de dias menor que o anterior");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			1,
			20,
			35,
			50,
			100,
			200
	})
	void deveCalcularMultaDiariaComValorDeDiasValido(int valorIntTeste) {
		Multa multa = criarMultaComStatusPagamentoNaoAplicavel();
		
		Assertions.assertEquals(valorIntTeste, multa.calcularMultaDiaria(valorIntTeste),
				"Era esperado que o valor retornado fosse " + valorIntTeste);
	}
	
	@Test
	void deveLancarExcecaoAoCalcularMultaDiariaComValorDeDiasMenorOuIgualAZero() {
		Multa multa = criarMultaComStatusPagamentoNaoAplicavel();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> multa.calcularMultaDiaria(-1),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularMultaDiaria com valor de dias negativo");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> multa.calcularMultaDiaria(0),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método calcularMultaDiaria com valor de dias igual a zero");
	}
	
	@Test
	void devePagarMultaComStatusPagamentoPendente() {
		Multa multa = criarMultaComStatusPagamentoPendente();
		
		Assertions.assertDoesNotThrow(() -> multa.pagarMulta(),
				"Era esperado que funcionasse o método pagarMulta no objeto com status PENDENTE");
		
		Assertions.assertEquals(StatusPagamento.PAGO, multa.getStatusPagamento(),
				"Era esperado que o valor retornado fosse PAGO");
	}
	
	@Test
	void deveLancarExcecaoAoPagarMultaComStatusPagamentoInvalido() {
		Multa multa1 = criarMultaComStatusPagamentoNaoAplicavel();
		Multa multa2 = criarMultaComStatusPagamentoPago();
		Multa multa3 = criarMultaComStatusPagamentoPerdoado();
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa1.pagarMulta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um objeto com status NAO_APLICAVEL");
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa2.pagarMulta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um objeto com status PAGO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa3.pagarMulta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método pagarMulta em um objeto com status PERDOADO");
	}
	
	@Test
	void devePerdoarMultaComStatusPagamentoPendente() {
		Multa multa = criarMultaComStatusPagamentoPendente();
		
		Assertions.assertDoesNotThrow(() -> multa.perdoarMulta(),
				"Era esperado que funcionasse o método perdoarMulta no objeto com status PENDENTE");
		
		Assertions.assertEquals(StatusPagamento.PERDOADO, multa.getStatusPagamento(),
				"Era esperado que o valor retornado fosse PERDOADO");
	}
	
	@Test
	void deveLancarExcecaoAoPerdoarMultaComStatusPagamentoInvalido() {
		Multa multa1 = criarMultaComStatusPagamentoNaoAplicavel();
		Multa multa2 = criarMultaComStatusPagamentoPago();
		Multa multa3 = criarMultaComStatusPagamentoPerdoado();
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa1.perdoarMulta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método perdoarMulta em um objeto com status NAO_APLICAVEL");
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa2.perdoarMulta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método perdoarMulta em um objeto com status PAGO");
		
		Assertions.assertThrows(IllegalStateException.class, () -> multa3.perdoarMulta(),
				"Era esperado que fosse lançada uma exceção ao tentar utilizar o método perdoarMulta em um objeto com status PERDOADO");
	}
}

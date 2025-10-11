package com.projeto.sistemabiblioteca.unit.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.exceptions.CpfInvalidoException;
import com.projeto.sistemabiblioteca.validation.Cpf;

public class CpfTest {
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345678901", 
			"00000000000", 
			"98765432100", 
			"11122233344", 
			"36925814703"
	})
	void deveInstanciarCpfComValorValido(String cpfStringTeste) {
		Cpf cpf = Assertions.assertDoesNotThrow(
				() -> new Cpf(cpfStringTeste),
				"Era esperado que a instanciação funcionasse com o valor " + cpfStringTeste);
		
		Assertions.assertEquals(cpfStringTeste, cpf.getValor(),
				"Era esperado que o valor retornado fosse " + cpfStringTeste);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"123.456.789-09", 
			"1234567890", 
			"123456789012", 
			"abcdefghijk", 
			"12345 678901"
	})
	void deveLancarExcecaoAoInstanciarCpfComValorInvalido(String cpfStringTeste) {
		Assertions.assertThrows(CpfInvalidoException.class, 
				() -> new Cpf(cpfStringTeste),
				"Era esperado que fosse lançada uma exceção para instanciação com o valor " + cpfStringTeste);
	}
}

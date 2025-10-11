package com.projeto.sistemabiblioteca.unit.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.exceptions.TelefoneInvalidoException;
import com.projeto.sistemabiblioteca.validation.Telefone;

public class TelefoneTest {
	
	@ParameterizedTest
	@ValueSource(strings = {
			"1123456789", 
			"11987654321", 
			"21900011122", 
			"31912345678", 
			"99988877766"
	})
	void deveInstanciarTelefoneComNumeroValido(String numeroStringTeste) {
		Telefone telefone = Assertions.assertDoesNotThrow(
				() -> new Telefone(numeroStringTeste),
				"Era esperado que a instanciação funcionasse com o número " + numeroStringTeste);
		
		Assertions.assertEquals(numeroStringTeste, telefone.getNumero(),
				"Era esperado que o valor retornado fosse " + numeroStringTeste);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"(11)2345-6789", 
			"11 234567890", 
			"123456789", 
			"123456789012", 
			"11-98765-4321"
	})
	void deveLancarExcecaoAoInstanciarTelefoneComNumeroInvalido(String numeroStringTeste) {
		Assertions.assertThrows(TelefoneInvalidoException.class, 
				() -> new Telefone(numeroStringTeste),
				"Era esperado que fosse lançada uma exceção para instanciação com o número " + numeroStringTeste);
	}
}

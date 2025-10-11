package com.projeto.sistemabiblioteca.unit.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.exceptions.EmailInvalidoException;
import com.projeto.sistemabiblioteca.validation.Email;

public class EmailTest {
	
	@ParameterizedTest
	@ValueSource(strings = {
			"usuario1@empresa.com",
			"a@b.com", "teste123@dominio.com.br", 
			"mail9@site.com", 
			"abc123@exemplo.com.br"
	})
	void deveInstanciarEmailComEnderecoValido(String emailStringTeste) {
		Email email = Assertions.assertDoesNotThrow(
				() -> new Email(emailStringTeste),
				"Era esperado que a instanciação funcionasse com o endereço " + emailStringTeste);
		
		Assertions.assertEquals(emailStringTeste, email.getEndereco(),
				"Era esperado que o valor retornado fosse " + emailStringTeste);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"User@site.com", 
			"user.name@site.com", 
			"user@site.org", 
			"user@site123.com", 
			"user@sub.site.com"
	})
	void deveLancarExcecaoAoInstanciarEmailComEnderecoInvalido(String emailStringTeste) {
		Assertions.assertThrows(EmailInvalidoException.class, 
				() -> new Email(emailStringTeste),
				"Era esperado que fosse lançada uma exceção para instanciação com o endereço " + emailStringTeste);
	}
}

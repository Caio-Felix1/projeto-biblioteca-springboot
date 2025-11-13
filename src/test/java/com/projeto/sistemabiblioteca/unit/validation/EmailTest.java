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
			"a@b.com", 
			"teste123@dominio.com.br", 
			"mail9@site.com", 
			"abc123@exemplo.com.br",
			"user@sub.site.com",
			"teste123@empresa.com.br",
			"john.doe@meusite.com",
			"ANA_SILVA@biblioteca.com.br",
			"dev+tag@provedor.com"
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
			"usuario@gmail.org", 
			"teste@empresa.net", 
			"john@dominio.com.us", 
			"ana@biblioteca.br", 
			"dev@provedor.cjfsakjfiqjasfjka", 
			"user@site.edu", 
			"contato@governo.gov.br", 
			"teste@empresa.io", 
			"pessoa@dominio.co.uk", 
			"aluno@universidade.edu.br"
	})
	void deveLancarExcecaoAoInstanciarEmailComEnderecoInvalido(String emailStringTeste) {
		Assertions.assertThrows(EmailInvalidoException.class, 
				() -> new Email(emailStringTeste),
				"Era esperado que fosse lançada uma exceção para instanciação com o endereço " + emailStringTeste);
	}
}

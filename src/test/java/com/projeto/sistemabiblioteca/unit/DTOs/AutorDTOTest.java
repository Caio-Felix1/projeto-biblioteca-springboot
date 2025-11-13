package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.AutorDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AutorDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"João Silva",
			"Maria de Souza",
			"Jean-Paul Sartre",
			"O'Connor",
			"George R. R. Martin"
	})
	void deveFuncionarAoInserirNomeDoAutorValido(String nomeValido) {
		AutorDTO autorDTO = new AutorDTO(nomeValido);
		
		Set<ConstraintViolation<AutorDTO>> violacoes = validator.validate(autorDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"João123",
			"Maria@Souza",
			"#AutorDesconhecido",
			"Silva*Pereira",
			"12345"
	})
	void deveRetornarErroAoInserirNomeDoAutorInvalido(String nomeInvalido) {
		AutorDTO autorDTO = new AutorDTO(nomeInvalido);
		
		Set<ConstraintViolation<AutorDTO>> violacoes = validator.validate(autorDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do autor deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"João da Silva Pereira de Albuquerque Monteiro Fernandes dos Santos Oliveira Costa e Souza Machado de Assis Filho Neto",
			"Maria Clara de Andrade Rodrigues da Silva Souza Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa e Lima",
			"José Antônio de Almeida Barros da Silva Souza Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa e Ramos",
			"Ana Beatriz de Oliveira Costa Souza Pereira Albuquerque Monteiro Fernandes dos Santos Rodrigues da Silva Machado de Assis",
			"Fernando Henrique de Souza Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa e Silva Rodrigues de Lima"
	})
	void deveRetornarErroAoInserirNomeDoAutorQueUltrapassaOMaximoDeCaracteres(String nomeInvalido) {
		AutorDTO autorDTO = new AutorDTO(nomeInvalido);
		
		Set<ConstraintViolation<AutorDTO>> violacoes = validator.validate(autorDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do autor deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirNomeDoAutorComStringVaziaOuComApenasEspacos(String nomeInvalido) {
		AutorDTO autorDTO = new AutorDTO(nomeInvalido);
		
		Set<ConstraintViolation<AutorDTO>> violacoes = validator.validate(autorDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome do autor é obrigatório")));
	}
}

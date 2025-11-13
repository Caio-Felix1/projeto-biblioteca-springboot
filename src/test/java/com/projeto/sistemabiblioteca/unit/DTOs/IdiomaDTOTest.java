package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.IdiomaDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class IdiomaDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Português",
			"Inglês",
			"Espanhol",
			"Alemão",
			"Francês Canadense"
	})
	void deveFuncionarAoInserirNomeDoIdiomaValido(String nomeValido) {
		IdiomaDTO idiomaDTO = new IdiomaDTO(nomeValido);
		
		Set<ConstraintViolation<IdiomaDTO>> violacoes = validator.validate(idiomaDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345",
			"Inglês@Avançado",
			"###",
			"Port/uguês",
			"---"
	})
	void deveRetornarErroAoInserirNomeDoIdiomaInvalido(String nomeInvalido) {
		IdiomaDTO idiomaDTO = new IdiomaDTO(nomeInvalido);
		
		Set<ConstraintViolation<IdiomaDTO>> violacoes = validator.validate(idiomaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do idioma deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Português Brasileiro com Influências Regionais do Nordeste Sul Sudeste e Centro-Oeste em Diversos Dialetos e Variações",
			"Inglês Americano Britânico Australiano Canadense e Sul-Africano com Todas as Suas Variações Dialetais e Regionais",
			"Espanhol Europeu Mexicano Argentino Colombiano Chileno e Peruano com Extensas Variações Linguísticas e Culturais",
			"Alemão Padrão Austríaco Suíço e Dialetos Regionais com Influências Históricas e Culturais em Diferentes Regiões",
			"Francês Europeu Canadense Haitiano Africano e Suíço com Diversas Variações Dialetais e Regionais ao Longo da História"
	})
	void deveRetornarErroAoInserirNomeDoIdiomaQueUltrapassaOMaximoDeCaracteres(String nomeInvalido) {
		IdiomaDTO idiomaDTO = new IdiomaDTO(nomeInvalido);
		
		Set<ConstraintViolation<IdiomaDTO>> violacoes = validator.validate(idiomaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do idioma deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirNomeDoIdiomaComStringVaziaOuComApenasEspacos(String nomeInvalido) {
		IdiomaDTO idiomaDTO = new IdiomaDTO(nomeInvalido);
		
		Set<ConstraintViolation<IdiomaDTO>> violacoes = validator.validate(idiomaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome do idioma é obrigatório")));
	}
}

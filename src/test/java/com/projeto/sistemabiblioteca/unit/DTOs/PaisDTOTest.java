package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.PaisDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PaisDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Brasil",
			"Estados Unidos",
			"França",
			"Alemanha",
			"Reino Unido"
	})
	void deveFuncionarAoInserirNomeDoPaisValido(String nomeValido) {
		PaisDTO paisDTO = new PaisDTO(nomeValido);
		
		Set<ConstraintViolation<PaisDTO>> violacoes = validator.validate(paisDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345",
			"Brasil@",
			"###",
			"França/Europa",
			"---"
	})
	void deveRetornarErroAoInserirNomeDoPaisInvalido(String nomeInvalido) {
		PaisDTO paisDTO = new PaisDTO(nomeInvalido);
		
		Set<ConstraintViolation<PaisDTO>> violacoes = validator.validate(paisDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do país deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"República Federativa do Brasil composta por diversos estados regiões e territórios com grande diversidade cultural e histórica",
			"Estados Unidos da América com cinquenta estados territórios associados e uma vasta extensão territorial e populacional",
			"Reino Unido da Grã-Bretanha e Irlanda do Norte incluindo Inglaterra Escócia País de Gales e Irlanda do Norte oficialmente",
			"República Democrática e Popular fictícia João da Silva Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa",
			"Confederação Internacional de Países Unidos em Cooperação Econômica Cultural Científica e Tecnológica com Extensão Global"
	})
	void deveRetornarErroAoInserirNomeDoPaisQueUltrapassaOMaximoDeCaracteres(String nomeInvalido) {
		PaisDTO paisDTO = new PaisDTO(nomeInvalido);
		
		Set<ConstraintViolation<PaisDTO>> violacoes = validator.validate(paisDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do país deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirNomeDoPaisComStringVaziaOuComApenasEspacos(String nomeInvalido) {
		PaisDTO paisDTO = new PaisDTO(nomeInvalido);
		
		Set<ConstraintViolation<PaisDTO>> violacoes = validator.validate(paisDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome do país é obrigatório")));
	}
}

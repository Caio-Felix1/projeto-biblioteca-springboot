package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.DTOs.TituloAutoresDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class TituloAutoresDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void deveFuncionarAoInserirIdsAutoresValido() {
		TituloAutoresDTO tituloAutoresDTO = new TituloAutoresDTO(Set.of(1L));
		
		Set<ConstraintViolation<TituloAutoresDTO>> violacoes = validator.validate(tituloAutoresDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@Test
	void deveRetornarErroAoInserirIdsAutoresInvalido() {
		TituloAutoresDTO tituloAutoresDTO = new TituloAutoresDTO(Set.of());
		
		Set<ConstraintViolation<TituloAutoresDTO>> violacoes = validator.validate(tituloAutoresDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idsAutores")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("É necessário inserir pelo menos um autor")));
	}
	
	@Test
	void deveRetornarErroAoInserirIdsAutoresNulo() {
		TituloAutoresDTO tituloAutoresDTO = new TituloAutoresDTO(null);
		
		Set<ConstraintViolation<TituloAutoresDTO>> violacoes = validator.validate(tituloAutoresDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idsAutores")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("É necessário inserir pelo menos um autor")));
	}
}

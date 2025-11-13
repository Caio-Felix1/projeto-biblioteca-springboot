package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.DTOs.TituloCategoriasDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class TituloCategoriasDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void deveFuncionarAoInserirIdsCategoriasValido() {
		TituloCategoriasDTO tituloCategoriasDTO = new TituloCategoriasDTO(Set.of(1L));
		
		Set<ConstraintViolation<TituloCategoriasDTO>> violacoes = validator.validate(tituloCategoriasDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@Test
	void deveRetornarErroAoInserirIdsCategoriasInvalido() {
		TituloCategoriasDTO tituloCategoriasDTO = new TituloCategoriasDTO(Set.of());
		
		Set<ConstraintViolation<TituloCategoriasDTO>> violacoes = validator.validate(tituloCategoriasDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idsCategorias")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("É necessário inserir pelo menos uma categoria")));
	}
	
	@Test
	void deveRetornarErroAoInserirIdsCategoriasNulo() {
		TituloCategoriasDTO tituloCategoriasDTO = new TituloCategoriasDTO(null);
		
		Set<ConstraintViolation<TituloCategoriasDTO>> violacoes = validator.validate(tituloCategoriasDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idsCategorias")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("É necessário inserir pelo menos uma categoria")));
	}
}

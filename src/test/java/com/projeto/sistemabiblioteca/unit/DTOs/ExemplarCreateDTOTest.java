package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.ExemplarCreateDTO;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ExemplarCreateDTOTest {
	
	private Validator validator;
	
	private final EstadoFisico estadoFisico = EstadoFisico.EXCELENTE;
	private final Integer qtdEstoque = 10;
	private final Long edicaoId = 1L;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void deveRetornarErroAoInserirEstadoFisicoNulo() {
		ExemplarCreateDTO exemplarCreateDTO = new ExemplarCreateDTO(
				null, 
				qtdEstoque, 
				edicaoId);
		
		Set<ConstraintViolation<ExemplarCreateDTO>> violacoes = validator.validate(exemplarCreateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("estadoFisico")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Estado físico é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			1,
			50,
			100
	})
	void deveFuncionarAoInserirQuantidadeEstoqueValida(Integer qtdEstoqueValida) {
		ExemplarCreateDTO exemplarCreateDTO = new ExemplarCreateDTO(
				estadoFisico, 
				qtdEstoqueValida, 
				edicaoId);
		
		Set<ConstraintViolation<ExemplarCreateDTO>> violacoes = validator.validate(exemplarCreateDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			-1,
			0
	})
	void deveRetornarErroAoInserirQuantidadeEstoqueMenorQueOValorMinimo(Integer qtdEstoqueInvalida) {
		ExemplarCreateDTO exemplarCreateDTO = new ExemplarCreateDTO(
				estadoFisico, 
				qtdEstoqueInvalida, 
				edicaoId);
		
		Set<ConstraintViolation<ExemplarCreateDTO>> violacoes = validator.validate(exemplarCreateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("qtdEstoque")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A quantidade mínima de estoque é 1")));
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			101,
			102
	})
	void deveRetornarErroAoInserirQuantidadeEstoqueMaiorQueOValorMaximo(Integer qtdEstoqueInvalida) {
		ExemplarCreateDTO exemplarCreateDTO = new ExemplarCreateDTO(
				estadoFisico, 
				qtdEstoqueInvalida, 
				edicaoId);
		
		Set<ConstraintViolation<ExemplarCreateDTO>> violacoes = validator.validate(exemplarCreateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("qtdEstoque")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A quantidade máxima permitida por requisição é 100")));
	}
	
	@Test
	void deveRetornarErroAoInserirEdicaoIdNulo() {
		ExemplarCreateDTO exemplarCreateDTO = new ExemplarCreateDTO(
				estadoFisico, 
				qtdEstoque, 
				null);
		
		Set<ConstraintViolation<ExemplarCreateDTO>> violacoes = validator.validate(exemplarCreateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edicaoId")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Edição é obrigatória")));
	}
}

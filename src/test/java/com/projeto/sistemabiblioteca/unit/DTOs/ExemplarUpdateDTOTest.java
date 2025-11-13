package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.DTOs.ExemplarUpdateDTO;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ExemplarUpdateDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void deveFuncionarAoInserirDadosValidos() {
		ExemplarUpdateDTO exemplarUpdateDTO = new ExemplarUpdateDTO(EstadoFisico.EXCELENTE, 1L);
		
		Set<ConstraintViolation<ExemplarUpdateDTO>> violacoes = validator.validate(exemplarUpdateDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@Test
	void deveRetornarErroAoInserirEstadoFisicoNulo() {
		ExemplarUpdateDTO exemplarUpdateDTO = new ExemplarUpdateDTO(null, 1L);
		
		Set<ConstraintViolation<ExemplarUpdateDTO>> violacoes = validator.validate(exemplarUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("estadoFisico")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Estado físico é obrigatório")));
	}
	
	@Test
	void deveRetornarErroAoInserirEdicaoIdNulo() {
		ExemplarUpdateDTO exemplarUpdateDTO = new ExemplarUpdateDTO(EstadoFisico.EXCELENTE, null);
		
		Set<ConstraintViolation<ExemplarUpdateDTO>> violacoes = validator.validate(exemplarUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("edicaoId")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Edição é obrigatória")));
	}
}

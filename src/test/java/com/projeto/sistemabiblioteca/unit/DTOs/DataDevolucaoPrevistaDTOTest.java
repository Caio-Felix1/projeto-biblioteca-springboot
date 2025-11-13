package com.projeto.sistemabiblioteca.unit.DTOs;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.DTOs.DataDevolucaoPrevistaDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class DataDevolucaoPrevistaDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void deveFuncionarAoInserirDataDeDevolucaoPrevistaValida() {
		DataDevolucaoPrevistaDTO dataDevolucaoPrevistaDTO1 = new DataDevolucaoPrevistaDTO(LocalDate.now().plusDays(1));
		DataDevolucaoPrevistaDTO dataDevolucaoPrevistaDTO2 = new DataDevolucaoPrevistaDTO(LocalDate.now().plusYears(1));
		
		Set<ConstraintViolation<DataDevolucaoPrevistaDTO>> violacoes1 = validator.validate(dataDevolucaoPrevistaDTO1);
		Set<ConstraintViolation<DataDevolucaoPrevistaDTO>> violacoes2 = validator.validate(dataDevolucaoPrevistaDTO2);
		
		Assertions.assertTrue(violacoes1.isEmpty());
		Assertions.assertTrue(violacoes2.isEmpty());
	}
	
	@Test
	void deveRetornarErroAoInserirDataDeDevolucaoPrevistaInvalida() {
		DataDevolucaoPrevistaDTO dataDevolucaoPrevistaDTO1 = new DataDevolucaoPrevistaDTO(LocalDate.now());
		DataDevolucaoPrevistaDTO dataDevolucaoPrevistaDTO2 = new DataDevolucaoPrevistaDTO(LocalDate.now().minusDays(1));
		
		Set<ConstraintViolation<DataDevolucaoPrevistaDTO>> violacoes1 = validator.validate(dataDevolucaoPrevistaDTO1);
		Set<ConstraintViolation<DataDevolucaoPrevistaDTO>> violacoes2 = validator.validate(dataDevolucaoPrevistaDTO2);
		
		Assertions.assertFalse(violacoes1.isEmpty());
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtDevolucaoPrevista")));
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getMessage().equals("A data de devolução prevista deve ser no futuro")));
		
		Assertions.assertFalse(violacoes2.isEmpty());
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtDevolucaoPrevista")));
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getMessage().equals("A data de devolução prevista deve ser no futuro")));
	}
	
	@Test
	void deveRetornarErroAoInserirDataDeDevolucaoPrevistaNula() {
		DataDevolucaoPrevistaDTO dataDevolucaoPrevistaDTO = new DataDevolucaoPrevistaDTO(null);
		
		Set<ConstraintViolation<DataDevolucaoPrevistaDTO>> violacoes = validator.validate(dataDevolucaoPrevistaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtDevolucaoPrevista")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Deve ser informado a data de devolução prevista")));
	}
}

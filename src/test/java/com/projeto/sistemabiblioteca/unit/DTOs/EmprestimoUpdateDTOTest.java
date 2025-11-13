package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.DTOs.EmprestimoUpdateDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EmprestimoUpdateDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void deveRetornarErroAoInserirIdPessoaNulo() {
		EmprestimoUpdateDTO emprestimoUpdateDTO = new EmprestimoUpdateDTO(null, 1L);
		
		Set<ConstraintViolation<EmprestimoUpdateDTO>> violacoes = validator.validate(emprestimoUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idPessoa")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Usuário é obrigatório")));
	}
	
	@Test
	void deveRetornarErroAoInserirIdExemplarNulo() {
		EmprestimoUpdateDTO emprestimoUpdateDTO = new EmprestimoUpdateDTO(1L, null);
		
		Set<ConstraintViolation<EmprestimoUpdateDTO>> violacoes = validator.validate(emprestimoUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idExemplar")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Exemplar é obrigatório")));
	}
}

package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.DTOs.EmprestimoCreateDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EmprestimoCreateDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void deveRetornarErroAoInserirIdPessoaNulo() {
		EmprestimoCreateDTO emprestimoCreateDTO = new EmprestimoCreateDTO(null, List.of(1L));
		
		Set<ConstraintViolation<EmprestimoCreateDTO>> violacoes = validator.validate(emprestimoCreateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idPessoa")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Usuário é obrigatório")));
	}
	
	@Test
	void deveFuncionarAoInserirIdsEdicaoComQuantidadeValida() {
		EmprestimoCreateDTO emprestimoCreateDTO1 = new EmprestimoCreateDTO(1L, List.of(1L));
		
		EmprestimoCreateDTO emprestimoCreateDTO2 = new EmprestimoCreateDTO(2L, List.of(1L, 2L, 3L, 4L, 5L));
		
		Set<ConstraintViolation<EmprestimoCreateDTO>> violacoes1 = validator.validate(emprestimoCreateDTO1);
		Set<ConstraintViolation<EmprestimoCreateDTO>> violacoes2 = validator.validate(emprestimoCreateDTO2);
		
		Assertions.assertTrue(violacoes1.isEmpty());
		Assertions.assertTrue(violacoes2.isEmpty());
	}
	
	@Test
	void deveRetornarErroAoInserirIdsEdicaoComQuantidadeInvalida() {
		EmprestimoCreateDTO emprestimoCreateDTO1 = new EmprestimoCreateDTO(1L, List.of());
		
		EmprestimoCreateDTO emprestimoCreateDTO2 = new EmprestimoCreateDTO(2L, List.of(1L, 2L, 3L, 4L, 5L, 6L));
		
		Set<ConstraintViolation<EmprestimoCreateDTO>> violacoes1 = validator.validate(emprestimoCreateDTO1);
		Set<ConstraintViolation<EmprestimoCreateDTO>> violacoes2 = validator.validate(emprestimoCreateDTO2);
		
		Assertions.assertFalse(violacoes1.isEmpty());
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idsEdicao")));
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getMessage().equals("O pedido deve ter entre 1 e 5 edições")));
		
		Assertions.assertFalse(violacoes2.isEmpty());
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idsEdicao")));
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getMessage().equals("O pedido deve ter entre 1 e 5 edições")));
	}
	
	@Test
	void deveRetornarErroAoInserirIdsEdicaoNulo() {
		EmprestimoCreateDTO emprestimoCreateDTO = new EmprestimoCreateDTO(1L, null);
		
		Set<ConstraintViolation<EmprestimoCreateDTO>> violacoes = validator.validate(emprestimoCreateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idsEdicao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("É obrigatório ter pelo menos uma edição")));
	}
}

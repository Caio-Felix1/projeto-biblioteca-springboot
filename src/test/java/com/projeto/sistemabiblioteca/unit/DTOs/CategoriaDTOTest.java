package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.CategoriaDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CategoriaDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Ficção Científica",
			"Romance Histórico",
			"Biografia",
			"Literatura Infantil",
			"Fantasia Épica"
	})
	void deveFuncionarAoInserirNomeDaCategoriaValido(String nomeValido) {
		CategoriaDTO categoriaDTO = new CategoriaDTO(nomeValido);
		
		Set<ConstraintViolation<CategoriaDTO>> violacoes = validator.validate(categoriaDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Ficção@Moderna",
			"Romance#2025",
			"Biografia*",
			"Literatura%Infantil",
			"Fantasia/Épica",
			"123",
			"-"
	})
	void deveRetornarErroAoInserirNomeDaCategoriaInvalido(String nomeInvalido) {
		CategoriaDTO categoriaDTO = new CategoriaDTO(nomeInvalido);
		
		Set<ConstraintViolation<CategoriaDTO>> violacoes = validator.validate(categoriaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome da categoria deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Categoria de Literatura Fantástica com Elementos de Ficção Científica. Romance Histórico. Biografia e Narrativas Épicas Misturadas em Uma Só",
			"Romance Contemporâneo com Influências da Literatura Clássica. Filosofia Moderna. Estudos Sociais e Aspectos Psicológicos Profundos",
			"História da Arte Literária Universal com Ênfase em Ficção Científica. Fantasia Épica. Romance Histórico e Biografia Detalhada",
			"Categoria de Estudos Acadêmicos sobre Literatura Comparada. Ficção Científica. Fantasia. Romance Histórico e Biografia Autoral",
			"Literatura Infantil e Juvenil com Elementos de Fantasia. Ficção Científica. Romance Histórico. Biografia e Narrativas Filosóficas"
	})
	void deveRetornarErroAoInserirNomeDoCategoriaQueUltrapassaOMaximoDeCaracteres(String nomeInvalido) {
		CategoriaDTO categoriaDTO = new CategoriaDTO(nomeInvalido);
		
		Set<ConstraintViolation<CategoriaDTO>> violacoes = validator.validate(categoriaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome da categoria deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirNomeDoCategoriaComStringVaziaOuComApenasEspacos(String nomeInvalido) {
		CategoriaDTO categoriaDTO = new CategoriaDTO(nomeInvalido);
		
		Set<ConstraintViolation<CategoriaDTO>> violacoes = validator.validate(categoriaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome da categoria é obrigatório")));
	}
}

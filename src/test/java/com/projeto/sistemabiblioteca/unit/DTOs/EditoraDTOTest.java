package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.EditoraDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EditoraDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Editora Abril",
			"Companhia das Letras",
			"Simon & Schuster",
			"Penguin Random House",
			"Editora 34"
	})
	void deveFuncionarAoInserirNomeDaEditoraValido(String nomeValido) {
		EditoraDTO editoraDTO = new EditoraDTO(nomeValido);
		
		Set<ConstraintViolation<EditoraDTO>> violacoes = validator.validate(editoraDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Editora@Brasil",
			"Companhia#Letras",
			"Penguin*House",
			"Editora%34",
			"Schuster/Simon"
	})
	void deveRetornarErroAoInserirNomeDaEditoraInvalido(String nomeInvalido) {
		EditoraDTO editoraDTO = new EditoraDTO(nomeInvalido);
		
		Set<ConstraintViolation<EditoraDTO>> violacoes = validator.validate(editoraDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome da editora deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Grande Editora Internacional de Publicações Científicas. Literárias. Acadêmicas e Técnicas com Distribuição Global e Nacional",
			"Companhia Editorial de Obras Literárias. Científicas. Técnicas e Acadêmicas com Ênfase em Pesquisa. Educação e Cultura Universal",
			"Editora Especializada em Publicações Acadêmicas. Científicas. Literárias e Técnicas com Distribuição em Diversos Países do Mundo",
			"Casa Editorial de Obras Literárias. Científicas. Técnicas e Acadêmicas com Foco em Educação. Pesquisa e Cultura Internacional",
			"Instituição Editorial de Publicações Literárias. Científicas. Técnicas e Acadêmicas com Distribuição Nacional e Internacional"
	})
	void deveRetornarErroAoInserirNomeDaEditoraQueUltrapassaOMaximoDeCaracteres(String nomeInvalido) {
		EditoraDTO editoraDTO = new EditoraDTO(nomeInvalido);
		
		Set<ConstraintViolation<EditoraDTO>> violacoes = validator.validate(editoraDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome da editora deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirNomeDaEditoraComStringVaziaOuComApenasEspacos(String nomeInvalido) {
		EditoraDTO editoraDTO = new EditoraDTO(nomeInvalido);
		
		Set<ConstraintViolation<EditoraDTO>> violacoes = validator.validate(editoraDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome da editora é obrigatório")));
	}
}

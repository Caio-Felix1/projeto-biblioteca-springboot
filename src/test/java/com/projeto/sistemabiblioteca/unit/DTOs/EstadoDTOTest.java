package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.EstadoDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EstadoDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"São Paulo",
			"Minas Gerais",
			"Rio Grande do Sul",
			"Espírito Santo",
			"Mato Grosso"
	})
	void deveFuncionarAoInserirNomeDoEstadoValido(String nomeValido) {
		EstadoDTO estadoDTO = new EstadoDTO(nomeValido, 1L);
		
		Set<ConstraintViolation<EstadoDTO>> violacoes = validator.validate(estadoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345",
			"Estado@Novo",
			"###",
			"São/Paulo",
			"---"
	})
	void deveRetornarErroAoInserirNomeDoEstadoInvalido(String nomeInvalido) {
		EstadoDTO estadoDTO = new EstadoDTO(nomeInvalido, 1L);
		
		Set<ConstraintViolation<EstadoDTO>> violacoes = validator.validate(estadoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do estado deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Estado Histórico e Cultural João da Silva Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa Machado de Assis",
			"Unidade Federativa das Flores Ornamentais e Plantas Tropicais com Extensão de Nome Muito Longo para Testar a Validação de Campos de Estado",
			"Estado dos Grandes Escritores e Poetas Brasileiros com Nome Extenso que Ultrapassa Cem Caracteres para Teste de Regex e Validação",
			"Unidade Territorial Internacional de Comércio. Cultura e Educação João Antônio de Souza Pereira Albuquerque Monteiro da Silva",
			"Estado Central de Integração Regional e Nacional com Nome Extenso que Ultrapassa Cem Caracteres para Teste de Validação de Campos"
	})
	void deveRetornarErroAoInserirNomeDoEstadoQueUltrapassaOMaximoDeCaracteres(String nomeInvalido) {
		EstadoDTO estadoDTO = new EstadoDTO(nomeInvalido, 1L);
		
		Set<ConstraintViolation<EstadoDTO>> violacoes = validator.validate(estadoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do estado deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirNomeDoEstadoComStringVaziaOuComApenasEspacos(String nomeInvalido) {
		EstadoDTO estadoDTO = new EstadoDTO(nomeInvalido, 1L);
		
		Set<ConstraintViolation<EstadoDTO>> violacoes = validator.validate(estadoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome do estado é obrigatório")));
	}
	
	@Test
	void deveRetornarErroAoInserirIdPaisNulo() {
		EstadoDTO estadoDTO = new EstadoDTO("São Paulo", null);
		
		Set<ConstraintViolation<EstadoDTO>> violacoes = validator.validate(estadoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idPais")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("País é obrigatório")));
	}
}

package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.MotivoInativacaoDoUsuarioDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MotivoInativacaoDoUsuarioDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"O usuário permaneceu inativo por mais de 12 meses sem realizar nenhum acesso ao sistema.",
			"A conta foi inativada devido a informações inconsistentes encontradas durante a revisão cadastral.",
			"Inativação realizada porque o cliente solicitou encerramento voluntário de sua conta.",
			"O usuário violou os termos de uso ao compartilhar credenciais com terceiros.",
			"A conta foi inativada após múltiplas tentativas de login sem sucesso e suspeita de fraude."
	})
	void deveFuncionarAoInserirMotivoDeInativacaoDoUsuarioValido(String motivoValido) {
		MotivoInativacaoDoUsuarioDTO motivoInativacaoDoUsuarioDTO = new MotivoInativacaoDoUsuarioDTO(motivoValido);
		
		Set<ConstraintViolation<MotivoInativacaoDoUsuarioDTO>> violacoes = validator.validate(motivoInativacaoDoUsuarioDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"###",
			"Inativação<Conta>",
			"Motivo*Invalido",
			"Conta>Bloqueada",
			"%%%Erro%%%"
	})
	void deveRetornarErroAoInserirMotivoDeInativacaoDoUsuarioInvalido(String motivoInvalido) {
		MotivoInativacaoDoUsuarioDTO motivoInativacaoDoUsuarioDTO = new MotivoInativacaoDoUsuarioDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoInativacaoDoUsuarioDTO>> violacoes = validator.validate(motivoInativacaoDoUsuarioDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de inativação do usuário deve estar num formato válido")));
	}
	
	@Test
	void deveRetornarErroAoInserirMotivoDeInativacaoDoUsuarioQueUltrapassaOMaximoDeCaracteres() {
		MotivoInativacaoDoUsuarioDTO motivoInativacaoDoUsuarioDTO = new MotivoInativacaoDoUsuarioDTO(
				"O usuário teve sua conta inativada porque não realizou nenhum acesso durante um período superior a dois anos, "
				+ "mesmo após múltiplas notificações enviadas por email e mensagens no sistema. Além disso, verificou-se que os dados cadastrados estavam desatualizados e não correspondiam às informações atuais fornecidas "
				+ "em outros registros. A decisão de inativação foi tomada para manter a base de usuários organizada, reduzir riscos de inconsistências e garantir que apenas contas ativas e verificadas permaneçam disponíveis. "
				+ "O procedimento segue a política interna de manutenção e segurança."
				);
		
		Set<ConstraintViolation<MotivoInativacaoDoUsuarioDTO>> violacoes = validator.validate(motivoInativacaoDoUsuarioDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de inativação do usuário deve ter entre 10 e 500 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Erro",
			"Inativo",
			"Cancelado"
	})
	void deveRetornarErroAoInserirMotivoDeInativacaoDoUsuarioMenorQueOValorMinimoDeCaracteres(String motivoInvalido) {
		MotivoInativacaoDoUsuarioDTO motivoInativacaoDoUsuarioDTO = new MotivoInativacaoDoUsuarioDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoInativacaoDoUsuarioDTO>> violacoes = validator.validate(motivoInativacaoDoUsuarioDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de inativação do usuário deve ter entre 10 e 500 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirMotivoDeInativacaoDoUsuarioComStringVaziaOuComApenasEspacos(String motivoInvalido) {
		MotivoInativacaoDoUsuarioDTO motivoInativacaoDoUsuarioDTO = new MotivoInativacaoDoUsuarioDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoInativacaoDoUsuarioDTO>> violacoes = validator.validate(motivoInativacaoDoUsuarioDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Motivo de inativação do usuário é obrigatório")));
	}
}

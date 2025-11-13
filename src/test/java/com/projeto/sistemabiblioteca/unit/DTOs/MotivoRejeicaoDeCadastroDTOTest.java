package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.MotivoRejeicaoDeCadastroDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MotivoRejeicaoDeCadastroDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Os dados inseridos no sistema não são adequados.",
			"O cadastro foi rejeitado devido a inserção de um endereço inválido.",
			"Documento de identificação não corresponde às informações fornecidas pelo usuário.",
			"Endereço informado está incompleto e não atende aos requisitos mínimos.",
			"Cadastro rejeitado devido à duplicidade de informações em diferentes contas."
	})
	void deveFuncionarAoInserirMotivoDeRejeicaoDeCadastroValido(String motivoValido) {
		MotivoRejeicaoDeCadastroDTO motivoRejeicaoDeCadastroDTO = new MotivoRejeicaoDeCadastroDTO(motivoValido);
		
		Set<ConstraintViolation<MotivoRejeicaoDeCadastroDTO>> violacoes = validator.validate(motivoRejeicaoDeCadastroDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"###",
			"Motivo*Invalido",
			"Rejeição<Cliente>",
			"Cadastro>Erro",
			"@@@!!!"
	})
	void deveRetornarErroAoInserirMotivoDeRejeicaoDeCadastroInvalido(String motivoInvalido) {
		MotivoRejeicaoDeCadastroDTO motivoRejeicaoDeCadastroDTO = new MotivoRejeicaoDeCadastroDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoRejeicaoDeCadastroDTO>> violacoes = validator.validate(motivoRejeicaoDeCadastroDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de rejeição de cadastro do usuário deve estar num formato válido")));
	}
	
	@Test
	void deveRetornarErroAoInserirMotivoDeRejeicaoDeCadastroQueUltrapassaOMaximoDeCaracteres() {
		MotivoRejeicaoDeCadastroDTO motivoRejeicaoDeCadastroDTO = new MotivoRejeicaoDeCadastroDTO(
				"O cadastro foi rejeitado porque o usuário forneceu informações inconsistentes em diversos campos, "
				+ "incluindo nome completo, endereço de email e telefone. Além disso, os dados ligados ao CPF não correspondem aos dados digitados, gerando dúvidas quanto à veracidade "
				+ "dos dados. O sistema exige que todos os campos sejam preenchidos corretamente e que os documentos enviados estejam em conformidade com as informações fornecidas. Como isso não ocorreu, "
				+ "o cadastro não pôde ser aprovado e foi rejeitado para garantir a segurança e a integridade da base de dados."
				);
		
		Set<ConstraintViolation<MotivoRejeicaoDeCadastroDTO>> violacoes = validator.validate(motivoRejeicaoDeCadastroDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de rejeição de cadastro do usuário deve ter entre 10 e 500 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Erro",
			"Duplicado",
			"Invalido"
	})
	void deveRetornarErroAoInserirMotivoDeRejeicaoDeCadastroMenorQueOValorMinimoDeCaracteres(String motivoInvalido) {
		MotivoRejeicaoDeCadastroDTO motivoRejeicaoDeCadastroDTO = new MotivoRejeicaoDeCadastroDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoRejeicaoDeCadastroDTO>> violacoes = validator.validate(motivoRejeicaoDeCadastroDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de rejeição de cadastro do usuário deve ter entre 10 e 500 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirMotivoDeRejeicaoDeCadastroComStringVaziaOuComApenasEspacos(String motivoInvalido) {
		MotivoRejeicaoDeCadastroDTO motivoRejeicaoDeCadastroDTO = new MotivoRejeicaoDeCadastroDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoRejeicaoDeCadastroDTO>> violacoes = validator.validate(motivoRejeicaoDeCadastroDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Motivo de rejeição de cadastro do usuário é obrigatório")));
	}
}

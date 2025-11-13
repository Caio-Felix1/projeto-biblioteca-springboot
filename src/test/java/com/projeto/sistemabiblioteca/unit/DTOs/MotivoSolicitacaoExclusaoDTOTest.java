package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.MotivoSolicitacaoExclusaoDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MotivoSolicitacaoExclusaoDTOTest {
	
	private Validator validator;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Cliente solicitou exclusão por ter que se mudar de cidade.",
			"Livro em estado físico ruim, com páginas rasgadas e capa danificada.",
			"Usuário João da Silva pediu exclusão por duplicidade de conta registrada.",
			"Cliente solicitou a exclusão de sua conta.",
			"Exclusão solicitada porque o exemplar foi devolvido com um estado físico ruim."
	})
	void deveFuncionarAoInserirMotivoDeSolicitacaoParaExclusaoValido(String motivoValido) {
		MotivoSolicitacaoExclusaoDTO motivoSolicitacaoExclusaoDTO = new MotivoSolicitacaoExclusaoDTO(motivoValido);
		
		Set<ConstraintViolation<MotivoSolicitacaoExclusaoDTO>> violacoes = validator.validate(motivoSolicitacaoExclusaoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"###",
			"Motivo*Invalido",
			"Excluir<Cliente>",
			"Livro>Quebrado",
			"@@@!!!"
	})
	void deveRetornarErroAoInserirMotivoDeSolicitacaoParaExclusaoInvalido(String motivoInvalido) {
		MotivoSolicitacaoExclusaoDTO motivoSolicitacaoExclusaoDTO = new MotivoSolicitacaoExclusaoDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoSolicitacaoExclusaoDTO>> violacoes = validator.validate(motivoSolicitacaoExclusaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de solicitação de exclusão deve estar num formato válido")));
	}
	
	@Test
	void deveRetornarErroAoInserirMotivoDeSolicitacaoParaExclusaoQueUltrapassaOMaximoDeCaracteres() {
		MotivoSolicitacaoExclusaoDTO motivoSolicitacaoExclusaoDTO = new MotivoSolicitacaoExclusaoDTO("O cliente solicitou a exclusão da conta porque percebeu que havia cadastrado informações incorretas em diversos campos, "
				+ "incluindo o endereço de email, o telefone e o nome completo. Além disso, relatou que não conseguiu atualizar os dados por "
				+ "meio das opções disponíveis no sistema e preferiu encerrar o cadastro para criar um novo com informações corretas. O motivo "
				+ "detalhado inclui também a preocupação com a segurança dos dados e a necessidade de manter a consistência das informações pessoais "
				+ "e profissionais.");
		
		Set<ConstraintViolation<MotivoSolicitacaoExclusaoDTO>> violacoes = validator.validate(motivoSolicitacaoExclusaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de solicitação de exclusão deve ter entre 10 e 500 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Erro",
			"Duplicado",
			"Apagar"
	})
	void deveRetornarErroAoInserirMotivoDeSolicitacaoParaExclusaoMenorQueOValorMinimoDeCaracteres(String motivoInvalido) {
		MotivoSolicitacaoExclusaoDTO motivoSolicitacaoExclusaoDTO = new MotivoSolicitacaoExclusaoDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoSolicitacaoExclusaoDTO>> violacoes = validator.validate(motivoSolicitacaoExclusaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O motivo de solicitação de exclusão deve ter entre 10 e 500 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirMotivoDeSolicitacaoParaExclusaoComStringVaziaOuComApenasEspacos(String motivoInvalido) {
		MotivoSolicitacaoExclusaoDTO motivoSolicitacaoExclusaoDTO = new MotivoSolicitacaoExclusaoDTO(motivoInvalido);
		
		Set<ConstraintViolation<MotivoSolicitacaoExclusaoDTO>> violacoes = validator.validate(motivoSolicitacaoExclusaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("motivo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Motivo de solicitação de exclusão é obrigatório")));
	}
}

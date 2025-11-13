package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.AutenticacaoDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AutenticacaoDTOTest {
	
	private Validator validator;
	
	private final String emailPadrao = "usuario@gmail.com";
	private final String senhaPadrao = "12345678";
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"usuario1@empresa.com",
			"a@b.com", 
			"teste123@dominio.com.br", 
			"mail9@site.com", 
			"abc123@exemplo.com.br",
			"user@sub.site.com",
			"teste123@empresa.com.br",
			"john.doe@meusite.com",
			"ANA_SILVA@biblioteca.com.br",
			"dev+tag@provedor.com"
	})
	void deveFuncionarAoInserirEmailValido(String emailValido) {
		AutenticacaoDTO autenticacaoDTO = new AutenticacaoDTO(emailValido, senhaPadrao);
		
		Set<ConstraintViolation<AutenticacaoDTO>> violacoes = validator.validate(autenticacaoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"usuario@gmail.org", 
			"teste@empresa.net", 
			"john@dominio.com.us", 
			"ana@biblioteca.br", 
			"dev@provedor.cjfsakjfiqjasfjka", 
			"user@site.edu", 
			"contato@governo.gov.br", 
			"teste@empresa.io", 
			"pessoa@dominio.co.uk", 
			"aluno@universidade.edu.br"
	})
	void deveRetornarErroAoInserirEmailInvalido(String emailInvalido) {
		AutenticacaoDTO autenticacaoDTO = new AutenticacaoDTO(emailInvalido, senhaPadrao);
		
		Set<ConstraintViolation<AutenticacaoDTO>> violacoes = validator.validate(autenticacaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Email deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             "
	})
	void deveRetornarErroAoInserirEmailComStringVaziaOuComApenasEspacos(String emailInvalido) {
		AutenticacaoDTO autenticacaoDTO = new AutenticacaoDTO(emailInvalido, senhaPadrao);
		
		Set<ConstraintViolation<AutenticacaoDTO>> violacoes = validator.validate(autenticacaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Email é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"usuario.nome.muito.grande.completo.da.silva.souza.pereira.albuquerque@examplelongdomaincompanycorporation.com",
			"joao.pedro.maria.ana.clara.luiza.fernanda.carlos.roberto.jose.alexandre.paulo.sergio@universidadeinternacionaldemedicina.com",
			"contato.departamento.financeiro.setor.administrativo.gerencia.empresa.multinacional.tecnologia.avancada@corporatebusinesssolutions.com",
			"cliente.vip.exclusivo.programa.fidelidade.superpremium.gold.platinum.diamond@shoppingcenterluxoemsaopaulobrasil.com",
			"professor.orientador.pesquisa.academica.cientifica.inovacao.tecnologica.laboratorio.experimentos@institutouniversitarioeducacionalglobal.com.br"
	})
	void deveRetornarErroAoInserirEmailQueUltrapassaOMaximoDeCaracteres(String emailInvalido) {
		AutenticacaoDTO autenticacaoDTO = new AutenticacaoDTO(emailInvalido, senhaPadrao);
		
		Set<ConstraintViolation<AutenticacaoDTO>> violacoes = validator.validate(autenticacaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O email deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"SenhaSegura1!",
			"Biblioteca2025#",
			"EstudoDeRegexAvancado@123",
			"UsuarioNomeRealComSenhaComplexa$456",
			"EstaSenhaTemMaisDeOitentaCaracteresMasAindaMenosDeCemParaTestarValidacaoDeComprimento1234567890"
	})
	void deveFuncionarAoInserirSenhaValida(String senhaValida) {
		AutenticacaoDTO autenticacaoDTO = new AutenticacaoDTO(emailPadrao, senhaValida);
		
		Set<ConstraintViolation<AutenticacaoDTO>> violacoes = validator.validate(autenticacaoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"abc123",
			"senha1",
			"1234567",
			"EstaSenhaEhExageradamenteLongaComMaisDeCemCaracteresParaTestarLimiteDeValidacaoDoSistemaDeSegurancaDaBibliotecaComRegex1234567890MaisTextoExtraParaUltrapassar",
			"SenhaMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoMuitoLonga123"
	})
	void deveRetornarErroAoInserirSenhaQueNaoEstaNoIntervaloDeQuantidadeDeCaracteresValidos(String senhaInvalida) {
		AutenticacaoDTO autenticacaoDTO = new AutenticacaoDTO(emailPadrao, senhaInvalida);
		
		Set<ConstraintViolation<AutenticacaoDTO>> violacoes = validator.validate(autenticacaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("senha")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A senha deve ter entre 8 e 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             "
	})
	void deveRetornarErroAoInserirSenhaComStringVaziaOuComApenasEspacos(String senhaInvalida) {
		AutenticacaoDTO autenticacaoDTO = new AutenticacaoDTO(emailPadrao, senhaInvalida);
		
		Set<ConstraintViolation<AutenticacaoDTO>> violacoes = validator.validate(autenticacaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("senha")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Senha é obrigatória")));
	}
}

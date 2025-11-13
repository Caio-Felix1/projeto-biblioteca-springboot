package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.EnderecoDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EnderecoDTOTest {
	
	private Validator validator;
	
	private final String nomeLogradouroPadrao = "rua teste";
	private final String numero = "123";
	private final String bairro = "bairro teste";
	private final String cep = "11111111";
	private final String cidade = "cidade teste";
	private final Long idEstado = 1L;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Rua São João",
			"Avenida Paulista",
			"Travessa das Flores",
			"Alameda Santos",
			"Rodovia BR-101"
	})
	void deveFuncionarAoInserirNomeLogradouroValido(String nomeLogradouroValido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroValido, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Rua@Paulista",
			"Avenida#Central",
			"Travessa*Flores",
			"Alameda%Santos",
			"Rodovia/101"
	})
	void deveRetornarErroAoInserirNomeLogradouroInvalido(String nomeLogradouroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroInvalido, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nomeLogradouro")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do logradouro deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Avenida Principal de Desenvolvimento Urbano Sustentável João da Silva Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira",
			"Rua das Flores e Plantas Ornamentais com Extensão de Mais de Cem Caracteres para Testar a Validação de Campos de Logradouro",
			"Travessa Histórica dos Grandes Escritores e Poetas Brasileiros com Nome Extenso para Testar Limite de Validação de Regex",
			"Alameda Internacional de Comércio. Cultura e Educação João Antônio de Souza Pereira Albuquerque Monteiro Fernandes da Silva",
			"Rodovia Estadual de Integração Regional e Nacional com Nome Extenso que Ultrapassa Cem Caracteres para Teste de Validação"
	})
	void deveRetornarErroAoInserirNomeLogradouroQueUltrapassaOMaximoDeCaracteres(String nomeLogradouroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroInvalido, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nomeLogradouro")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do logradouro deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             "
	})
	void deveRetornarErroAoInserirNomeLogradouroComStringVaziaOuComApenasEspacos(String nomeLogradouroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroInvalido, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nomeLogradouro")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome do logradouro é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"123",
			"45A",
			"789-1",
			"56B-22",
			"1000C",
			"S/N",
			"s/n"
	})
	void deveFuncionarAoInserirNumeroValido(String numeroValido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numeroValido, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12@",
			"AB123",
			"45-",
			"78--90",
			"#100"
	})
	void deveRetornarErroAoInserirNumeroInvalido(String numeroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numeroInvalido, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("numero")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O número do endereço deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345678901",
			"9876543210A",
			"1234567890-12",
			"1111111111B-2222",
			"9999999999C"
	})
	void deveRetornarErroAoInserirNumeroQueUltrapassaOMaximoDeCaracteres(String numeroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numeroInvalido, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("numero")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O número do endereço deve ter no máximo 10 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             "
	})
	void deveRetornarErroAoInserirNumeroComStringVaziaOuComApenasEspacos(String numeroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numeroInvalido, 
				null, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("numero")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Número do endereço é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Apto 12",
			"Bloco B",
			"Torre 3",
			"Fundos",
			"Casa 45-B"
	})
	void deveFuncionarAoInserirComplementoValido(String complementoValido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				complementoValido, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"@@Apto",
			"Bloco#1",
			"***",
			"Casa/45",
			"---",
			"",
			"    "
	})
	void deveRetornarErroAoInserirComplementoInvalido(String complementoInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				complementoInvalido, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("complemento")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O complemento deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Apartamento localizado no terceiro andar da torre principal do condomínio residencial",
			"Bloco B. setor 4. conjunto habitacional João da Silva Pereira Albuquerque Fernandes",
			"Casa 12-B. fundos. próximo à entrada lateral do condomínio fechado com portaria 24h",
			"Torre 7. apartamento 123. ala norte. edifício residencial com estacionamento subterrâneo",
			"Complemento: sala comercial número 45. andar 10. edifício empresarial Avenida Paulista"
	})
	void deveRetornarErroAoInserirComplementoQueUltrapassaOMaximoDeCaracteres(String complementoInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				complementoInvalido, 
				bairro, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("complemento")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Complemento deve ter no máximo 50 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Vila Mariana",
			"São João",
			"Jardim Paulista",
			"Santa Cecília",
			"Morumbi"
	})
	void deveFuncionarAoInserirBairroValido(String bairroValido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairroValido, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345",
			"Bairro@Centro",
			"###",
			"Morumbi/Paulista",
			"---"
	})
	void deveRetornarErroAoInserirBairroInvalido(String bairroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairroInvalido, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("bairro")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do bairro deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Bairro Residencial e Comercial João da Silva Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa Machado de Assis",
			"Jardim das Flores Ornamentais e Plantas Tropicais com Extensão de Nome Muito Longo para Testar a Validação de Campos de Bairro",
			"Vila Histórica dos Grandes Escritores e Poetas Brasileiros com Nome Extenso que Ultrapassa Cem Caracteres para Teste de Regex",
			"Conjunto Habitacional Internacional de Comércio. Cultura e Educação João Antônio de Souza Pereira Albuquerque Monteiro da Silva",
			"Bairro Central de Integração Regional e Nacional com Nome Extenso que Ultrapassa Cem Caracteres para Teste de Validação"
	})
	void deveRetornarErroAoInserirBairroQueUltrapassaOMaximoDeCaracteres(String bairroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairroInvalido, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("bairro")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do bairro deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             "
	})
	void deveRetornarErroAoInserirBairroComStringVaziaOuComApenasEspacos(String bairroInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairroInvalido, 
				cep, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("bairro")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Bairro é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"01001000",
			"20040030",
			"30140071",
			"40028922",
			"69920900"
	})
	void deveFuncionarAoInserirCepValido(String cepValido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cepValido, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"1234567",      // apenas 7 dígitos
			"123456789",    // 9 dígitos
			"12A45678",     // contém letra
			"1234-567",     // contém hífen
			"ABCDE123"      // contém letras

	})
	void deveRetornarErroAoInserirCepInvalido(String cepInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cepInvalido, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cep")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O CEP deve ter 8 dígitos")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             "
	})
	void deveRetornarErroAoInserirCepComStringVaziaOuComApenasEspacos(String cepInvalido) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cepInvalido, 
				cidade, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cep")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("CEP é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"São Paulo",
			"Rio de Janeiro",
			"Belo Horizonte",
			"Porto Alegre",
			"Vitória"
	})
	void deveFuncionarAoInserirCidadeValida(String cidadeValida) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidadeValida, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345",
			"Cidade@Nova",
			"###",
			"São/Paulo",
			"---"
	})
	void deveRetornarErroAoInserirCidadeInvalida(String cidadeInvalida) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidadeInvalida, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cidade")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome da cidade deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Cidade Histórica e Cultural João da Silva Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa Machado de Assis",
			"Município das Flores Ornamentais e Plantas Tropicais com Extensão de Nome Muito Longo para Testar a Validação de Campos de Cidade",
			"Vila Histórica dos Grandes Escritores e Poetas Brasileiros com Nome Extenso que Ultrapassa Cem Caracteres para Teste de Regex",
			"Conjunto Urbano Internacional de Comércio. Cultura e Educação João Antônio de Souza Pereira Albuquerque Monteiro da Silva",
			"Cidade Central de Integração Regional e Nacional com Nome Extenso que Ultrapassa Cem Caracteres para Teste de Validação"
	})
	void deveRetornarErroAoInserirCidadeQueUltrapassaOMaximoDeCaracteres(String cidadeInvalida) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidadeInvalida, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cidade")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome da cidade deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             "
	})
	void deveRetornarErroAoInserirCidadeComStringVaziaOuComApenasEspacos(String cidadeInvalida) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidadeInvalida, 
				idEstado);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cidade")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Cidade é obrigatória")));
	}
	
	@Test
	void deveRetornarErroAoInserirIdEstadoNulo() {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
				nomeLogradouroPadrao, 
				numero, 
				null, 
				bairro, 
				cep, 
				cidade, 
				null);
		
		Set<ConstraintViolation<EnderecoDTO>> violacoes = validator.validate(enderecoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idEstado")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Estado é obrigatório")));
	}
}

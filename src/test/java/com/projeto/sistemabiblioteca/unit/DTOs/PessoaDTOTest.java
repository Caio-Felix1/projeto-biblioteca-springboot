package com.projeto.sistemabiblioteca.unit.DTOs;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.EnderecoDTO;
import com.projeto.sistemabiblioteca.DTOs.PessoaDTO;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PessoaDTOTest {
	
	private Validator validator;
	
	private final String nome = "João";
	private final String cpf = "11111111111";
	private final Sexo sexo = Sexo.MASCULINO;
	private final FuncaoUsuario funcao = FuncaoUsuario.ADMINISTRADOR;
	private final LocalDate dtNascimento = LocalDate.of(2000, 1, 1);
	private final String telefone = "1111111111";
	private final String email = "usuario@gmail.com";
	private final String senha = "12345678";
	private final EnderecoDTO enderecoDTO = new EnderecoDTO("rua teste", "123", null, "bairro teste", "11111111", "cidade teste", 1L);
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"João da Silva",
			"Maria de Souza",
			"José Antônio",
			"Ana-Clara Pereira",
			"Luís O'Neill"
	})
	void deveFuncionarAoInserirNomeDaPessoaValido(String nomeValido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nomeValido, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345",
			"João@Silva",
			"###",
			"Ana/Clara",
			"---"
	})
	void deveRetornarErroAoInserirNomeDaPessoaInvalido(String nomeInvalido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nomeInvalido, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do usuário deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"João da Silva Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa Machado de Assis Filho Neto Júnior",
			"Maria Clara de Andrade Rodrigues da Silva Souza Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa e Lima",
			"José Antônio de Almeida Barros da Silva Souza Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa e Ramos",
			"Ana Beatriz de Oliveira Costa Souza Pereira Albuquerque Monteiro Fernandes dos Santos Rodrigues da Silva Machado de Assis",
			"Fernando Henrique de Souza Pereira Albuquerque Monteiro Fernandes dos Santos Oliveira Costa e Silva Rodrigues de Lima"
	})
	void deveRetornarErroAoInserirNomeDaPessoaQueUltrapassaOMaximoDeCaracteres(String nomeInvalido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nomeInvalido, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome do usuário deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirNomeDaPessoaComStringVaziaOuComApenasEspacos(String nomeInvalido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nomeInvalido, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Nome do usuário é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"12345678901", 
			"00000000000", 
			"98765432100", 
			"11122233344", 
			"36925814703"
	})
	void deveFuncionarAoInserirCpfValido(String cpfValido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpfValido, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"123.456.789-09", 
			"1234567890", 
			"123456789012", 
			"abcdefghijk", 
			"12345 678901"
	})
	void deveRetornarErroAoInserirCpfInvalido(String cpfInvalido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpfInvalido, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("CPF deve ter 11 dígitos")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirCpfComStringVaziaOuComApenasEspacos(String cpfInvalido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpfInvalido, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("CPF é obrigatório")));
	}
	
	@Test
	void deveRetornarErroAoInserirSexoNulo() {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				null, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("sexo")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Sexo é obrigatório")));
	}
	
	@Test
	void deveRetornarErroAoInserirFuncaoNula() {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				null, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("funcao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Função do usuário é obrigatória")));
	}
	
	@Test
	void deveFuncionarAoInserirDataDeNascimentoValida() {
		PessoaDTO pessoaDTO1 = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				LocalDate.now().minusDays(1), 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		PessoaDTO pessoaDTO2 = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				LocalDate.now().minusYears(1), 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes1 = validator.validate(pessoaDTO1);
		Set<ConstraintViolation<PessoaDTO>> violacoes2 = validator.validate(pessoaDTO2);
		
		Assertions.assertTrue(violacoes1.isEmpty());
		
		Assertions.assertTrue(violacoes2.isEmpty());
	}
	
	@Test
	void deveFuncionarAoInserirDataDeNascimentoInvalida() {
		PessoaDTO pessoaDTO1 = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				LocalDate.now(), 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		PessoaDTO pessoaDTO2 = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				LocalDate.now().plusDays(1), 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes1 = validator.validate(pessoaDTO1);
		Set<ConstraintViolation<PessoaDTO>> violacoes2 = validator.validate(pessoaDTO2);
		
		Assertions.assertFalse(violacoes1.isEmpty());
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtNascimento")));
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getMessage().equals("Data de nascimento deve ser no passado")));
		
		Assertions.assertFalse(violacoes2.isEmpty());
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtNascimento")));
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getMessage().equals("Data de nascimento deve ser no passado")));
	}
	
	@Test
	void deveRetornarErroAoInserirDataDeNascimentoNula() {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				null, 
				telefone, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtNascimento")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Data de nascimento é obrigatória")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"11987654321",
			"21912345678",
			"3134567890",
			"41998765432",
			"51987654321"
	})
	void deveFuncionarAoInserirTelefoneValido(String telefoneValido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefoneValido, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"987654321",     // apenas 9 dígitos
			"123456789012",  // 12 dígitos
			"(11)987654321", // contém parênteses
			"11-987654321",  // contém hífen
			"telefone1234"   // contém letras
	})
	void deveRetornarErroAoInserirTelefoneInvalido(String telefoneInvalido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefoneInvalido, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telefone")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Telefone deve ter 10 ou 11 dígitos")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirTelefoneComStringVaziaOuComApenasEspacos(String telefoneInvalido) {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefoneInvalido, 
				email, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telefone")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Telefone é obrigatório")));
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
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				emailValido, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
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
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				emailInvalido, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
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
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				emailInvalido, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
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
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				emailInvalido, 
				senha, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
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
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senhaValida, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
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
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senhaInvalida, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
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
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senhaInvalida, 
				enderecoDTO);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("senha")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Senha é obrigatória")));
	}
	
	@Test
	void deveRetornarErroAoInserirEnderecoNulo() {
		PessoaDTO pessoaDTO = new PessoaDTO(
				nome, 
				cpf, 
				sexo, 
				funcao, 
				dtNascimento, 
				telefone, 
				email, 
				senha, 
				null);
		
		Set<ConstraintViolation<PessoaDTO>> violacoes = validator.validate(pessoaDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("endereco")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Endereço é obrigatório")));
	}
}

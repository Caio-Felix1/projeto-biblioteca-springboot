package com.projeto.sistemabiblioteca.unit.DTOs;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.EdicaoDTO;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EdicaoDTOTest {
	
	private Validator validator;
	
	private final String descricaoPadrao = "Edição de Colecionador";
	private final TipoCapa tipoCapaPadrao = TipoCapa.DURA;
	private final Integer qtdPaginasPadrao = 100;
	private final TamanhoEdicao tamanhoEdicaoPadrao = TamanhoEdicao.PEQUENO;
	private final ClassificacaoIndicativa classificacaoPadrao = ClassificacaoIndicativa.L;
	private final LocalDate dtPublicacaoPadrao = LocalDate.of(2000, 1, 1);
	private final Long tituloId = 1L;
	private final Long editoraId = 1L;
	private final Long idiomaId = 1L;
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"1ª edição",
			"2º volume revisado",
			"Edição especial limitada",
			"Volume 3 - Coleção Clássicos",
			"Edição comemorativa 50 anos"
	})
	void deveFuncionarAoInserirDescricaoDeEdicaoValida(String descricaoValida) {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoValida, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Edição*Especial",
			"Volume@2025",
			"#PrimeiraEdição",
			"Edição%Comemorativa",
			"Vol/2"
	})
	void deveRetornarErroAoInserirDescricaoDeEdicaoInvalida(String descricaoInvalida) {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoInvalida, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricaoEdicao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A descrição da edição deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Edição comemorativa especial de colecionador com capa dura e conteúdo adicional exclusivo",
			"Volume 2 revisado e ampliado com notas explicativas, comentários críticos e apêndices detalhados",
			"Edição limitada numerada com prefácio inédito, ilustrações raras e material suplementar exclusivo",
			"Edição especial de aniversário com introdução histórica, glossário completo e índice temático extenso",
			"Volume 5 da coleção clássicos universais revisado com notas de rodapé e referências bibliográficas"
	})
	void deveRetornarErroAoInserirDescricaoDeEdicaoQueUltrapassaOMaximoDeCaracteres(String descricaoInvalida) {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoInvalida, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricaoEdicao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A descrição da edição deve ter no máximo 60 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirDescricaoDeEdicaoComStringVaziaOuComApenasEspacos(String descricaoInvalida) {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoInvalida, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricaoEdicao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Descrição da edição é obrigatória")));
	}
	
	@Test
	void deveRetornarErroAoInserirTipoDeCapaNula() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				null, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tipoCapa")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Tipo de capa é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			1,
			1000,
			5000
	})
	void deveFuncionarAoInserirQuantidadeDePaginasValida(Integer qtdPaginasValida) {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasValida, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			-1,
			0,
	})
	void deveRetornarErroAoInserirQuantidadeDePaginasMenorQueOValorMinimo(Integer qtdPaginasInvalida) {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasInvalida, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("qtdPaginas")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Deve ter no mínimo 1 página")));
	}
	
	@ParameterizedTest
	@ValueSource(ints = {
			5001,
			10000
	})
	void deveRetornarErroAoInserirQuantidadeDePaginasMaiorQueOValorMaximo(Integer qtdPaginasInvalida) {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasInvalida, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("qtdPaginas")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Deve ter no máximo 5 mil páginas")));
	}
	
	@Test
	void deveRetornarErroAoInserirQuantidadeDePaginasNula() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				null, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("qtdPaginas")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Quantidade de páginas é obrigatória")));
	}
	
	@Test
	void deveRetornarErroAoInserirTamanhoDeEdicaoNula() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				null, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tamanho")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Tamanho da edição é obrigatório")));
	}
	
	@Test
	void deveRetornarErroAoInserirClassificacaoIndicativaNula() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				null, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("classificacao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Classificação da edição é obrigatória")));
	}
	
	@Test
	void deveFuncionarAoInserirDataDePublicacaoValida() {
		EdicaoDTO edicaoDTO1 = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				LocalDate.now(), 
				tituloId, 
				editoraId, 
				idiomaId);
		
		EdicaoDTO edicaoDTO2 = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				LocalDate.now().minusDays(1), 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes1 = validator.validate(edicaoDTO1);
		Set<ConstraintViolation<EdicaoDTO>> violacoes2 = validator.validate(edicaoDTO2);
		
		Assertions.assertTrue(violacoes1.isEmpty());
		
		Assertions.assertTrue(violacoes2.isEmpty());
	}
	
	@Test
	void deveRetornarErroAoInserirDataDePublicacaoInvalida() {
		EdicaoDTO edicaoDTO1 = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				LocalDate.now().plusDays(1), 
				tituloId, 
				editoraId, 
				idiomaId);
		
		EdicaoDTO edicaoDTO2 = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				LocalDate.now().plusYears(1), 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes1 = validator.validate(edicaoDTO1);
		Set<ConstraintViolation<EdicaoDTO>> violacoes2 = validator.validate(edicaoDTO2);
		
		Assertions.assertFalse(violacoes1.isEmpty());
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtPublicacao")));
		Assertions.assertTrue(violacoes1.stream().anyMatch(v -> v.getMessage().equals("Data de publicação não pode ser no futuro")));
		
		Assertions.assertFalse(violacoes2.isEmpty());
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtPublicacao")));
		Assertions.assertTrue(violacoes2.stream().anyMatch(v -> v.getMessage().equals("Data de publicação não pode ser no futuro")));
	}
	
	@Test
	void deveRetornarErroAoInserirDataDePublicacaoNula() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				null, 
				tituloId, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dtPublicacao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Data de publicação é obrigatória")));
	}
	
	@Test
	void deveRetornarErroAoInserirTituloIdNulo() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				null, 
				editoraId, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tituloId")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Título é obrigatório")));
	}
	
	@Test
	void deveRetornarErroAoInserirEditoraIdNulo() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				null, 
				idiomaId);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("editoraId")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Editora é obrigatória")));
	}
	
	@Test
	void deveRetornarErroAoInserirIdiomaIdNulo() {
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				descricaoPadrao, 
				tipoCapaPadrao, 
				qtdPaginasPadrao, 
				tamanhoEdicaoPadrao, 
				classificacaoPadrao, 
				dtPublicacaoPadrao, 
				tituloId, 
				editoraId, 
				null);
		
		Set<ConstraintViolation<EdicaoDTO>> violacoes = validator.validate(edicaoDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idiomaId")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Idioma é obrigatório")));
	}
}

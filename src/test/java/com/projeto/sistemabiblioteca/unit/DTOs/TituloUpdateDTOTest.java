package com.projeto.sistemabiblioteca.unit.DTOs;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projeto.sistemabiblioteca.DTOs.TituloUpdateDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class TituloUpdateDTOTest {
	
	private Validator validator;
	
	private final String titulo = "Título";
	private final String descricao = "Descrição";
	
	@BeforeEach
	void configurar() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"O Senhor dos Anéis",
			"1984",
			"Dom Casmurro",
			"A Guerra dos Mundos!",
			"Amor & Ódio: Histórias",
			"Programação com C# e C++"
	})
	void deveFuncionarAoInserirTituloValido(String tituloValido) {
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				tituloValido, 
				descricao
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"###",
			"Livro_Novo",
			"História^Fantástica",
			"---",
			"Contos<>Modernos"
	})
	void deveRetornarErroAoInserirTituloInvalido(String tituloInvalido) {
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				tituloInvalido, 
				descricao
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O título deve estar num formato válido")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"A História Completa da Civilização Ocidental: Dos Primeiros Povos da Antiguidade às Grandes Revoluções Modernas e Contemporâneas",
			"Romance Épico de Aventuras Fantásticas e Históricas com Personagens Complexos, Intrigas Políticas e Mistérios Filosóficos Profundos",
			"Crônicas Literárias e Científicas sobre a Evolução da Humanidade, Cultura, Tecnologia e Sociedade ao Longo dos Séculos e Milênios",
			"Memórias e Reflexões Filosóficas de um Autor sobre a Vida, o Amor, a Morte, a Arte, a Política e os Grandes Mistérios da Existência",
			"Enciclopédia Completa de Literatura, Filosofia, História, Ciência, Arte e Cultura com Narrativas Detalhadas e Explicações Profundas"
	})
	void deveRetornarErroAoInserirTituloQueUltrapassaOMaximoDeCaracteres(String tituloInvalido) {
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				tituloInvalido, 
				descricao
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O título deve ter no máximo 100 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirTituloComStringVaziaOuComApenasEspacos(String tituloInvalido) {
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				tituloInvalido, 
				descricao
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Título é obrigatório")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"Um jovem descobre poderes ocultos e precisa enfrentar seus medos para salvar o reino.",
			"História emocionante de amizade, coragem e superação em tempos de guerra.",
			"Romance envolvente que mistura paixão, segredos e escolhas difíceis.",
			"Aventura épica com batalhas, criaturas fantásticas e heróis improváveis.",
			"Biografia inspiradora de uma mulher que transformou sua vida e impactou gerações."
	})
	void deveFuncionarAoInserirDescricaoValida(String descricaoValida) {
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				titulo, 
				descricaoValida
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertTrue(violacoes.isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"###",
			"Sinopse_Livro",
			"História^Fantástica",
			"---",
			"Descrição<>Completa"
	})
	void deveRetornarErroAoInserirDescricaoInvalida(String descricaoInvalida) {
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				titulo, 
				descricaoInvalida
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A descrição do título deve estar num formato válido")));
	}
	
	@Test
	void deveRetornarErroAoInserirDescricaoQueUltrapassaOMaximoDeCaracteres() {
		String descricaoInvalida = 	"Esta obra apresenta a jornada de um protagonista que atravessa diferentes fases da vida, enfrentando desafios pessoais, sociais e espirituais. "
				+ "Ao longo de mais de mil páginas, o leitor é convidado a refletir sobre os dilemas da existência humana, a busca por sentido e a necessidade de "
				+ "conexão com os outros. A narrativa se desenrola em cenários variados, desde pequenas vilas até grandes metrópoles, explorando culturas, tradições e "
				+ "conflitos. Cada capítulo traz novos personagens, histórias paralelas e acontecimentos que se entrelaçam em uma trama complexa e envolvente. O autor "
				+ "utiliza recursos literários como metáforas, simbolismos e diálogos intensos para construir uma experiência rica e multifacetada. A sinopse, por si só, "
				+ "já ultrapassa mil caracteres, pois descreve em detalhes os principais elementos da obra, incluindo temas como amor, perda, esperança, redenção e transformação "
				+ "pessoal. É uma descrição extensa que demonstra a profundidade e a densidade do conteúdo apresentado. É uma história fascinante.";
		
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				titulo, 
				descricaoInvalida
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A descrição do título deve ter no máximo 1000 caracteres")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"             ",
	})
	void deveRetornarErroAoInserirDescricaoComStringVaziaOuComApenasEspacos(String descricaoInvalida) {
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				titulo, 
				descricaoInvalida
				);
		
		Set<ConstraintViolation<TituloUpdateDTO>> violacoes = validator.validate(tituloUpdateDTO);
		
		Assertions.assertFalse(violacoes.isEmpty());
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descricao")));
		Assertions.assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Descrição do título é obrigatória")));
	}
}

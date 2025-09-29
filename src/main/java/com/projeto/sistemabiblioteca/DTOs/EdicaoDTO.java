package com.projeto.sistemabiblioteca.DTOs;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;

public record EdicaoDTO(
		Long idEdicao,
		TipoCapa tipoCapa,
		int qtdPaginas,
		TamanhoEdicao tamanhoEdicao,
		ClassificacaoIndicativa classificacao,
		LocalDate dtPublicacao,
		IdiomaDTO idioma,
		TituloDTO titulo,
		EditoraDTO editora
		) {}

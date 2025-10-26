package com.projeto.sistemabiblioteca.DTOs;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record TituloDTO(
		@NotBlank(message = "Título é obrigatório")
		String nome,
		
		@NotBlank(message = "Descrição do título é obrigatória")
		String descricao,
		
		@NotEmpty(message = "É necessário inserir pelo menos uma categoria")
		Set<Long> idsCategorias,
		
		@NotEmpty(message = "É necessário inserir pelo menos um autor")
		Set<Long> idsAutores) {}

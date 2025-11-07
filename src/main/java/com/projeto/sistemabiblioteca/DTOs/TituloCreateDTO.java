package com.projeto.sistemabiblioteca.DTOs;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TituloCreateDTO(
		@NotBlank(message = "Título é obrigatório")
		@Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
		String nome,
		
		@NotBlank(message = "Descrição do título é obrigatória")
		@Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
		String descricao,
		
		@NotEmpty(message = "É necessário inserir pelo menos uma categoria")
		Set<Long> idsCategorias,
		
		@NotEmpty(message = "É necessário inserir pelo menos um autor")
		Set<Long> idsAutores) {}

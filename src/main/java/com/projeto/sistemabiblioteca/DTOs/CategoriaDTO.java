package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
		@NotBlank(message = "Nome da categoria é obrigatório")
		String nome) {}

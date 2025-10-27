package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;

public record EditoraDTO(
		@NotBlank(message = "Nome da editora é obrigatório")
		String nome) {}

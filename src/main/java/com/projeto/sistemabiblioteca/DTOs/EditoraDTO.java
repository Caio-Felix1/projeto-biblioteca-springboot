package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EditoraDTO(
		@NotBlank(message = "Nome da editora é obrigatório")
		@Size(max = 100, message = "Nome da editora deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ])[A-Za-zÀ-ÖØ-öø-ÿ0-9&'\"\\-\\.\\s]+$", message = "O nome da editora deve estar num formato válido")
		String nome) {}

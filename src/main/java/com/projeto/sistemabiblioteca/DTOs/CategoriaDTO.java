package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
		@NotBlank(message = "Nome da categoria é obrigatório")
		@Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ])[A-Za-zÀ-ÖØ-öø-ÿ0-9.'\\-\\s]+$", message = "O nome da categoria deve estar num formato válido")
		String nome) {}

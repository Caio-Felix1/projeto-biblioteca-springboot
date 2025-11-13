package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PaisDTO(
		@NotBlank(message = "Nome do país é obrigatório")
		@Size(max = 100, message = "O nome do país deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ])[A-Za-zÀ-ÖØ-öø-ÿ.'\\-\\s]+$", message = "O nome do país deve estar num formato válido")
		String nome) {}

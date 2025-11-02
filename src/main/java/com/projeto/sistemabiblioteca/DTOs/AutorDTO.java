package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AutorDTO(
		@NotBlank(message = "Nome do autor é obrigatório")
		@Size(max = 100, message = "O nome do autor deve ter no máximo 100 caracteres")
		String nome) {}

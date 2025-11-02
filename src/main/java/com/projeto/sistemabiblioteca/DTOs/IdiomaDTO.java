package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IdiomaDTO(
		@NotBlank(message = "Nome do idioma é obrigatório")
		@Size(max = 100, message = "O nome do idioma deve ter no máximo 100 caracteres")
		String nome) {}

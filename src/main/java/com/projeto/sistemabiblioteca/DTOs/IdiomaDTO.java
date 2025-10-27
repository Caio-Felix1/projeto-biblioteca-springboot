package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;

public record IdiomaDTO(
		@NotBlank(message = "Nome do idioma é obrigatório")
		String nome) {}

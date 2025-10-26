package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;

public record PaisDTO(
		@NotBlank(message = "Nome do país é obrigatório")
		String nome) {}

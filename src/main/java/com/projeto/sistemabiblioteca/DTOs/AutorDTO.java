package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;

public record AutorDTO(
		@NotBlank(message = "Nome do autor é obrigatório")
		String nome) {}

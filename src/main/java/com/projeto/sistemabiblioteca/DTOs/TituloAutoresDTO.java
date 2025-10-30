package com.projeto.sistemabiblioteca.DTOs;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

public record TituloAutoresDTO(
		@NotEmpty(message = "É necessário inserir pelo menos um autor")
		Set<Long> idsAutores) {}

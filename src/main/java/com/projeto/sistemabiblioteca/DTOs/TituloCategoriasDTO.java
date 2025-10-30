package com.projeto.sistemabiblioteca.DTOs;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

public record TituloCategoriasDTO(
		@NotEmpty(message = "É necessário inserir pelo menos uma categoria")
		Set<Long> idsCategorias) {}

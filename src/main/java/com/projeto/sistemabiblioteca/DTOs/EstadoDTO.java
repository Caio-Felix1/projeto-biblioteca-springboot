package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstadoDTO(
		@NotBlank(message = "Nome do estado é obrigatório")
		String nome,
		
		@NotNull(message = "País é obrigatório")
		Long idPais) {}

package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstadoDTO(
		@NotBlank(message = "Nome do estado é obrigatório")
		@Size(max = 100, message = "O nome do estado deve ter no máximo 100 caracteres")
		String nome,
		
		@NotNull(message = "País é obrigatório")
		Long idPais) {}

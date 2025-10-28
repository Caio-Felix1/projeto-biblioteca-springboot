package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;

public record EmprestimoDTO(
		@NotBlank(message = "Usuário é obrigatório")
		Long idPessoa,
		
		@NotBlank(message = "Exemplar é obrigatório")
		Long idExemplar) {}

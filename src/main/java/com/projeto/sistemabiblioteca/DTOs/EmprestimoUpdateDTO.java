package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotNull;

public record EmprestimoUpdateDTO(
		@NotNull(message = "Usuário é obrigatório")
		Long idPessoa,
		
		@NotNull(message = "Exemplar é obrigatório")
		Long idExemplar) {}

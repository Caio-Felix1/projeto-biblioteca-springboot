package com.projeto.sistemabiblioteca.DTOs;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EmprestimoCreateDTO(
		@NotNull(message = "Usuário é obrigatório")
		Long idPessoa,
		
		@NotEmpty(message = "É obrigatório ter pelo menos uma edição")
		List<Long> idsEdicao) {}

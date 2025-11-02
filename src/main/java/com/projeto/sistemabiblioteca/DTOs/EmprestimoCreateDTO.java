package com.projeto.sistemabiblioteca.DTOs;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmprestimoCreateDTO(
		@NotNull(message = "Usuário é obrigatório")
		Long idPessoa,
		
		@NotEmpty(message = "É obrigatório ter pelo menos uma edição")
		@Size(min = 1, max = 5, message = "O pedido deve ter entre 1 e 5 edições")
		List<Long> idsEdicao) {}

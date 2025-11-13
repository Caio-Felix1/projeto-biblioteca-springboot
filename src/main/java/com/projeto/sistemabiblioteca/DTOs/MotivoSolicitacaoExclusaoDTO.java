package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MotivoSolicitacaoExclusaoDTO(
		@NotBlank(message = "Motivo de solicitação de exclusão é obrigatório")
		@Size(min = 10, max = 100, message = "O motivo de solicitação de exclusão deve ter entre 10 e 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ0-9])[A-Za-zÀ-ÖØ-öø-ÿ0-9.,:;!?@'\"()\\[\\]{}\\-\\s]+$", message = "O motivo de solicitação de exclusão deve estar num formato válido")
		String motivo) {}

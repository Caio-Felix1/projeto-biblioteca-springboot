package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MotivoRejeicaoDeCadastroDTO(
		@NotBlank(message = "Motivo de rejeição de cadastro do usuário é obrigatório")
		@Size(min = 10, max = 500, message = "O motivo de rejeição de cadastro do usuário deve ter entre 10 e 500 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ0-9])[A-Za-zÀ-ÖØ-öø-ÿ0-9.,:;!?@'\"()\\[\\]{}\\-\\s]+$", message = "O motivo de rejeição de cadastro do usuário deve estar num formato válido")
		String motivo) {}

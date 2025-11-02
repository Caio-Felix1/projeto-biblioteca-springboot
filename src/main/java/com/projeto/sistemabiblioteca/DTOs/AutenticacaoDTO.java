package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AutenticacaoDTO(
		@NotNull(message = "Email é obrigatório")
		@Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^[a-z0-9]+@[a-z]+\\.com(\\.br)?$", message = "Email deve estar num formato válido")
		String email, 
		
		@NotNull(message = "Senha é obrigatória")
		@Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres")
		String senha) {
}

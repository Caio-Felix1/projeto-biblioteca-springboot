package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TituloUpdateDTO(
		@NotBlank(message = "Título é obrigatório")
		@Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
		String nome,
		
		@NotBlank(message = "Descrição do título é obrigatória")
		@Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
		String descricao) {

}

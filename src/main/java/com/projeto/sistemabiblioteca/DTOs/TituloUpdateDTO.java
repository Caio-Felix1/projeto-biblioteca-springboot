package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;

public record TituloUpdateDTO(
		@NotBlank(message = "Título é obrigatório")
		String nome,
		
		@NotBlank(message = "Descrição do título é obrigatória")
		String descricao) {

}

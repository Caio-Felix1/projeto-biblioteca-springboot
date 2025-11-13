package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TituloUpdateDTO(
		@NotBlank(message = "Título é obrigatório")
		@Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ0-9])[A-Za-zÀ-ÖØ-öø-ÿ0-9.,:;!?&%#$@'+()\\[\\]{}*=~\\\\/|\"\\-\\s\u00AA\u00BA]+$", message = "O título deve estar num formato válido")
		String nome,
		
		@NotBlank(message = "Descrição do título é obrigatória")
		@Size(max = 1000, message = "A descrição do título deve ter no máximo 1000 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ0-9])[A-Za-zÀ-ÖØ-öø-ÿ0-9.,:;!?&%#$@'+()\\[\\]{}*=~\\\\/|\"\\-\\s\u00AA\u00BA]+$", message = "A descrição do título deve estar num formato válido")
		String descricao) {

}

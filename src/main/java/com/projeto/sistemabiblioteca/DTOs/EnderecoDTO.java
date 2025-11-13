package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
		@NotBlank(message = "Nome do logradouro é obrigatório")
		@Size(max = 100, message = "O nome do logradouro deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ])[A-Za-zÀ-ÖØ-öø-ÿ0-9.'\\-\\s]+$", message = "O nome do logradouro deve estar num formato válido")
		String nomeLogradouro,
		
		@NotBlank(message = "Número do endereço é obrigatório")
		@Size(max = 10, message = "O número do endereço deve ter no máximo 10 caracteres")
		@Pattern(regexp = "^[0-9]+[A-Za-z]?(\\-[A-Za-z0-9]+)?$", message = "O número do endereço deve estar num formato válido")
		String numero,
		
		@Size(max = 50, message = "Complemento deve ter no máximo 50 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ0-9])[A-Za-zÀ-ÖØ-öø-ÿ0-9.'\"\\-\\s]+$", message = "O complemento deve estar num formato válido")
		String complemento,
		
		@NotBlank(message = "Bairro é obrigatório")
		@Size(max = 100, message = "O nome do bairro deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ])[A-Za-zÀ-ÖØ-öø-ÿ.'\\-\\s]+$", message = "O nome do bairro deve estar num formato válido")
		String bairro,
		
		@NotBlank(message = "CEP é obrigatório")
		@Pattern(regexp = "^[0-9]{8}$", message = "O CEP deve ter 8 dígitos")
		String cep,
		
		@NotBlank(message = "Cidade é obrigatória")
		@Size(max = 100, message = "O nome da cidade deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^(?=.*[A-Za-zÀ-ÖØ-öø-ÿ])[A-Za-zÀ-ÖØ-öø-ÿ.'\\-\\s]+$", message = "O nome da cidade deve estar num formato válido")
		String cidade,
		
		@NotNull(message = "Estado é obrigatório")
		Long idEstado
		) {

}

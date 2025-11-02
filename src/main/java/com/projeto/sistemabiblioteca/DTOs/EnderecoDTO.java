package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
		@NotBlank(message = "Nome do logradouro é obrigatório")
		@Size(max = 100, message = "O nome do logradouro deve ter no máximo 100 caracteres")
		String nomeLogradouro,
		
		@NotBlank(message = "Número do endereço é obrigatório")
		@Size(max = 10, message = "O número deve ter no máximo 10 caracteres")
		String numero,
		
		@Size(max = 50, message = "Complemento deve ter no máximo 50 caracteres")
		String complemento,
		
		@NotBlank(message = "Bairro é obrigatório")
		@Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
		String bairro,
		
		@NotBlank(message = "CEP é obrigatório")
		@Pattern(regexp = "^[0-9]{8}$", message = "O CEP deve ter 8 dígitos")
		String cep,
		
		@NotBlank(message = "Cidade é obrigatória")
		@Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
		String cidade,
		
		@NotNull(message = "Estado é obrigatório")
		Long idEstado
		) {

}

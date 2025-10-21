package com.projeto.sistemabiblioteca.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(
		@NotBlank(message = "Nome do logradouro é obrigatório")
		String nomeLogradouro,
		
		@NotBlank(message = "Número do endereço é obrigatório")
		String numero,
		
		@Pattern(regexp = "^.+$", message = "O complemento não pode ser vazio se preenchido")
		String complemento,
		
		@NotBlank(message = "Bairro é obrigatório")
		String bairro,
		
		@NotBlank(message = "CEP é obrigatório")
		@Pattern(regexp = "^[0-9]{8}$")
		String cep,
		
		@NotBlank(message = "Cidade é obrigatória")
		String cidade,
		
		@NotNull(message = "Estado é obrigatório")
		Long idEstado
		) {

}

package com.projeto.sistemabiblioteca.DTOs;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PessoaDTO(
		@NotBlank(message = "Nome é obrigatório")
		String nome,
		
		@NotBlank(message = "CPF é obrigatório")
		String cpf,
		
		@NotNull(message = "Sexo é obrigatório")
		Sexo sexo,
		
		@NotNull(message = "Função do usuário é obrigatória")
		FuncaoUsuario funcao,
		
		@NotNull(message = "Data de nascimento é obrigatória")
		LocalDate dtNascimento,
		
		@NotBlank(message = "Telefone é obrigatório")
		String telefone,
		
		@NotBlank(message = "Email é obrigatório")
		String email,
		
		@NotBlank(message = "Senha é obrigatória")
		String senha,
		
		@NotNull(message = "Endereço é obrigatório")
		@Valid
		EnderecoDTO endereco
		) {

}

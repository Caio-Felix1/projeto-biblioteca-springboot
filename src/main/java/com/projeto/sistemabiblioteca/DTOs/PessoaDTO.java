package com.projeto.sistemabiblioteca.DTOs;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PessoaDTO(
		@NotBlank(message = "Nome é obrigatório")
		@Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
		String nome,
		
		@NotBlank(message = "CPF é obrigatório")
		@Pattern(regexp = "^[0-9]{11}$", message = "CPF deve ter 11 dígitos")
		String cpf,
		
		@NotNull(message = "Sexo é obrigatório")
		Sexo sexo,
		
		@NotNull(message = "Função do usuário é obrigatória")
		FuncaoUsuario funcao,
		
		@NotNull(message = "Data de nascimento é obrigatória")
		@Past(message = "Data de nascimento deve ser no passado")
		LocalDate dtNascimento,
		
		@NotBlank(message = "Telefone é obrigatório")
		@Pattern(regexp = "^[0-9]{10,11}$", message = "Telefone deve ter 10 ou 11 dígitos")
		String telefone,
		
		@NotBlank(message = "Email é obrigatório")
		@Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
		@Pattern(regexp = "^[a-z0-9]+@[a-z]+\\.com(\\.br)?$", message = "Email deve estar num formato válido")
		String email,
		
		@NotBlank(message = "Senha é obrigatória")
		@Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres")
		String senha,
		
		@NotNull(message = "Endereço é obrigatório")
		@Valid
		EnderecoDTO endereco
		) {

}

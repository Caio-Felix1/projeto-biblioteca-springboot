package com.projeto.sistemabiblioteca.DTOs;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record RegistroDTO(
		/*
        @NotBlank  String email,
        @NotBlank String senha,
        @NotNull FuncaoUsuario funcao
        */
		
		@NotBlank(message = "Nome é obrigatório")
		String nome,
		
		@NotBlank(message = "CPF é obrigatório")
		String cpf,
		
		@NotNull(message = "Sexo é obrigatório")
		Sexo sexo,
		
		@NotNull(message = "Data de nascimento é obrigatória")
		@Past(message = "Data de nascimento deve ser no passado")
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
) {}

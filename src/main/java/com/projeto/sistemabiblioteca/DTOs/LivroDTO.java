package com.projeto.sistemabiblioteca.DTOs;

import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;

import jakarta.validation.constraints.NotNull;

public record LivroDTO (
		@NotNull EstadoFisico estadoFisico,
		EdicaoDTO edicao
		) {}

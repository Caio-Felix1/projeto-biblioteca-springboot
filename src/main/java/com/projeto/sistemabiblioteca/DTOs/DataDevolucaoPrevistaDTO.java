package com.projeto.sistemabiblioteca.DTOs;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record DataDevolucaoPrevistaDTO(
		@NotNull(message = "Deve ser informado a data de devolução prevista")
		@Future(message = "A data de devolução prevista deve ser no futuro")
		LocalDate dtDevolucaoPrevista) {}

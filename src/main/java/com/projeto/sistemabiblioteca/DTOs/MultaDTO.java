package com.projeto.sistemabiblioteca.DTOs;

import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

public record MultaDTO(
		Long idMulta,
		double valor,
		StatusPagamento statusPagamento) {}

package com.projeto.sistemabiblioteca.DTOs.Response;

import jakarta.validation.constraints.NotNull;

public record TituloResponseDTO(
        Long id_titulo,
        String descricao,
        String nome,
        String status
) {}


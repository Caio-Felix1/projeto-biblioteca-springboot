package com.projeto.sistemabiblioteca.DTOs.Response;

import jakarta.validation.constraints.NotNull;

public record EditoraResponseDTO(
        Long id_editora,
        String nome,
        String status
) {}


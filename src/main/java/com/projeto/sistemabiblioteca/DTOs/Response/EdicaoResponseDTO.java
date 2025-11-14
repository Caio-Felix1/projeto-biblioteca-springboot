package com.projeto.sistemabiblioteca.DTOs.Response;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EdicaoResponseDTO(
        @NotNull Long id_edicao,
        @NotNull String classificacao,
        LocalDate dt_publicacao,
        @NotNull Integer qtd_paginas,
        @NotNull String tamanho,
        @NotNull String tipo_capa,
        String descricao_edicao,
        String imagem_url,
        EditoraResponseDTO editora,
        IdiomaResponseDTO idioma,
        TituloResponseDTO titulo,
        @NotNull String status
) {}

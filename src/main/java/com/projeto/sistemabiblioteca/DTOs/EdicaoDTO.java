package com.projeto.sistemabiblioteca.DTOs;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record EdicaoDTO(
		@NotNull(message = "Tipo de capa é obrigatório")
        TipoCapa tipoCapa,
        
        @NotNull(message = "Quantidade de capas é obrigatória")
		@Min(value = 1, message = "Quantidade de páginas deve ser no mínimo 1")
		
        Integer qtdPaginas,
        
        @NotNull(message = "Tamanho da edição é obrigatório")
        TamanhoEdicao tamanho,
        
        @NotNull(message = "Classificação da edição é obrigatória")
        ClassificacaoIndicativa classificacao,
        
        @NotNull(message = "Data de publicação é obrigatória")
		@PastOrPresent(message = "Data de publicação não pode ser no futuro")
        LocalDate dtPublicacao,
        
        @NotNull(message = "Título é obrigatório")
        Long tituloId,
        
        @NotNull(message = "Editora é obrigatória")
        Long editoraId,
        
        @NotNull(message = "Idioma é obrigatório")
        Long idiomaId
		) {}

package com.projeto.sistemabiblioteca.DTOs;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record EdicaoDTO(
		@NotBlank(message = "Descrição da edição é obrigatória")
		@Size(max = 60, message = "A descrição da edição deve ter no máximo 60 caracteres")
		String descricaoEdicao,
		
		@NotNull(message = "Tipo de capa é obrigatório")
        TipoCapa tipoCapa,
        
        @NotNull(message = "Quantidade de páginas é obrigatória")
		@Min(value = 1, message = "Deve ter no mínimo 1 página")
		@Max(value = 5000, message = "Deve ter no máximo 5 mil páginas")
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

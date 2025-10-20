package com.projeto.sistemabiblioteca.DTOs;

import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;

import java.time.LocalDate;

public record EdicaoCreateDTO(
        TipoCapa tipoCapa,
        int qtdPaginas,
        TamanhoEdicao tamanho,
        ClassificacaoIndicativa classificacao,
        LocalDate dtPublicacao,
        StatusAtivo status,
        long tituloId,
        long editoraId,
        long idiomaId
) {}

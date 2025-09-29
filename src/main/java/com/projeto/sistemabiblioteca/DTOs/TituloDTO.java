package com.projeto.sistemabiblioteca.DTOs;

import java.util.List;

public record TituloDTO(
		Long idTitulo,
		String nome,
		String descricao,
		List<CategoriaDTO> categorias,
		List<AutorDTO> autores) {}

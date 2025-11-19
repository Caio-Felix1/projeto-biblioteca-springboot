package com.projeto.sistemabiblioteca.DTOs;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponseDTO<T>(
		List<T> conteudo,
		int numeroDaPagina,
		int tamanhoDaPagina,
		int totalElementosDaPagina,
		long totalElementos,
		int totalPaginas,
		boolean primeiraPagina,
		boolean ultimaPagina,
		boolean temProximaPagina,
		boolean temPaginaAnterior) {
	
	public static <T> PageResponseDTO<T> converterParaDTO(Page<T> pagina) {
		return new PageResponseDTO<>(
				pagina.getContent(),
				pagina.getNumber(),
				pagina.getSize(),
				pagina.getNumberOfElements(),
				pagina.getTotalElements(),
				pagina.getTotalPages(),
				pagina.isFirst(),
				pagina.isLast(),
				pagina.hasNext(),
				pagina.hasPrevious());
	}
}



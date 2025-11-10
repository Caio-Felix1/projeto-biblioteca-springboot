package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimCategoriaRepository;
import com.projeto.sistemabiblioteca.entities.Categoria;

import jakarta.transaction.Transactional;

@Service
public class DimCategoriaService {
	
	private DimCategoriaRepository dimCategoriaRepository;

	public DimCategoriaService(DimCategoriaRepository dimCategoriaRepository) {
		this.dimCategoriaRepository = dimCategoriaRepository;
	}
	
	@Transactional
	public DimCategoria atualizar(Categoria categoriaReal) {
		Optional<DimCategoria> dimCategoriaExistente = dimCategoriaRepository.findByIdNatural(categoriaReal.getIdCategoria());
		
		if (dimCategoriaExistente.isPresent()) {
			DimCategoria dimCategoria = dimCategoriaExistente.get();
			atualizarDados(dimCategoria, categoriaReal);
			return dimCategoriaRepository.save(dimCategoria);
		}
		else {
			DimCategoria dimNovo = new DimCategoria(categoriaReal);
			return dimCategoriaRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimCategoria dimCategoria, Categoria categoriaReal) {
		dimCategoria.setNome(categoriaReal.getNome());
		dimCategoria.setStatus(categoriaReal.getStatusAtivo());
	}
}

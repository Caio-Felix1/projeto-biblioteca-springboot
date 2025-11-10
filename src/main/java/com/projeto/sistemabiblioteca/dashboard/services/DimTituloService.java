package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimTitulo;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimTituloRepository;
import com.projeto.sistemabiblioteca.entities.Titulo;

import jakarta.transaction.Transactional;

@Service
public class DimTituloService {
	
	private DimTituloRepository dimTituloRepository;

	public DimTituloService(DimTituloRepository dimTituloRepository) {
		this.dimTituloRepository = dimTituloRepository;
	}
	
	@Transactional
	public DimTitulo atualizar(Titulo tituloReal, Set<DimCategoria> categorias, Set<DimAutor> autores) {
		Optional<DimTitulo> dimTituloExistente = dimTituloRepository.findByIdNatural(tituloReal.getIdTitulo());
		
		if (dimTituloExistente.isPresent()) {
			DimTitulo dimTitulo = dimTituloExistente.get();
			atualizarDados(dimTitulo, tituloReal);
			
			atualizarCategorias(dimTitulo, categorias);
			atualizarAutores(dimTitulo, autores);
			
			return dimTituloRepository.save(dimTitulo);
		}
		else {
			DimTitulo dimNovo = new DimTitulo(tituloReal, categorias, autores);
			return dimTituloRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimTitulo dimTitulo, Titulo tituloReal) {
		dimTitulo.setNome(tituloReal.getNome());
		dimTitulo.setStatus(tituloReal.getStatusAtivo());
	}
	
	private void atualizarAutores(DimTitulo dimTitulo, Set<DimAutor> autores) {
		dimTitulo.removerTodosAutores();
		
		autores.forEach(a -> dimTitulo.adicionarAutor(a));
	}
	
	private void atualizarCategorias(DimTitulo dimTitulo, Set<DimCategoria> categorias) {
		dimTitulo.removerTodasCategorias();
		
		categorias.forEach(c -> dimTitulo.adicionarCategoria(c));
	}
}

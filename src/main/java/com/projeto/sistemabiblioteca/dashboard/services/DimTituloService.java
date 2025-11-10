package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimTitulo;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimAutorRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimCategoriaRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimTituloRepository;
import com.projeto.sistemabiblioteca.entities.Titulo;

import jakarta.transaction.Transactional;

public class DimTituloService {
	
	private DimTituloRepository dimTituloRepository;
	
	private DimAutorRepository dimAutorRepository;
	
	private DimCategoriaRepository dimCategoriaRepository;

	public DimTituloService(DimTituloRepository dimTituloRepository, DimAutorRepository dimAutorRepository, DimCategoriaRepository dimCategoriaRepository) {
		this.dimTituloRepository = dimTituloRepository;
		this.dimAutorRepository = dimAutorRepository;
		this.dimCategoriaRepository = dimCategoriaRepository;
	}
	
	@Transactional
	public void atualizar(Titulo tituloReal) {
		Optional<DimTitulo> dimTituloExistente = dimTituloRepository.findByIdNatural(tituloReal.getIdTitulo());
		
		if (dimTituloExistente.isPresent()) {
			DimTitulo dimTitulo = dimTituloExistente.get();
			atualizarDados(dimTitulo, tituloReal);
			atualizarAutores(dimTitulo, tituloReal);
			atualizarCategorias(dimTitulo, tituloReal);
			dimTituloRepository.save(dimTitulo);
		}
		else {
			DimTitulo dimNovo = new DimTitulo(tituloReal);
			dimTituloRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimTitulo dimTitulo, Titulo tituloReal) {
		dimTitulo.setNome(tituloReal.getNome());
		dimTitulo.setStatus(tituloReal.getStatusAtivo());
	}
	
	private void atualizarAutores(DimTitulo dimTitulo, Titulo tituloReal) {
		dimTitulo.getAutores().clear();
		
		tituloReal.getAutores().forEach(autorReal -> {
			Optional<DimAutor> dimAutorExistente = dimAutorRepository.findByIdNatural(autorReal.getIdAutor());
			if (dimAutorExistente.isPresent()) {
				dimTitulo.adicionarAutor(dimAutorExistente.get());
			}
		});
	}
	
	private void atualizarCategorias(DimTitulo dimTitulo, Titulo tituloReal) {
		dimTitulo.getCategorias().clear();
		
		tituloReal.getCategorias().forEach(categoriaReal -> {
			Optional<DimCategoria> dimCategoriaExistente = dimCategoriaRepository.findByIdNatural(categoriaReal.getIdCategoria());
			if (dimCategoriaExistente.isPresent()) {
				dimTitulo.adicionarCategoria(dimCategoriaExistente.get());
			}
		});
	}
}

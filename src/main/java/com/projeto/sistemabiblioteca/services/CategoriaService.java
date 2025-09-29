package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
	
	private CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}
	
	public List<Categoria> buscarTodos() {
		return categoriaRepository.findAll();
	}
	
	public List<Categoria> buscarTodosComNomeContendo(String nome) {
		return categoriaRepository.findAllByNomeContainingIgnoreCase(nome);
	}
	
	public Categoria buscarPorId(Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (categoria.isEmpty()) {
			throw new EntityNotFoundException("Erro: categoria com id correspondente não foi encontrada.");
		}
		return categoria.get();
	}
	
	public Categoria inserir(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public void deletar(Long id) {
		categoriaRepository.deleteById(id);
	}
	
	public Categoria atualizar(Long id, Categoria categoria2) {
		Categoria categoria1 = buscarPorId(id);
		atualizarDados(categoria1, categoria2);
		return categoriaRepository.save(categoria1);
	}
	
	private void atualizarDados(Categoria categoria1, Categoria categoria2) {
		categoria1.setNome(categoria2.getNome());
	}
}

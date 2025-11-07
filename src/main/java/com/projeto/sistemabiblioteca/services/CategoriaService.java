package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.CategoriaJaCadastradaException;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

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
	
	public List<Categoria> buscarTodosComStatusIgualA(StatusAtivo status) {
		return categoriaRepository.findAllByStatusEquals(status);
	}
	
	public List<Categoria> buscarTodosPorId(Set<Long> ids) {
		return categoriaRepository.findAllById(ids);
	}
	
	public Categoria buscarPorId(Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		if (categoria.isEmpty()) {
			throw new EntityNotFoundException("Erro: categoria com id correspondente não foi encontrada.");
		}
		return categoria.get();
	}
	
	public void verificarSeCategoriaJaExiste(String nomeCategoria) {
		if (categoriaRepository.existsByNome(nomeCategoria)) {
			throw new CategoriaJaCadastradaException("Erro: categoria já foi cadastrada.");
		}
	}
	
	public Categoria inserir(Categoria categoria) {
		verificarSeCategoriaJaExiste(categoria.getNome());
		return categoriaRepository.save(categoria);
	}
	
	public void inativar(Long id) {
		Categoria categoria = buscarPorId(id);
		if (categoria.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: categoria já está inativa.");
		}
		categoria.inativar();
		categoriaRepository.save(categoria);
	}
	
	public Categoria atualizar(Long id, Categoria categoria2) {
		Categoria categoria1 = buscarPorId(id);
		
		if(!categoria1.getNome().equals(categoria2.getNome())) {
			verificarSeCategoriaJaExiste(categoria2.getNome());
		}
		
		atualizarDados(categoria1, categoria2);
		return categoriaRepository.save(categoria1);
	}
	
	private void atualizarDados(Categoria categoria1, Categoria categoria2) {
		categoria1.setNome(categoria2.getNome());
	}
}

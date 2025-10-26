package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AutorService {
	
	private AutorRepository autorRepository;
	
	public AutorService(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}
	
	public List<Autor> buscarTodos() {
		return autorRepository.findAll();
	}
	
	public List<Autor> buscarTodosComNomeContendo(String nome) {
		return autorRepository.findAllByNomeContainingIgnoreCase(nome);
	}
	
	public List<Autor> buscarTodosComStatusIgualA(StatusAtivo status) {
		return autorRepository.findAllByStatusEquals(status);
	}
	
	public List<Autor> buscarTodosPorId(Set<Long> ids) {
		return autorRepository.findAllById(ids);
	}
	
	public Autor buscarPorId(Long id) {
		Optional<Autor> autor = autorRepository.findById(id);
		if (autor.isEmpty()) {
			throw new EntityNotFoundException("Erro: autor com id correspondente não foi encontrado.");
		}
		return autor.get();
	}
	
	public Autor inserir(Autor autor) {
		return autorRepository.save(autor);
	}
	
	public void inativar(Long id) {
		Autor autor = buscarPorId(id);
		if (autor.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: autor já está inativo.");
		}
		autor.inativar();
		autorRepository.save(autor);
	}
	
	public Autor atualizar(Long id, Autor autor2) {
		Autor autor1 = buscarPorId(id);
		atualizarDados(autor1, autor2);
		return autorRepository.save(autor1);
	}
	
	private void atualizarDados(Autor autor1, Autor autor2) {
		autor1.setNome(autor2.getNome());
	}
}

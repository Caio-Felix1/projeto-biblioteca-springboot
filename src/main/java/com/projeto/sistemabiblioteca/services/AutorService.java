package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;

import jakarta.persistence.EntityNotFoundException;

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
	
	public Autor buscarPorId(Long id) {
		Optional<Autor> autor = autorRepository.findById(id);
		if (autor.isEmpty()) {
			throw new EntityNotFoundException("Erro: usuário com id correspondente não foi encontrado.");
		}
		return autor.get();
	}
	
	public Autor inserir(Autor autor) {
		return autorRepository.save(autor);
	}
	
	public void deletar(Long id) {
		autorRepository.deleteById(id);
	}
	
	public Autor atualizar(Long id, Autor autor2) {
		Autor autor1 = buscarPorId(id);
		atualizarDados(autor1, autor2);
		return autorRepository.save(autor1);
	}
	
	private void atualizarDados(Autor a1, Autor a2) {
		a1.setNome(a2.getNome());
	}
}

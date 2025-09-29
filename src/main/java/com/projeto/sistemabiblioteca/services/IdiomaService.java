package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class IdiomaService {
	
	private IdiomaRepository idiomaRepository;
	
	public IdiomaService(IdiomaRepository idiomaRepository) {
		this.idiomaRepository = idiomaRepository;
	}
	
	public List<Idioma> buscarTodos() {
		return idiomaRepository.findAll();
	}
	
	public Idioma buscarPorId(Long id) {
		Optional<Idioma> idioma = idiomaRepository.findById(id);
		if (idioma.isEmpty()) {
			throw new EntityNotFoundException("Erro: idioma com id correspondente não foi encontrado.");
		}
		return idioma.get();
	}
	
	public Idioma inserir(Idioma idioma) {
		return idiomaRepository.save(idioma);
	}
	
	public void deletar(Long id) {
		idiomaRepository.deleteById(id);
	}
	
	public Idioma atualizar(Long id, Idioma idioma2) {
		Idioma idioma1 = buscarPorId(id);
		atualizarDados(idioma1, idioma2);
		return idiomaRepository.save(idioma1);
	}
	
	private void atualizarDados(Idioma idioma1, Idioma idioma2) {
		idioma1.setNome(idioma2.getNome());
	}
}

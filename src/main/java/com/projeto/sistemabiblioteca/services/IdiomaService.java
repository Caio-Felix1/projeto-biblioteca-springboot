package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.IdiomaJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class IdiomaService {
	
	private IdiomaRepository idiomaRepository;
	
	public IdiomaService(IdiomaRepository idiomaRepository) {
		this.idiomaRepository = idiomaRepository;
	}
	
	public List<Idioma> buscarTodos() {
		return idiomaRepository.findAll();
	}
	
	public List<Idioma> buscarTodosComStatusIgualA(StatusAtivo status) {
		return idiomaRepository.findAllByStatusEquals(status);
	}
	
	public Idioma buscarPorId(Long id) {
		Optional<Idioma> idioma = idiomaRepository.findById(id);
		if (idioma.isEmpty()) {
			throw new EntityNotFoundException("Erro: idioma com id correspondente não foi encontrado.");
		}
		return idioma.get();
	}
	
	public void verificarSeIdiomaJaExiste(String nomeIdioma) {
		if (idiomaRepository.existsByNome(nomeIdioma)) {
			throw new IdiomaJaCadastradoException("Erro: idioma já foi cadastrado.");
		}
	}
	
	public Idioma inserir(Idioma idioma) {
		verificarSeIdiomaJaExiste(idioma.getNome());
		return idiomaRepository.save(idioma);
	}
	
	public void inativar(Long id) {
		Idioma idioma = buscarPorId(id);
		if (idioma.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: idioma já está inativo.");
		}
		idioma.inativar();
		idiomaRepository.save(idioma);
	}
	
	public Idioma atualizar(Long id, Idioma idioma2) {
		Idioma idioma1 = buscarPorId(id);
		
		if (!idioma1.getNome().equals(idioma2.getNome())) {
			verificarSeIdiomaJaExiste(idioma2.getNome());
		}
		
		atualizarDados(idioma1, idioma2);
		return idiomaRepository.save(idioma1);
	}
	
	private void atualizarDados(Idioma idioma1, Idioma idioma2) {
		idioma1.setNome(idioma2.getNome());
	}
}

package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ExemplarService {
	
	private ExemplarRepository exemplarRepository;

	public ExemplarService(ExemplarRepository exemplarRepository) {
		this.exemplarRepository = exemplarRepository;
	}
	
	public List<Exemplar> buscarTodos() {
		return exemplarRepository.findAll();
	}
	
	public List<Exemplar> buscarTodosComStatusIgualA(StatusExemplar statusExemplar) {
		return exemplarRepository.findAllByStatusEquals(statusExemplar);
	}
	
	public Exemplar buscarPorId(Long id) {
		Optional<Exemplar> exemplar = exemplarRepository.findById(id);
		if (exemplar.isEmpty()) {
			throw new EntityNotFoundException("Erro: exemplar com id correspondente não foi encontrado.");
		}
		return exemplar.get();
	}
	
	public Exemplar inserir(Exemplar exemplar) {
		return exemplarRepository.save(exemplar);
	}
	
	public void deletar(Long id) {
		exemplarRepository.deleteById(id);
	}
	
	public Exemplar atualizar(Long id, Exemplar exemplar2) {
		Exemplar exemplar1 = buscarPorId(id);
		atualizarDados(exemplar1, exemplar2);
		return exemplarRepository.save(exemplar1);
	}
	
	private void atualizarDados(Exemplar exemplar1, Exemplar exemplar2) {
		exemplar1.setEstadoFisico(exemplar2.getEstadoFisico());
		exemplar1.setEdicao(exemplar2.getEdicao());
	}
}

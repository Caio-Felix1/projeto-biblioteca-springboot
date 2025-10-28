package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MultaService {
	
	private MultaRepository multaRepository;

	public MultaService(MultaRepository multaRepository) {
		this.multaRepository = multaRepository;
	}
	
	public List<Multa> buscarTodos() {
		return multaRepository.findAll();
	}
	
	public List<Multa> buscarTodosComStatusPagamentoIgualA(StatusPagamento statusPagamento) {
		return multaRepository.findAllByStatusPagamentoEquals(statusPagamento);
	}
	
	public Multa buscarPorId(Long id) {
		Optional<Multa> multa = multaRepository.findById(id);
		if (multa.isEmpty()) {
			throw new EntityNotFoundException("Erro: multa com id correspondente não foi encontrada.");
		}
		return multa.get();
	}
	
	public Multa inserir(Multa multa) {
		return multaRepository.save(multa);
	}
	
	public void perdoarMulta(Long id) {
		Multa multa = buscarPorId(id);
		multa.perdoarMulta();
		multaRepository.save(multa);
	}
}

package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {
	
	private EstadoRepository estadoRepository;

	public EstadoService(EstadoRepository estadoRepository) {
		this.estadoRepository = estadoRepository;
	}
	
	public List<Estado> buscarTodos() {
		return estadoRepository.findAll();
	}
	
	public Estado buscarPorId(Long id) {
		Optional<Estado> estado = estadoRepository.findById(id);
		if (estado.isEmpty()) {
			throw new EntityNotFoundException("Erro: estado com id correspondente não foi encontrado.");
		}
		return estado.get();
	}
	
	public Estado inserir(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	public void deletar(Long id) {
		estadoRepository.deleteById(id);
	}
	
	public Estado atualizar(Long id, Estado estado2) {
		Estado estado1 = buscarPorId(id);
		atualizarDados(estado1, estado2);
		return estadoRepository.save(estado1);
	}
	
	private void atualizarDados(Estado estado1, Estado estado2) {
		estado1.setNome(estado2.getNome());
	}
}

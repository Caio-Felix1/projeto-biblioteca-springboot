package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PaisService {
	
	private PaisRepository paisRepository;

	public PaisService(PaisRepository paisRepository) {
		this.paisRepository = paisRepository;
	}
	
	public List<Pais> buscarTodos() {
		return paisRepository.findAll();
	}
	
	public Pais buscarPorId(Long id) {
		Optional<Pais> pais = paisRepository.findById(id);
		if (pais.isEmpty()) {
			throw new EntityNotFoundException("Erro: país com id correspondente não foi encontrado.");
		}
		return pais.get();
	}
	
	public Pais inserir(Pais pais) {
		return paisRepository.save(pais);
	}
	
	public void deletar(Long id) {
		paisRepository.deleteById(id);
	}
	
	public Pais atualizar(Long id, Pais pais2) {
		Pais pais1 = buscarPorId(id);
		atualizarDados(pais1, pais2);
		return paisRepository.save(pais1);
	}
	
	private void atualizarDados(Pais pais1, Pais pais2) {
		pais1.setNome(pais2.getNome());
	}
}

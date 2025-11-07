package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.PaisJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PaisService {
	
	private PaisRepository paisRepository;

	public PaisService(PaisRepository paisRepository) {
		this.paisRepository = paisRepository;
	}
	
	public List<Pais> buscarTodos() {
		return paisRepository.findAll();
	}
	
	public List<Pais> buscarTodosComStatusIgualA(StatusAtivo status) {
		return paisRepository.findAllByStatusEquals(status);
	}
	
	public Pais buscarPorId(Long id) {
		Optional<Pais> pais = paisRepository.findById(id);
		if (pais.isEmpty()) {
			throw new EntityNotFoundException("Erro: país com id correspondente não foi encontrado.");
		}
		return pais.get();
	}
	
	public void verificarSePaisJaExiste(String nomePais) {
		if (paisRepository.existsByNome(nomePais)) {
			throw new PaisJaCadastradoException("Erro: país já foi cadastrado.");
		}
	}
	
	public Pais inserir(Pais pais) {
		verificarSePaisJaExiste(pais.getNome());
		return paisRepository.save(pais);
	}
	
	public void inativar(Long id) {
		Pais pais = buscarPorId(id);
		if (pais.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: país já está inativo.");
		}
		pais.inativar();
		paisRepository.save(pais);
	}
	
	public Pais atualizar(Long id, Pais pais2) {
		Pais pais1 = buscarPorId(id);
		
		if (!pais1.getNome().equals(pais2.getNome())) {
			verificarSePaisJaExiste(pais2.getNome());
		}
		
		atualizarDados(pais1, pais2);
		return paisRepository.save(pais1);
	}
	
	private void atualizarDados(Pais pais1, Pais pais2) {
		pais1.setNome(pais2.getNome());
	}
}

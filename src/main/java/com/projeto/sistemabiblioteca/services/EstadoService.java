package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.EstadoDTO;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.EstadoJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EstadoService {
	
	private EstadoRepository estadoRepository;
	
	private PaisService paisService;

	public EstadoService(EstadoRepository estadoRepository, PaisService paisService) {
		this.estadoRepository = estadoRepository;
		this.paisService = paisService;
	}
	
	public List<Estado> buscarTodos() {
		return estadoRepository.findAll();
	}
	
	public List<Estado> buscarTodosComStatusIgualA(StatusAtivo status) {
		return estadoRepository.findAllByStatusEquals(status);
	}
	
	public Estado buscarPorId(Long id) {
		Optional<Estado> estado = estadoRepository.findById(id);
		if (estado.isEmpty()) {
			throw new EntityNotFoundException("Erro: estado com id correspondente não foi encontrado.");
		}
		return estado.get();
	}
	
	public void verificarSeEstadoJaExiste(String nomeEstado) {
		if (estadoRepository.existsByNome(nomeEstado)) {
			throw new EstadoJaCadastradoException("Erro: estado já foi cadastrado.");
		}
	}
	
	public Estado cadastrar(EstadoDTO estadoDTO) {
		Pais pais = paisService.buscarPorId(estadoDTO.idPais());
		
		if (pais.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalArgumentException("Erro: não é possível associar um novo estado a um país com status inativo.");
		}
		
		verificarSeEstadoJaExiste(estadoDTO.nome());
		
		Estado estado = new Estado(estadoDTO.nome(), pais);
		
		return inserir(estado);
	}
	
	public Estado inserir(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	public void inativar(Long id) {
		Estado estado = buscarPorId(id);
		if (estado.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: estado já está inativo.");
		}
		estado.inativar();
		estadoRepository.save(estado);
	}
	
	public Estado atualizar(Long id, EstadoDTO estadoDTO) {
		Estado estado1 = buscarPorId(id);
		
		Pais pais;
		if (estado1.getPais().getIdPais().equals(estadoDTO.idPais())) {
			pais = estado1.getPais();
		}
		else {
			pais = paisService.buscarPorId(estadoDTO.idPais());
			
			if (pais.getStatusAtivo() == StatusAtivo.INATIVO) {
				throw new IllegalArgumentException("Erro: não é possível associar um estado a um país com status inativo ao atualizar.");
			}
		}
		
		if (!estado1.getNome().equals(estadoDTO.nome())) {
			verificarSeEstadoJaExiste(estadoDTO.nome());
		}
		
		Estado estado2 = new Estado(estadoDTO.nome(), pais);
		
		atualizarDados(estado1, estado2);
		return estadoRepository.save(estado1);
	}
	
	private void atualizarDados(Estado estado1, Estado estado2) {
		estado1.setNome(estado2.getNome());
		estado1.setPais(estado2.getPais());
	}
}

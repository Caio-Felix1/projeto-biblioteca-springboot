package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.ExemplarDTO;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.exceptions.ExemplarNaoDisponivelException;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ExemplarService {
	
	private ExemplarRepository exemplarRepository;
	private final EdicaoService edicaoService;

	public ExemplarService(ExemplarRepository exemplarRepository,EdicaoService edicaoService) {
		this.exemplarRepository = exemplarRepository;
		this.edicaoService = edicaoService;
	}
	
	public List<Exemplar> buscarTodos() {
		return exemplarRepository.findAll();
	}
	
	public List<Exemplar> buscarTodosComStatusIgualA(StatusExemplar statusExemplar) {
		return exemplarRepository.findAllByStatusEquals(statusExemplar);
	}
	
	public List<Exemplar> buscarTodosComEdicaoComIdIgualA(Long id) {
		return exemplarRepository.findAllByEdicaoIdEdicao(id);
	}
	
	public Exemplar buscarPrimeiroComEdicaoComIdIgualA(Long id, StatusExemplar status) {
		return exemplarRepository.findAllByEdicaoOrderByEstadoFisicoCustom(id, status)
				.stream()
				.findFirst()
				.orElseThrow(() -> new ExemplarNaoDisponivelException("Erro: não tem exemplar disponível para essa edição."));
	}
	
	public Exemplar buscarPorId(Long id) {
		Optional<Exemplar> exemplar = exemplarRepository.findById(id);
		if (exemplar.isEmpty()) {
			throw new EntityNotFoundException("Erro: exemplar com id correspondente não foi encontrado.");
		}
		return exemplar.get();
	}
	
	@Transactional
	public Exemplar cadastrarExemplar(ExemplarDTO exemplarDTO) {
		Edicao edicao = edicaoService.buscarPorId(exemplarDTO.edicaoId());
		
		if (edicao.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalArgumentException("Erro: não é possível associar um exemplar a uma edição com status inativo.");
		}
		
		Exemplar exemplar = new Exemplar(exemplarDTO.estadoFisico(), edicao);
		
		return inserir(exemplar);
	}
	
	public Exemplar inserir(Exemplar exemplar) {
		return exemplarRepository.save(exemplar);
	}
	
	public void remover(Long id) {
		Exemplar exemplar = buscarPorId(id);
		
		if (exemplar.getStatus() == StatusExemplar.REMOVIDO) {
			throw new IllegalStateException("Erro: exemplar já foi removido.");
		}
		
		exemplar.remover();
		exemplarRepository.save(exemplar);
	}
	
	@Transactional
	public Exemplar atualizar(Long id, ExemplarDTO exemplarDTO) {
		Exemplar exemplar1 = buscarPorId(id);
		
		Edicao edicao;
		if (exemplar1.getEdicao().getIdEdicao().equals(exemplarDTO.edicaoId())) {
			edicao = exemplar1.getEdicao();
		}
		else {
			edicao = edicaoService.buscarPorId(exemplarDTO.edicaoId());
			
			if (edicao.getStatusAtivo() == StatusAtivo.INATIVO) {
				throw new IllegalArgumentException("Erro: não é possível associar um exemplar a uma edição com status inativo ao atualizar.");
			}
		}
		
		Exemplar exemplar2 = new Exemplar(exemplarDTO.estadoFisico(), edicao);
		
		atualizarDados(exemplar1, exemplar2);
		return exemplarRepository.save(exemplar1);
	}
	
	private void atualizarDados(Exemplar exemplar1, Exemplar exemplar2) {
		exemplar1.setEstadoFisico(exemplar2.getEstadoFisico());
		exemplar1.setEdicao(exemplar2.getEdicao());
	}
	
	public void solicitarExclusaoDoExemplar(Long id) {
		Exemplar exemplar = buscarPorId(id);
		
		exemplar.solicitarExclusao();
		exemplarRepository.save(exemplar);
	}
	
	public void rejeitarSolicitacaoDeExclusaoDoExemplar(Long id) {
		Exemplar exemplar = buscarPorId(id);
		
		exemplar.rejeitarExclusao();
		exemplarRepository.save(exemplar);
	}
}

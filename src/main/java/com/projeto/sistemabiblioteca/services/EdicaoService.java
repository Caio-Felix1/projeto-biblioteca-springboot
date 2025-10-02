package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EdicaoService {
	
	private EdicaoRepository edicaoRepository;

	public EdicaoService(EdicaoRepository edicaoRepository) {
		this.edicaoRepository = edicaoRepository;
	}
	
	public List<Edicao> buscarTodos() {
		return edicaoRepository.findAll();
	}
	
	public List<Edicao> buscarTodosComStatusIgualA(StatusAtivo status) {
		return edicaoRepository.findAllByStatusEquals(status);
	}
	
	public Edicao buscarPorId(Long id) {
		Optional<Edicao> edicao = edicaoRepository.findById(id);
		if (edicao.isEmpty()) {
			throw new EntityNotFoundException("Erro: edição com id correspondente não foi encontrada.");
		}
		return edicao.get();
	}
	
	public Edicao inserir(Edicao edicao) {
		return edicaoRepository.save(edicao);
	}
	
	public void inativar(Long id) {
		Edicao edicao = buscarPorId(id);
		if (edicao.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: edição já está inativa.");
		}
		edicao.inativar();
		edicaoRepository.save(edicao);
	}
	
	public Edicao atualizar(Long id, Edicao edicao2) {
		Edicao edicao1 = buscarPorId(id);
		atualizarDados(edicao1, edicao2);
		return edicaoRepository.save(edicao1);
	}
	
	private void atualizarDados(Edicao edicao1, Edicao edicao2) {
		edicao1.setTipoCapa(edicao2.getTipoCapa());
		edicao1.setQtdPaginas(edicao2.getQtdPaginas());
		edicao1.setTamanho(edicao2.getTamanho());
		edicao1.setIdioma(edicao2.getIdioma());
		edicao1.setClassificacao(edicao2.getClassificacao());
		edicao1.setDtPublicacao(edicao2.getDtPublicacao());
		edicao1.setTitulo(edicao2.getTitulo());
		edicao1.setEditora(edicao2.getEditora());
	}
}

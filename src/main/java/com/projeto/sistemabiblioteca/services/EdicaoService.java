package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;

import jakarta.persistence.EntityNotFoundException;

public class EdicaoService {
	
	private EdicaoRepository edicaoRepository;

	public EdicaoService(EdicaoRepository edicaoRepository) {
		this.edicaoRepository = edicaoRepository;
	}
	
	public List<Edicao> buscarTodos() {
		return edicaoRepository.findAll();
	}
	
	public Edicao buscarPorId(Long id) {
		Optional<Edicao> edicao = edicaoRepository.findById(id);
		if (edicao.isEmpty()) {
			throw new EntityNotFoundException("Erro: edicao com id correspondente não foi encontrado.");
		}
		return edicao.get();
	}
	
	public Edicao inserir(Edicao edicao) {
		return edicaoRepository.save(edicao);
	}
	
	public void deletar(Long id) {
		edicaoRepository.deleteById(id);
	}
	
	public Edicao atualizar(Long id, Edicao edicao2) {
		Edicao edicao1 = buscarPorId(id);
		atualizarDados(edicao1, edicao2);
		return edicaoRepository.save(edicao1);
	}
	
	private void atualizarDados(Edicao e1, Edicao e2) {
		e1.setTipoCapa(e2.getTipoCapa());
		e1.setQtdPaginas(e2.getQtdPaginas());
		e1.setTamanho(e2.getTamanho());
		e1.setIdioma(e2.getIdioma());
		e1.setClassificacao(e2.getClassificacao());
		e1.setDtPublicacao(e2.getDtPublicacao());
		e1.setTitulo(e2.getTitulo());
		e1.setEditora(e2.getEditora());
	}
}

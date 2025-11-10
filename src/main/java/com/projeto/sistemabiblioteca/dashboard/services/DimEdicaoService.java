package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEdicao;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimEdicaoRepository;
import com.projeto.sistemabiblioteca.entities.Edicao;

import jakarta.transaction.Transactional;

@Service
public class DimEdicaoService {
	
	private DimEdicaoRepository dimEdicaoRepository;

	public DimEdicaoService(DimEdicaoRepository dimEdicaoRepository) {
		this.dimEdicaoRepository = dimEdicaoRepository;
	}
	
	@Transactional
	public DimEdicao atualizar(Edicao edicaoReal) {
		Optional<DimEdicao> dimEdicaoExistente = dimEdicaoRepository.findByIdNatural(edicaoReal.getIdEdicao());
		
		if (dimEdicaoExistente.isPresent()) {
			DimEdicao dimEdicao = dimEdicaoExistente.get();
			atualizarDados(dimEdicao, edicaoReal);
			return dimEdicaoRepository.save(dimEdicao);
		}
		else {
			DimEdicao dimNovo = new DimEdicao(edicaoReal);
			return dimEdicaoRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimEdicao dimEdicao, Edicao edicaoReal) {
		dimEdicao.setDescricaoEdicao(edicaoReal.getDescricaoEdicao());
		dimEdicao.setTipoCapa(edicaoReal.getTipoCapa());
		dimEdicao.setQtdPaginas(edicaoReal.getQtdPaginas());
		dimEdicao.setTamanho(edicaoReal.getTamanho());
		dimEdicao.setClassificacao(edicaoReal.getClassificacao());
		dimEdicao.setDtPublicacao(edicaoReal.getDtPublicacao());
		dimEdicao.setStatus(edicaoReal.getStatusAtivo());
	}
}

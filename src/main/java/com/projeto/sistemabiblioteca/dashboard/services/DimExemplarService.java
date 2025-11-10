package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimExemplar;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimExemplarRepository;
import com.projeto.sistemabiblioteca.entities.Exemplar;

import jakarta.transaction.Transactional;

public class DimExemplarService {
	
	private DimExemplarRepository dimExemplarRepository;

	public DimExemplarService(DimExemplarRepository dimExemplarRepository) {
		this.dimExemplarRepository = dimExemplarRepository;
	}
	
	@Transactional
	public void atualizar(Exemplar exemplarReal) {
		Optional<DimExemplar> dimExemplarExistente = dimExemplarRepository.findByIdNatural(exemplarReal.getIdExemplar());
		
		if (dimExemplarExistente.isPresent()) {
			DimExemplar dimExemplar = dimExemplarExistente.get();
			atualizarDados(dimExemplar, exemplarReal);
			dimExemplarRepository.save(dimExemplar);
		}
		else {
			DimExemplar dimNovo = new DimExemplar(exemplarReal);
			dimExemplarRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimExemplar dimExemplar, Exemplar exemplarReal) {
		dimExemplar.setEstadoFisico(exemplarReal.getEstadoFisico());
		dimExemplar.setStatus(exemplarReal.getStatus());
	}
}

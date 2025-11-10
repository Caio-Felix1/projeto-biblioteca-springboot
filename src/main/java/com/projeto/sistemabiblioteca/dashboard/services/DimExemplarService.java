package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimExemplar;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimExemplarRepository;
import com.projeto.sistemabiblioteca.entities.Exemplar;

import jakarta.transaction.Transactional;

@Service
public class DimExemplarService {
	
	private DimExemplarRepository dimExemplarRepository;

	public DimExemplarService(DimExemplarRepository dimExemplarRepository) {
		this.dimExemplarRepository = dimExemplarRepository;
	}
	
	@Transactional
	public DimExemplar atualizar(Exemplar exemplarReal) {
		Optional<DimExemplar> dimExemplarExistente = dimExemplarRepository.findByIdNatural(exemplarReal.getIdExemplar());
		
		if (dimExemplarExistente.isPresent()) {
			DimExemplar dimExemplar = dimExemplarExistente.get();
			atualizarDados(dimExemplar, exemplarReal);
			return dimExemplarRepository.save(dimExemplar);
		}
		else {
			DimExemplar dimNovo = new DimExemplar(exemplarReal);
			return dimExemplarRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimExemplar dimExemplar, Exemplar exemplarReal) {
		dimExemplar.setEstadoFisico(exemplarReal.getEstadoFisico());
		dimExemplar.setStatus(exemplarReal.getStatus());
	}
}

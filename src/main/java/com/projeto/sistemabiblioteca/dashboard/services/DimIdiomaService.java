package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimIdioma;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimIdiomaRepository;
import com.projeto.sistemabiblioteca.entities.Idioma;

import jakarta.transaction.Transactional;

public class DimIdiomaService {
	
	private DimIdiomaRepository dimIdiomaRepository;

	public DimIdiomaService(DimIdiomaRepository dimIdiomaRepository) {
		this.dimIdiomaRepository = dimIdiomaRepository;
	}
	
	@Transactional
	public void atualizar(Idioma idiomaReal) {
		Optional<DimIdioma> dimIdiomaExistente = dimIdiomaRepository.findByIdNatural(idiomaReal.getIdIdioma());
		
		if (dimIdiomaExistente.isPresent()) {
			DimIdioma dimIdioma = dimIdiomaExistente.get();
			atualizarDados(dimIdioma, idiomaReal);
			dimIdiomaRepository.save(dimIdioma);
		}
		else {
			DimIdioma dimNovo = new DimIdioma(idiomaReal);
			dimIdiomaRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimIdioma dimIdioma, Idioma idiomaReal) {
		dimIdioma.setNome(idiomaReal.getNome());
		dimIdioma.setStatus(idiomaReal.getStatusAtivo());
	}
}

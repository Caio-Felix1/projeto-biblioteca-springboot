package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimAutorRepository;
import com.projeto.sistemabiblioteca.entities.Autor;

import jakarta.transaction.Transactional;

@Service
public class DimAutorService {
	
	private DimAutorRepository dimAutorRepository;

	public DimAutorService(DimAutorRepository dimAutorRepository) {
		this.dimAutorRepository = dimAutorRepository;
	}
	
	@Transactional
	public DimAutor atualizar(Autor autorReal) {
		Optional<DimAutor> dimAutorExistente = dimAutorRepository.findByIdNatural(autorReal.getIdAutor());
		
		if (dimAutorExistente.isPresent()) {
			DimAutor dimAutor = dimAutorExistente.get();
			atualizarDados(dimAutor, autorReal);
			return dimAutorRepository.save(dimAutor);
		}
		else {
			DimAutor dimNovo = new DimAutor(autorReal);
			return dimAutorRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimAutor dimAutor, Autor autorReal) {
		dimAutor.setNome(autorReal.getNome());
		dimAutor.setStatus(autorReal.getStatusAtivo());
	}
}

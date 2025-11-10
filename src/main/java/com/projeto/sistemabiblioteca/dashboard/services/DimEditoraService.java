package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEditora;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimEditoraRepository;
import com.projeto.sistemabiblioteca.entities.Editora;

import jakarta.transaction.Transactional;

public class DimEditoraService {
	
	private DimEditoraRepository dimEditoraRepository;

	public DimEditoraService(DimEditoraRepository dimEditoraRepository) {
		this.dimEditoraRepository = dimEditoraRepository;
	}
	
	@Transactional
	public void atualizar(Editora editoraReal) {
		Optional<DimEditora> dimEditoraExistente = dimEditoraRepository.findByIdNatural(editoraReal.getIdEditora());
		
		if (dimEditoraExistente.isPresent()) {
			DimEditora dimEditora = dimEditoraExistente.get();
			atualizarDados(dimEditora, editoraReal);
			dimEditoraRepository.save(dimEditora);
		}
		else {
			DimEditora dimNovo = new DimEditora(editoraReal);
			dimEditoraRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimEditora dimEditora, Editora editoraReal) {
		dimEditora.setNome(editoraReal.getNome());
		dimEditora.setStatus(editoraReal.getStatusAtivo());
	}
}

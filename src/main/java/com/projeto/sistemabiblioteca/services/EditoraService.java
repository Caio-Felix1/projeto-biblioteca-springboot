package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EditoraService {
	
	private EditoraRepository editoraRepository;

	public EditoraService(EditoraRepository editoraRepository) {
		this.editoraRepository = editoraRepository;
	}
	
	public List<Editora> buscarTodos() {
		return editoraRepository.findAll();
	}
	
	public List<Editora> buscarTodosComNomeContendo(String nome) {
		return editoraRepository.findAllByNomeContainingIgnoreCase(nome);
	}
	
	public Editora buscarPorId(Long id) {
		Optional<Editora> editora = editoraRepository.findById(id);
		if (editora.isEmpty()) {
			throw new EntityNotFoundException("Erro: editora com id correspondente não foi encontrada.");
		}
		return editora.get();
	}
	
	public Editora inserir(Editora editora) {
		return editoraRepository.save(editora);
	}
	
	public void deletar(Long id) {
		editoraRepository.deleteById(id);
	}
	
	public Editora atualizar(Long id, Editora editora2) {
		Editora editora1 = buscarPorId(id);
		atualizarDados(editora1, editora2);
		return editoraRepository.save(editora1);
	}
	
	private void atualizarDados(Editora editora1, Editora editora2) {
		editora1.setNome(editora2.getNome());
	}
}

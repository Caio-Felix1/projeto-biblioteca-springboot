package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;

import jakarta.persistence.EntityNotFoundException;

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
	
	public List<Editora> buscarTodosComStatusIgualA(StatusAtivo status) {
		return editoraRepository.findAllByStatusEquals(status);
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
	
	public void inativar(Long id) {
		Editora editora = buscarPorId(id);
		if (editora.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: editora já está inativa.");
		}
		editora.inativar();
		editoraRepository.save(editora);
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

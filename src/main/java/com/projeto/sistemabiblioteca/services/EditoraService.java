package com.projeto.sistemabiblioteca.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.EditoraJaCadastradaException;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EditoraService {
	
	private EditoraRepository editoraRepository;

	public EditoraService(EditoraRepository editoraRepository) {
		this.editoraRepository = editoraRepository;
	}
	
	public Page<Editora> buscarTodos(Pageable pageable) {
		return editoraRepository.findAll(pageable);
	}
	
	public Page<Editora> buscarTodosComNomeContendo(String nome, Pageable pageable) {
		return editoraRepository.findAllByNomeContainingIgnoreCase(nome, pageable);
	}
	
	public Page<Editora> buscarTodosComStatusIgualA(StatusAtivo status, Pageable pageable) {
		return editoraRepository.findAllByStatusEquals(status, pageable);
	}
	
	public Editora buscarPorId(Long id) {
		Optional<Editora> editora = editoraRepository.findById(id);
		if (editora.isEmpty()) {
			throw new EntityNotFoundException("Erro: editora com id correspondente não foi encontrada.");
		}
		return editora.get();
	}
	
	public void verificarSeEditoraJaExiste(String nomeEditora) {
		if (editoraRepository.existsByNome(nomeEditora)) {
			throw new EditoraJaCadastradaException("Erro: editora já foi cadastrada.");
		}
	}
	
	public Editora inserir(Editora editora) {
		verificarSeEditoraJaExiste(editora.getNome());
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
		
		if (!editora1.getNome().equals(editora2.getNome())) {
			verificarSeEditoraJaExiste(editora2.getNome());
		}
		
		atualizarDados(editora1, editora2);
		return editoraRepository.save(editora1);
	}
	
	private void atualizarDados(Editora editora1, Editora editora2) {
		editora1.setNome(editora2.getNome());
	}
}

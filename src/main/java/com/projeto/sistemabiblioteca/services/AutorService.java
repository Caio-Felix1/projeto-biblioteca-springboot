package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.AutorJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AutorService {
	
	private AutorRepository autorRepository;
	
	public AutorService(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}
	
	public Page<Autor> buscarTodos(Pageable pageable) {
		return autorRepository.findAll(pageable);
	}
	
	public Page<Autor> buscarTodosComNomeContendo(String nome, Pageable pageable) {
		return autorRepository.findAllByNomeContainingIgnoreCase(nome, pageable);
	}
	
	public Page<Autor> buscarTodosComStatusIgualA(StatusAtivo status, Pageable pageable) {
		return autorRepository.findAllByStatusEquals(status, pageable);
	}
	
	public List<Autor> buscarTodosPorId(Set<Long> ids) {
		return autorRepository.findAllById(ids);
	}
	
	public Autor buscarPorId(Long id) {
		Optional<Autor> autor = autorRepository.findById(id);
		if (autor.isEmpty()) {
			throw new EntityNotFoundException("Erro: autor com id correspondente não foi encontrado.");
		}
		return autor.get();
	}
	
	public void verificarSeAutorJaExiste(String nomeAutor) {
		if (autorRepository.existsByNome(nomeAutor)) {
			throw new AutorJaCadastradoException("Erro: autor já foi cadastrado.");
		}
	}
	
	public Autor inserir(Autor autor) {
		verificarSeAutorJaExiste(autor.getNome());
		return autorRepository.save(autor);
	}
	
	public void inativar(Long id) {
		Autor autor = buscarPorId(id);
		if (autor.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: autor já está inativo.");
		}
		autor.inativar();
		autorRepository.save(autor);
	}
	
	public Autor atualizar(Long id, Autor autor2) {
		Autor autor1 = buscarPorId(id);
		
		if (!autor1.getNome().equals(autor2.getNome())) {
			verificarSeAutorJaExiste(autor2.getNome());
		}
		
		atualizarDados(autor1, autor2);
		return autorRepository.save(autor1);
	}
	
	private void atualizarDados(Autor autor1, Autor autor2) {
		autor1.setNome(autor2.getNome());
	}
}

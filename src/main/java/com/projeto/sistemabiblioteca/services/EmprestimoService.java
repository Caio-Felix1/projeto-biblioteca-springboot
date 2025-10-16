package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.repositories.EmprestimoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmprestimoService {
	
	private EmprestimoRepository emprestimoRepository;

	public EmprestimoService(EmprestimoRepository emprestimoRepository) {
		this.emprestimoRepository = emprestimoRepository;
	}
	
	public List<Emprestimo> buscarTodos() {
		return emprestimoRepository.findAll();
	}
	
	public List<Emprestimo> buscarTodosComStatusIgualA(StatusEmprestimo statusEmprestimo) {
		return emprestimoRepository.findAllByStatusEquals(statusEmprestimo);
	}
	
	public Emprestimo buscarPorId(Long id) {
		Optional<Emprestimo> emprestimo = emprestimoRepository.findById(id);
		if (emprestimo.isEmpty()) {
			throw new EntityNotFoundException("Erro: emprestimo com id correspondente não foi encontrado.");
		}
		return emprestimo.get();
	}
	
	public Emprestimo inserir(Emprestimo emprestimo) {
		return emprestimoRepository.save(emprestimo);
	}
	
	public Emprestimo atualizar(Long id, Emprestimo emprestimo2) {
		Emprestimo emprestimo1 = buscarPorId(id);
		atualizarDados(emprestimo1, emprestimo2);
		return emprestimoRepository.save(emprestimo1);
	}
	
	private void atualizarDados(Emprestimo emprestimo1, Emprestimo emprestimo2) {
		emprestimo1.setPessoa(emprestimo2.getPessoa());
		emprestimo2.setExemplar(emprestimo2.getExemplar());
	}
	
	
}

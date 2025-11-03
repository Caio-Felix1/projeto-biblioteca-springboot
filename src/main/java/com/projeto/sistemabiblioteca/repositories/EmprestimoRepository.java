package com.projeto.sistemabiblioteca.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
	
	List<Emprestimo> findAllByStatusEquals(StatusEmprestimo status);
	
	List<Emprestimo> findAllByPessoaIdPessoa(Long id);
	
	List<Emprestimo> findAllByStatusIn(Set<StatusEmprestimo> statusSet);
	
	long countByPessoaAndStatusIn(Pessoa pessoa, Set<StatusEmprestimo> statusSet);
	
	boolean existsByPessoaAndMultaStatusPagamento(Pessoa pessoa, StatusPagamento status);
}

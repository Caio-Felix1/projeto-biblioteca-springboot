package com.projeto.sistemabiblioteca.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
	
	Page<Emprestimo> findAllByStatusEquals(StatusEmprestimo status, Pageable pageable);
	
	Page<Emprestimo> findAllByPessoaIdPessoa(Long id, Pageable pageable);
	
	Page<Emprestimo> findAllByPessoaEmailEndereco(String email, Pageable pageable);
	
	Page<Emprestimo> findAllByPessoaCpfValor(String cpf, Pageable pageable);
	
	List<Emprestimo> findAllByStatusIn(Set<StatusEmprestimo> statusSet);
	
	long countByPessoaAndStatusIn(Pessoa pessoa, Set<StatusEmprestimo> statusSet);
	
	boolean existsByPessoaAndMultaStatusPagamento(Pessoa pessoa, StatusPagamento status);
}

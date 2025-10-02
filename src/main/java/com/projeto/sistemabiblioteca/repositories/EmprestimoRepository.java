package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
	
	List<Emprestimo> findAllByStatusEquals(StatusEmprestimo status);
}

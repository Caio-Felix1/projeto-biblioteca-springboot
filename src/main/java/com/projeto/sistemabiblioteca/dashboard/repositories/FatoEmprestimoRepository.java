package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.facts.FatoEmprestimo;

public interface FatoEmprestimoRepository extends JpaRepository<FatoEmprestimo, Long> {
	
	Optional<FatoEmprestimo> findByIdNaturalEmprestimo(Long id);
}

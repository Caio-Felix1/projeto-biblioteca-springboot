package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	Page<Categoria> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);
	
	Page<Categoria> findAllByStatusEquals(StatusAtivo status, Pageable pageable);
	
	boolean existsByNome(String nome);
}

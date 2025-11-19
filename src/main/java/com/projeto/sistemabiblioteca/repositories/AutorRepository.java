package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface AutorRepository extends JpaRepository<Autor, Long> {
	
	Page<Autor> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);
	
	Page<Autor> findAllByStatusEquals(StatusAtivo status, Pageable pageable);
	
	boolean existsByNome(String nome);
}

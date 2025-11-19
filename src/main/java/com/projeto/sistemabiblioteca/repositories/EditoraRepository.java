package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface EditoraRepository extends JpaRepository<Editora, Long> {
	
	Page<Editora> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);
	
	Page<Editora> findAllByStatusEquals(StatusAtivo status, Pageable pageable);
	
	boolean existsByNome(String nome);
}

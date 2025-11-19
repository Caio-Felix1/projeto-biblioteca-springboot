package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface TituloRepository extends JpaRepository<Titulo, Long> {
	
	Page<Titulo> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);
	
	Page<Titulo> findAllByStatusEquals(StatusAtivo status, Pageable pageable);
	
	boolean existsByNome(String nome);
}

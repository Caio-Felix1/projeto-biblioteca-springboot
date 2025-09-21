package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
	
	List<Autor> findAllByNomeContainingIgnoreCase(String nome);
}

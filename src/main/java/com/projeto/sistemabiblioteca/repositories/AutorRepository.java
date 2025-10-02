package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface AutorRepository extends JpaRepository<Autor, Long> {
	
	List<Autor> findAllByNomeContainingIgnoreCase(String nome);
	
	List<Autor> findAllByStatusEquals(StatusAtivo status);
}

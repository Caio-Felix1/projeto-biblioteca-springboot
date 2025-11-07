package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	List<Categoria> findAllByNomeContainingIgnoreCase(String nome);
	
	List<Categoria> findAllByStatusEquals(StatusAtivo status);
	
	boolean existsByNome(String nome);
}

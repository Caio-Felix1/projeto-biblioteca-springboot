package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Titulo;

public interface TituloRepository extends JpaRepository<Titulo, Long> {
	
	List<Titulo> findAllByNomeContainingIgnoreCase(String nome);
}

package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Pais;

public interface PaisRepository extends JpaRepository<Pais, Long> {
	
}

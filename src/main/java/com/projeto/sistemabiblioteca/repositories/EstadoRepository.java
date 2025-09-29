package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	
}

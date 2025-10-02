package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface PaisRepository extends JpaRepository<Pais, Long> {
	
	List<Pais> findAllByStatusEquals(StatusAtivo status);
}

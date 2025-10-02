package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	
	List<Estado> findAllByStatusEquals(StatusAtivo status);
}

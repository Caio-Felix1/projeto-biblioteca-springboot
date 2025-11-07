package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface TituloRepository extends JpaRepository<Titulo, Long> {
	
	List<Titulo> findAllByNomeContainingIgnoreCase(String nome);
	
	List<Titulo> findAllByStatusEquals(StatusAtivo status);
	
	boolean existsByNome(String nome);
}

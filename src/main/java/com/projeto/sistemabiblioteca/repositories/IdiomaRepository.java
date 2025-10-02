package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface IdiomaRepository extends JpaRepository<Idioma, Long> {
	
	List<Idioma> findAllByStatusEquals(StatusAtivo status);
}

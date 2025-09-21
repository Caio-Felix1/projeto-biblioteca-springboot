package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
	
	List<Exemplar> findAllByStatusEquals(StatusExemplar statusExemplar);
}

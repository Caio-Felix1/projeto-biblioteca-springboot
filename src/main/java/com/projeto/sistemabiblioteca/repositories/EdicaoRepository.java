package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface EdicaoRepository extends JpaRepository<Edicao, Long> {
	
	List<Edicao> findAllByStatusEquals(StatusAtivo status);
}

package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface EditoraRepository extends JpaRepository<Editora, Long> {
	
	List<Editora> findAllByNomeContainingIgnoreCase(String nome);
	
	List<Editora> findAllByStatusEquals(StatusAtivo status);
}

package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
	
	List<Endereco> findAllByStatusEquals(StatusAtivo status);
}

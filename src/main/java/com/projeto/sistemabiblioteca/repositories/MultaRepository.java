package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

public interface MultaRepository extends JpaRepository<Multa, Long> {
	
	List<Multa> findAllByStatusPagamentoEquals(StatusPagamento statusPagamento);
}

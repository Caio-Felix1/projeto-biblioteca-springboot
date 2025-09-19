package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.HistoricoEmprestimo;

public interface HistoricoEmprestimoRepository extends JpaRepository<HistoricoEmprestimo, Long> {

}

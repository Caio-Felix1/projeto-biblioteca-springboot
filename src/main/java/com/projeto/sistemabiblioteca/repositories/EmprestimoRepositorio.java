package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Emprestimo;

public interface EmprestimoRepositorio extends JpaRepository<Emprestimo, Long> {

}

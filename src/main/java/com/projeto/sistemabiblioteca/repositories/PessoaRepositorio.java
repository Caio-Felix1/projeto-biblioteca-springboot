package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Pessoa;

public interface PessoaRepositorio extends JpaRepository<Pessoa, Long> {

}

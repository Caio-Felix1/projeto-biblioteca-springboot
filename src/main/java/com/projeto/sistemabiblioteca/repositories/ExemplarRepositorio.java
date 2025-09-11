package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Exemplar;

public interface ExemplarRepositorio extends JpaRepository<Exemplar, Long> {

}

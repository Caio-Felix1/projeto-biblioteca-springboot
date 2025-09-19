package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Exemplar;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {

}

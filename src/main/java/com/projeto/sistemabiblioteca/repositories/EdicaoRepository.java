package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Edicao;

public interface EdicaoRepository extends JpaRepository<Edicao, Long> {

}

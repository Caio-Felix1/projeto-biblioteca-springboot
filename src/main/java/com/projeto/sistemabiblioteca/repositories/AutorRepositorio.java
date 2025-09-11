package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Autor;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {

}

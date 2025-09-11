package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Titulo;

public interface TituloRepositorio extends JpaRepository<Titulo, Long> {

}

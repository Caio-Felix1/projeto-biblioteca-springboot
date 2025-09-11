package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {

}

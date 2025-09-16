package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import org.springframework.security.core.userdetails.UserDetails;

public interface PessoaRepositorio extends JpaRepository<Pessoa, Long> {

     UserDetails findByEmail(String email );


}

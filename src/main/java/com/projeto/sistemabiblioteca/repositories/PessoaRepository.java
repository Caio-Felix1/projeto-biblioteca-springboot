package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

     Pessoa findByEmail(String email);

     Pessoa findByCpf(String cpf);
     
     List<Pessoa> findByFuncaoEquals(FuncaoUsuario funcao);
     
     List<Pessoa> findByStatusContaEquals(StatusConta statusConta);
}

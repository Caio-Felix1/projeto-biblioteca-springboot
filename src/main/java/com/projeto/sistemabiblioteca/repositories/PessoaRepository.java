package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	 boolean existsByEmailEndereco(String email);
	 
	 boolean existsByCpfValor(String cpf);
	
     Pessoa findByEmailEndereco(String email);

     Pessoa findByCpfValor(String cpf);
     
     List<Pessoa> findAllByFuncaoEquals(FuncaoUsuario funcao);
     
     List<Pessoa> findAllByStatusContaEquals(StatusConta statusConta);
}

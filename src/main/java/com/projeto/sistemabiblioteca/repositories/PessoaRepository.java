package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	 boolean existsByEmailEndereco(String email);
	 
	 boolean existsByCpfValor(String cpf);
	
     Pessoa findByEmailEndereco(String email);

     Pessoa findByCpfValor(String cpf);
     
     Page<Pessoa> findAllByFuncaoEquals(FuncaoUsuario funcao, Pageable pageable);
     
     Page<Pessoa> findAllByStatusContaEquals(StatusConta statusConta, Pageable pageable);
}

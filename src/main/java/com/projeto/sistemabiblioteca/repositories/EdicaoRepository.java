package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface EdicaoRepository extends JpaRepository<Edicao, Long> {

	Page<Edicao> findAllByStatusEquals(StatusAtivo status, Pageable pageable);

	Page<Edicao> findAllByTituloNomeContainingIgnoreCaseAndStatus(String nome, StatusAtivo status, Pageable pageable);

	Page<Edicao> findAllByTituloAutoresNomeContainingIgnoreCaseAndStatus(String nome, StatusAtivo status, Pageable pageable);

	Page<Edicao> findAllByTituloCategoriasIdCategoriaAndStatus(Long id, StatusAtivo status, Pageable pageable);
	
	Page<Edicao> findAllByEditoraIdEditoraAndStatus(Long id, StatusAtivo status, Pageable pageable);

	@Query("""
    SELECT edicao
    FROM Edicao edicao
    LEFT JOIN edicao.titulo titulo
    LEFT JOIN edicao.editora editora
    LEFT JOIN edicao.idioma idioma
    LEFT JOIN titulo.autores autores
    WHERE (LOWER(titulo.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
       OR LOWER(autores.nome) LIKE LOWER(CONCAT('%', :termo, '%')))
       AND edicao.status = :status
""")
	Page<Edicao> buscarPorTituloOuAutor(String termo, StatusAtivo status, Pageable pageable);

}

package com.projeto.sistemabiblioteca.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    INNER JOIN edicao.titulo titulo
    INNER JOIN edicao.editora editora
    WHERE (
        LOWER(titulo.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
        OR EXISTS (
            SELECT 1
            FROM titulo.autores autor
            WHERE LOWER(autor.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
        )
        OR EXISTS (
            SELECT 1
            FROM titulo.categorias categoria
            WHERE LOWER(categoria.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
        )
        OR
        LOWER(editora.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
    )
    AND edicao.status = :status
	""")
	Page<Edicao> buscarPorTituloOuAutor(@Param("termo") String termo, @Param("status") StatusAtivo status, Pageable pageable);
}

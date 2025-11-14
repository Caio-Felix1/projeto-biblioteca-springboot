package com.projeto.sistemabiblioteca.repositories;

import java.util.List;

import com.projeto.sistemabiblioteca.DTOs.Response.EdicaoResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EdicaoRepository extends JpaRepository<Edicao, Long> {

	List<Edicao> findAllByStatusEquals(StatusAtivo status);

	List<Edicao> findAllByTituloNomeContainingIgnoreCaseAndStatus(String nome, StatusAtivo status);

	List<Edicao> findAllByTituloAutoresNomeContainingIgnoreCaseAndStatus(String nome, StatusAtivo status);

	List<Edicao> findAllByTituloCategoriasIdCategoriaAndStatus(Long id, StatusAtivo status);
	
	List<Edicao> findAllByEditoraIdEditoraAndStatus(Long id, StatusAtivo status);

	@Query("""
    SELECT new com.projeto.sistemabiblioteca.DTOs.Response.EdicaoResponseDTO(
        edicao.idEdicao,
        CONCAT('', edicao.classificacao),
        edicao.dtPublicacao,
        edicao.qtdPaginas,
        CONCAT('', edicao.tamanho),
        CONCAT('', edicao.tipoCapa),
        edicao.descricaoEdicao,
        edicao.imagemUrl,

        new com.projeto.sistemabiblioteca.DTOs.Response.EditoraResponseDTO(
            editora.idEditora,
            editora.nome,
            CONCAT('', editora.status)
        ),

        new com.projeto.sistemabiblioteca.DTOs.Response.IdiomaResponseDTO(
            idioma.idIdioma,
            idioma.nome,
            CONCAT('', idioma.status)
        ),

        new com.projeto.sistemabiblioteca.DTOs.Response.TituloResponseDTO(
            titulo.idTitulo,
            titulo.descricao,
            titulo.nome,
            CONCAT('', titulo.status)
        ),

        CONCAT('', edicao.status)
    )
    FROM Edicao edicao
    LEFT JOIN edicao.titulo titulo
    LEFT JOIN edicao.editora editora
    LEFT JOIN edicao.idioma idioma
    LEFT JOIN titulo.autores autores
    WHERE LOWER(titulo.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
       OR LOWER(autores.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
""")
	List<EdicaoResponseDTO> buscarPorTituloOuAutor(String termo);

}

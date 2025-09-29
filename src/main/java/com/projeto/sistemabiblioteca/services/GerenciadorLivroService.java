package com.projeto.sistemabiblioteca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.AutorDTO;
import com.projeto.sistemabiblioteca.DTOs.CategoriaDTO;
import com.projeto.sistemabiblioteca.DTOs.EdicaoDTO;
import com.projeto.sistemabiblioteca.DTOs.EditoraDTO;
import com.projeto.sistemabiblioteca.DTOs.IdiomaDTO;
import com.projeto.sistemabiblioteca.DTOs.LivroDTO;
import com.projeto.sistemabiblioteca.DTOs.TituloDTO;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.Titulo;

import jakarta.transaction.Transactional;

@Service
public class GerenciadorLivroService {
	
	private ExemplarService exemplarService;
	
	private EdicaoService edicaoService;
	
	private TituloService tituloService;
	
	private EditoraService editoraService;
	
	private IdiomaService idiomaService;
	
	private AutorService autorService;
	
	private CategoriaService categoriaService;
	
	public GerenciadorLivroService(ExemplarService exemplarService, EdicaoService edicaoService,
			TituloService tituloService, EditoraService editoraService, IdiomaService idiomaService,
			AutorService autorService, CategoriaService categoriaService) {
		this.exemplarService = exemplarService;
		this.edicaoService = edicaoService;
		this.tituloService = tituloService;
		this.editoraService = editoraService;
		this.idiomaService = idiomaService;
		this.autorService = autorService;
		this.categoriaService = categoriaService;
	}



	@Transactional
	public Exemplar cadastrarLivro(LivroDTO livroDTO) {
		EdicaoDTO edicaoDTO = livroDTO.edicao();
		TituloDTO tituloDTO = edicaoDTO.titulo();
		EditoraDTO editoraDTO = edicaoDTO.editora();
		IdiomaDTO idiomaDTO = edicaoDTO.idioma();
		
		Edicao edicao;
		Titulo titulo;
		Editora editora;
		Idioma idioma;
		
		if (livroDTO.estadoFisico() == null) {
			throw new IllegalArgumentException("Erro: estado físico do livro não foi informado.");
		}
		
		if (edicaoDTO.idEdicao() != null) {
			edicao = edicaoService.buscarPorId(edicaoDTO.idEdicao());
			return exemplarService.inserir(new Exemplar(livroDTO.estadoFisico(), edicao));
		}
		
		if (tituloDTO.idTitulo() != null) {
			titulo = tituloService.buscarPorId(tituloDTO.idTitulo());
		}
		else {
			List<CategoriaDTO> categorias = tituloDTO.categorias();
			List<AutorDTO> autores = tituloDTO.autores();
			titulo = new Titulo(tituloDTO.nome(), tituloDTO.descricao());
			
			if (autores.size() == 0) {
				throw new IllegalArgumentException("Erro: título deve ter pelo menos um autor.");
			}
			if (categorias.size() == 0) {
				throw new IllegalArgumentException("Erro: título deve ter pelo menos uma categoria.");
			}
			
			for (AutorDTO autorDTO : autores) {
				Autor autor = (autorDTO.idAutor() != null) ? autorService.buscarPorId(autorDTO.idAutor()) : autorService.inserir(new Autor(autorDTO.nome()));
				titulo.adicionarAutor(autor);
			}
			
			for (CategoriaDTO categoriaDTO : categorias) {
				Categoria categoria = (categoriaDTO.idCategoria() != null) ? categoriaService.buscarPorId(categoriaDTO.idCategoria()) : categoriaService.inserir(new Categoria(categoriaDTO.nome()));
				titulo.adicionarCategoria(categoria);
			}
			
			tituloService.inserir(titulo);
		}
		
		editora = (editoraDTO.idEditora() != null) ? editoraService.buscarPorId(editoraDTO.idEditora()) : editoraService.inserir(new Editora(editoraDTO.nome()));
		
		idioma = (idiomaDTO.idIdioma() != null) ? idiomaService.buscarPorId(idiomaDTO.idIdioma()) : idiomaService.inserir(new Idioma(idiomaDTO.nome()));
		
		edicao = edicaoService.inserir(new Edicao(edicaoDTO.tipoCapa(), edicaoDTO.qtdPaginas(), edicaoDTO.tamanhoEdicao(), edicaoDTO.classificacao(), edicaoDTO.dtPublicacao(), titulo, editora, idioma));
		
		return exemplarService.inserir(new Exemplar(livroDTO.estadoFisico(), edicao));
	}
}

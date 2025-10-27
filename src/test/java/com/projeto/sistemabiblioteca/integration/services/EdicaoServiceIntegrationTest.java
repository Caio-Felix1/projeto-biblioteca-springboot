package com.projeto.sistemabiblioteca.integration.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.DTOs.EdicaoDTO;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;
import com.projeto.sistemabiblioteca.services.EdicaoService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class EdicaoServiceIntegrationTest {
	
	@Autowired
	private EdicaoService edicaoService;
	
	@Autowired
	private TituloRepository tituloRepository;
	
	@Autowired
	private EditoraRepository editoraRepository;
	
	@Autowired
	private IdiomaRepository idiomaRepository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	private Edicao criarEdicao(Titulo titulo, Editora editora, Idioma idioma) {
		return new Edicao(
				TipoCapa.DURA,
				100,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2025, 10, 10),
				titulo,
				editora,
				idioma);
	}
	
	@Test
	void buscarTodosFiltrandoPeloNomeDoAutor() {
		Autor autor1 = new Autor("Tolkien Teste 1");
		Autor autor2 = new Autor("Tolkien Teste 2");
		Autor autor3 = new Autor("Martin Teste 1");
		
		autorRepository.save(autor1);
		autorRepository.save(autor2);
		autorRepository.save(autor3);
		
		Titulo titulo1 = new Titulo("Anel", "Meu precioso");
		titulo1.adicionarAutor(autor1);
		
		Titulo titulo2 = new Titulo("Anel 2", "Meu precioso 2");
		titulo2.adicionarAutor(autor2);
		
		Titulo titulo3 = new Titulo("Tronos", "Jogo");
		titulo3.adicionarAutor(autor3);
		
		tituloRepository.save(titulo1);
		tituloRepository.save(titulo2);
		tituloRepository.save(titulo3);
		
		Edicao edicao1 = criarEdicao(titulo1, null, null);
		Edicao edicao2 = criarEdicao(titulo2, null, null);
		Edicao edicao3 = criarEdicao(titulo3, null, null);
		
		edicaoService.inserir(edicao1);
		edicaoService.inserir(edicao2);
		edicaoService.inserir(edicao3);
		
		List<Edicao> edicoes = edicaoService.buscarTodosComAutorComNomeContendo("tolkien");
		
		Assertions.assertEquals(2, edicoes.size());
		Assertions.assertTrue(edicoes.stream().allMatch(e -> e.getTitulo().getNome().contains("Anel")));
		for (Edicao e : edicoes) {
			Set<Autor> autores = e.getTitulo().getAutores();
			Assertions.assertTrue(autores.stream().allMatch(a -> a.getNome().contains("Tolkien")));
		}
	}
	
	@Test
	void deveBuscarTodosFiltrandoPeloNomeDoTitulo() {
		Titulo titulo1 = new Titulo("Anel", "Meu precioso");
		Titulo titulo2 = new Titulo("Anel 2", "Meu precioso 2");
		Titulo titulo3 = new Titulo("Tronos", "Jogo");
		
		tituloRepository.save(titulo1);
		tituloRepository.save(titulo2);
		tituloRepository.save(titulo3);
		
		Edicao edicao1 = criarEdicao(titulo1, null, null);
		Edicao edicao2 = criarEdicao(titulo2, null, null);
		Edicao edicao3 = criarEdicao(titulo3, null, null);
		
		edicaoService.inserir(edicao1);
		edicaoService.inserir(edicao2);
		edicaoService.inserir(edicao3);
		
		List<Edicao> edicoes = edicaoService.buscarTodosComTituloComNomeContendo("tronos");
		
		Assertions.assertEquals(1, edicoes.size());
		Assertions.assertTrue(edicoes.get(0).getTitulo().getNome().equals("Tronos"));
	}
	
	@Test
	void buscarTodosFiltrandoPeloIdDaCategoria() {
		Categoria categoria1 = new Categoria("Aventura");
		Categoria categoria2 = new Categoria("Drama");
		
		categoriaRepository.save(categoria1);
		categoriaRepository.save(categoria2);
		
		Titulo titulo1 = new Titulo("Anel", "Meu precioso");
		titulo1.adicionarCategoria(categoria1);
		
		Titulo titulo2 = new Titulo("Anel 2", "Meu precioso 2");
		titulo2.adicionarCategoria(categoria1);
		
		Titulo titulo3 = new Titulo("Tronos", "Jogo");
		titulo3.adicionarCategoria(categoria2);
		
		tituloRepository.save(titulo1);
		tituloRepository.save(titulo2);
		tituloRepository.save(titulo3);
		
		Edicao edicao1 = criarEdicao(titulo1, null, null);
		Edicao edicao2 = criarEdicao(titulo2, null, null);
		Edicao edicao3 = criarEdicao(titulo3, null, null);
		
		edicaoService.inserir(edicao1);
		edicaoService.inserir(edicao2);
		edicaoService.inserir(edicao3);
		
		List<Edicao> edicoes = edicaoService.buscarTodosComCategoriaComIdIgualA(categoria1.getIdCategoria());
		
		Assertions.assertEquals(2, edicoes.size());
		for (Edicao e : edicoes) {
			Set<Categoria> categorias = e.getTitulo().getCategorias();
			Assertions.assertTrue(categorias.stream().allMatch(c -> c.getNome().equals("Aventura")));
		}
	}
	
	@Test
	void deveCadastrarEdicao() {
		Titulo titulo = new Titulo("Anel", "Meu precioso");
		
		tituloRepository.save(titulo);
		
		Editora editora = new Editora("Editora teste");
		
		editoraRepository.save(editora);
		
		Idioma idioma = new Idioma("Português");
		
		idiomaRepository.save(idioma);
		
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				TipoCapa.DURA,
				100,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2025, 10, 10),
				titulo.getIdTitulo(),
				editora.getIdEditora(),
				idioma.getIdIdioma());
		
		Edicao edicaoNova = edicaoService.cadastrarEdicao(edicaoDTO);
		
		Assertions.assertEquals(TipoCapa.DURA, edicaoNova.getTipoCapa());
		Assertions.assertEquals(100, edicaoNova.getQtdPaginas());
		Assertions.assertEquals(TamanhoEdicao.MEDIO, edicaoNova.getTamanho());
		Assertions.assertEquals(ClassificacaoIndicativa.C14, edicaoNova.getClassificacao());
		Assertions.assertEquals(LocalDate.of(2025, 10, 10), edicaoNova.getDtPublicacao());
		Assertions.assertEquals("Anel", edicaoNova.getTitulo().getNome());
		Assertions.assertEquals("Editora teste", edicaoNova.getEditora().getNome());
		Assertions.assertEquals("Português", edicaoNova.getIdioma().getNome());
	}
	
	@Test
	void deveAtualizarEdicao() {
		Titulo titulo1 = new Titulo("Anel", "Meu precioso");
		Titulo titulo2 = new Titulo("Tronos", "Jogo");
		
		tituloRepository.save(titulo1);
		tituloRepository.save(titulo2);
		
		Editora editora1 = new Editora("Editora teste");
		Editora editora2 = new Editora("Editora alterado");
		
		editoraRepository.save(editora1);
		editoraRepository.save(editora2);
		
		Idioma idioma1 = new Idioma("Português");
		Idioma idioma2 = new Idioma("Inglês");
		
		idiomaRepository.save(idioma1);
		idiomaRepository.save(idioma2);
		
		Edicao edicao = criarEdicao(titulo1, editora1, idioma1);
		
		edicaoService.inserir(edicao);
		
		EdicaoDTO edicaoDTO = new EdicaoDTO(
				TipoCapa.MOLE,
				2000,
				TamanhoEdicao.GRANDE,
				ClassificacaoIndicativa.C18,
				LocalDate.of(2024, 9, 9),
				titulo2.getIdTitulo(),
				editora2.getIdEditora(),
				idioma2.getIdIdioma());
		
		Edicao edicaoAtualizada = edicaoService.atualizar(edicao.getIdEdicao(), edicaoDTO);
		
		Assertions.assertEquals(TipoCapa.MOLE, edicaoAtualizada.getTipoCapa());
		Assertions.assertEquals(2000, edicaoAtualizada.getQtdPaginas());
		Assertions.assertEquals(TamanhoEdicao.GRANDE, edicaoAtualizada.getTamanho());
		Assertions.assertEquals(ClassificacaoIndicativa.C18, edicaoAtualizada.getClassificacao());
		Assertions.assertEquals(LocalDate.of(2024, 9, 9), edicaoAtualizada.getDtPublicacao());
		Assertions.assertEquals("Tronos", edicaoAtualizada.getTitulo().getNome());
		Assertions.assertEquals("Editora alterado", edicaoAtualizada.getEditora().getNome());
		Assertions.assertEquals("Inglês", edicaoAtualizada.getIdioma().getNome());
	}
}

package com.projeto.sistemabiblioteca.integration.services;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.DTOs.TituloCreateDTO;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;
import com.projeto.sistemabiblioteca.services.TituloService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class TituloServiceIntegrationTest {
	
	@Autowired
	private TituloService tituloService;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Test
	void deveBuscarTodosFiltrandoPeloNome() {
		Titulo titulo1 = new Titulo("Titulo 1", "Descricao 1");
		Titulo titulo2 = new Titulo("Titulo 2", "Descricao 2");
		
		tituloService.inserir(titulo1);
		tituloService.inserir(titulo2);
		
		List<Titulo> titulos = tituloService.buscarTodosComNomeContendo("Titulo 2");
		
		Assertions.assertEquals(1, titulos.size());
		Assertions.assertEquals("Titulo 2", titulos.get(0).getNome());
	}
	
	@Test
	void deveCadastrarTitulo() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		Long autor1Id = autorRepository.save(autor1).getIdAutor();
		Long autor2Id = autorRepository.save(autor2).getIdAutor();
		
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		Long categoria1Id = categoriaRepository.save(categoria1).getIdCategoria();
		Long categoria2Id = categoriaRepository.save(categoria2).getIdCategoria();
		
		TituloCreateDTO tituloCreateDTO = new TituloCreateDTO(
				"Titulo 1", 
				"Descricao 1",
				Set.of(categoria1Id, categoria2Id),
				Set.of(autor1Id, autor2Id));
		
		Titulo titulo = tituloService.cadastrarTitulo(tituloCreateDTO);
		
		Assertions.assertEquals("Titulo 1", titulo.getNome());
		Assertions.assertEquals("Descricao 1", titulo.getDescricao());
		
		Set<Categoria> categorias = titulo.getCategorias();
		
		Assertions.assertEquals(2, categorias.size());
		Assertions.assertTrue(categorias.stream().anyMatch(c -> c.getNome().equals("Categoria 1") || c.getNome().equals("Categoria 2")));
		
		Set<Autor> autores = titulo.getAutores();
		
		Assertions.assertEquals(2, autores.size());
		Assertions.assertTrue(autores.stream().allMatch(a -> a.getNome().equals("Autor 1") || a.getNome().equals("Autor 2")));
	}
	
	@Test
	void deveAtualizarTituloCompleto() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		Autor autor3 = new Autor("Autor 3");
		Autor autor4 = new Autor("Autor 4");
		
		Long autor1Id = autorRepository.save(autor1).getIdAutor();
		Long autor2Id = autorRepository.save(autor2).getIdAutor();
		Long autor3Id = autorRepository.save(autor3).getIdAutor();
		Long autor4Id = autorRepository.save(autor4).getIdAutor();
		
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		Categoria categoria3 = new Categoria("Categoria 3");
		Categoria categoria4 = new Categoria("Categoria 4");
		
		Long categoria1Id = categoriaRepository.save(categoria1).getIdCategoria();
		Long categoria2Id = categoriaRepository.save(categoria2).getIdCategoria();
		Long categoria3Id = categoriaRepository.save(categoria3).getIdCategoria();
		Long categoria4Id = categoriaRepository.save(categoria4).getIdCategoria();
		
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		List.of(autor1, autor2).forEach(titulo::adicionarAutor);
		List.of(categoria1, categoria2).forEach(titulo::adicionarCategoria);
		
		tituloService.inserir(titulo);
		
		TituloCreateDTO tituloCreateDTO = new TituloCreateDTO(
				"Titulo 2", 
				"Descricao 2",
				Set.of(categoria3Id, categoria4Id),
				Set.of(autor3Id, autor4Id));
		
		Titulo tituloAtualizado = tituloService.atualizarTituloCompleto(titulo.getIdTitulo(), tituloCreateDTO);
		
		Assertions.assertEquals("Titulo 2", tituloAtualizado.getNome());
		Assertions.assertEquals("Descricao 2", tituloAtualizado.getDescricao());
		
		Set<Categoria> categorias = tituloAtualizado.getCategorias();
		
		Assertions.assertEquals(2, categorias.size());
		Assertions.assertTrue(categorias.stream().anyMatch(c -> c.getNome().equals("Categoria 3") || c.getNome().equals("Categoria 4")));
		
		Set<Autor> autores = tituloAtualizado.getAutores();
		
		Assertions.assertEquals(2, autores.size());
		Assertions.assertTrue(autores.stream().allMatch(a -> a.getNome().equals("Autor 3") || a.getNome().equals("Autor 4")));
	}
	
	@Test
	void deveAdicionarUmaCategoria() {
		Categoria categoria = new Categoria("Categoria 1");
		
		Long categoriaId = categoriaRepository.save(categoria).getIdCategoria();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.adicionarCategorias(tituloId, Set.of(categoriaId));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(1, tituloAtualizado.getCategorias().size());
		Assertions.assertTrue(tituloAtualizado.getCategorias().stream().anyMatch(c -> c.getNome().equals("Categoria 1")));
	}
	
	@Test
	void deveAdicionarVariasCategorias() {
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		Long categoria1Id = categoriaRepository.save(categoria1).getIdCategoria();
		Long categoria2Id = categoriaRepository.save(categoria2).getIdCategoria();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.adicionarCategorias(tituloId, Set.of(categoria1Id, categoria2Id));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(2, tituloAtualizado.getCategorias().size());
		Assertions.assertTrue(tituloAtualizado.getCategorias().stream().anyMatch(c -> c.getNome().equals("Categoria 1")));
		Assertions.assertTrue(tituloAtualizado.getCategorias().stream().anyMatch(c -> c.getNome().equals("Categoria 2")));
	}
	
	@Test
	void deveRemoverUmaCategoria() {
		Categoria categoria = new Categoria("Categoria 1");
		
		Long categoriaId = categoriaRepository.save(categoria).getIdCategoria();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		titulo.adicionarCategoria(categoria);
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.removerCategorias(tituloId, Set.of(categoriaId));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(0, tituloAtualizado.getCategorias().size());
	}
	
	@Test
	void deveRemoverVariasCategorias() {
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		Long categoria1Id = categoriaRepository.save(categoria1).getIdCategoria();
		Long categoria2Id = categoriaRepository.save(categoria2).getIdCategoria();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		titulo.adicionarCategoria(categoria1);
		titulo.adicionarCategoria(categoria2);
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.removerCategorias(tituloId, Set.of(categoria1Id, categoria2Id));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(0, tituloAtualizado.getCategorias().size());
	}
	
	@Test
	void deveAdicionarUmAutor() {
		Autor autor = new Autor("Autor 1");
		
		Long autorId = autorRepository.save(autor).getIdAutor();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.adicionarAutores(tituloId, Set.of(autorId));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(1, tituloAtualizado.getAutores().size());
		Assertions.assertTrue(tituloAtualizado.getAutores().stream().anyMatch(a -> a.getNome().equals("Autor 1")));
	}
	
	@Test
	void deveAdicionarVariosAutores() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		Long autor1Id = autorRepository.save(autor1).getIdAutor();
		Long autor2Id = autorRepository.save(autor2).getIdAutor();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.adicionarAutores(tituloId, Set.of(autor1Id, autor2Id));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(2, tituloAtualizado.getAutores().size());
		Assertions.assertTrue(tituloAtualizado.getAutores().stream().anyMatch(a -> a.getNome().equals("Autor 1")));
		Assertions.assertTrue(tituloAtualizado.getAutores().stream().anyMatch(a -> a.getNome().equals("Autor 2")));
	}
	
	@Test
	void deveRemoverUmAutor() {
		Autor autor = new Autor("Autor 1");
		
		Long autorId = autorRepository.save(autor).getIdAutor();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		titulo.adicionarAutor(autor);
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.removerAutores(tituloId, Set.of(autorId));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(0, tituloAtualizado.getAutores().size());
	}
	
	@Test
	void deveRemoverVariosAutores() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		Long autor1Id = autorRepository.save(autor1).getIdAutor();
		Long autor2Id = autorRepository.save(autor2).getIdAutor();
		
		Titulo titulo = new Titulo("Titulo 1", "Descricao 1");
		titulo.adicionarAutor(autor1);
		titulo.adicionarAutor(autor2);
		
		Long tituloId = tituloService.inserir(titulo).getIdTitulo();
		
		tituloService.removerAutores(tituloId, Set.of(autor1Id, autor2Id));
		
		Titulo tituloAtualizado = tituloService.buscarPorId(tituloId);
		
		Assertions.assertEquals(0, tituloAtualizado.getAutores().size());
	}
}

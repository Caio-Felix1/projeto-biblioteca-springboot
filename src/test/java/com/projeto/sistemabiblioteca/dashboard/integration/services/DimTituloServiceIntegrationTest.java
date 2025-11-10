package com.projeto.sistemabiblioteca.dashboard.integration.services;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimTitulo;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimAutorRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimCategoriaRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimTituloRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimTituloService;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimTituloServiceIntegrationTest {
	
	@Autowired
	private DimTituloService dimTituloService;
	
	@Autowired
	private DimTituloRepository dimTituloRepository;
	
	@Autowired
	private TituloRepository TituloRepository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private DimAutorRepository dimAutorRepository;
	
	@Autowired
	private DimCategoriaRepository dimCategoriaRepository;
	
	@Test
	void deveCriarUmNovoDimTitulo() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		autorRepository.save(autor1);
		autorRepository.save(autor2);
		
		DimAutor dimAutor1 = new DimAutor(autor1);
		DimAutor dimAutor2 = new DimAutor(autor2);
		
		dimAutorRepository.save(dimAutor1);
		dimAutorRepository.save(dimAutor2);
		
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		categoriaRepository.save(categoria1);
		categoriaRepository.save(categoria2);
		
		DimCategoria dimCategoria1 = new DimCategoria(categoria1);
		DimCategoria dimCategoria2 = new DimCategoria(categoria2);
		
		dimCategoriaRepository.save(dimCategoria1);
		dimCategoriaRepository.save(dimCategoria2);
		
		Titulo titulo = new Titulo("Titulo 1", null);
		
		TituloRepository.save(titulo);
		
		DimTitulo dimTituloNovo = dimTituloService.atualizar(titulo, Set.of(dimCategoria1, dimCategoria2), Set.of(dimAutor1, dimAutor2));
		
		Assertions.assertTrue(dimTituloNovo.getSurrogateKey() != null && dimTituloNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(titulo.getIdTitulo(), dimTituloNovo.getIdNatural());
		Assertions.assertEquals(titulo.getNome(), dimTituloNovo.getNome());
		Assertions.assertEquals(titulo.getStatusAtivo(), dimTituloNovo.getStatus());
		
		Assertions.assertEquals(2, dimTituloNovo.getAutores().size());
		Assertions.assertTrue(dimTituloNovo.getAutores().stream().allMatch(a -> List.of("Autor 1", "Autor 2").contains(a.getNome())));
		
		Assertions.assertEquals(2, dimTituloNovo.getCategorias().size());
		Assertions.assertTrue(dimTituloNovo.getCategorias().stream().allMatch(c -> List.of("Categoria 1", "Categoria 2").contains(c.getNome())));
	}
	
	@Test
	void deveAtualizarDimTituloJaExistente() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		autorRepository.save(autor1);
		autorRepository.save(autor2);
		
		DimAutor dimAutor1 = new DimAutor(autor1);
		DimAutor dimAutor2 = new DimAutor(autor2);
		
		dimAutorRepository.save(dimAutor1);
		dimAutorRepository.save(dimAutor2);
		
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		categoriaRepository.save(categoria1);
		categoriaRepository.save(categoria2);
		
		DimCategoria dimCategoria1 = new DimCategoria(categoria1);
		DimCategoria dimCategoria2 = new DimCategoria(categoria2);
		
		dimCategoriaRepository.save(dimCategoria1);
		dimCategoriaRepository.save(dimCategoria2);
		
		Titulo titulo = new Titulo("Titulo 1", null);
		
		TituloRepository.save(titulo);
		
		DimTitulo dimTitulo = new DimTitulo(titulo, Set.of(dimCategoria1, dimCategoria2), Set.of(dimAutor1, dimAutor2));
		
		dimTituloRepository.save(dimTitulo);
		
		titulo.setNome("Titulo 2");
		titulo.inativar();
		
		autor1.setNome("Autor 3");
		autor1.inativar();
		
		dimAutor1.setNome(autor1.getNome());
		dimAutor1.setStatus(autor1.getStatusAtivo());
		
		categoria1.setNome("Categoria 3");
		categoria1.inativar();
		
		dimCategoria1.setNome(categoria1.getNome());
		dimCategoria1.setStatus(categoria1.getStatusAtivo());
		
		DimTitulo dimTituloAtualizado = dimTituloService.atualizar(titulo, Set.of(dimCategoria1), Set.of(dimAutor1));
		
		Assertions.assertEquals(dimTitulo.getSurrogateKey(), dimTituloAtualizado.getSurrogateKey());
		Assertions.assertEquals(titulo.getIdTitulo(), dimTituloAtualizado.getIdNatural());
		Assertions.assertEquals(titulo.getNome(), dimTituloAtualizado.getNome());
		Assertions.assertEquals(titulo.getStatusAtivo(), dimTituloAtualizado.getStatus());
		
		Assertions.assertEquals(1, dimTituloAtualizado.getAutores().size());
		Assertions.assertTrue(dimTituloAtualizado.getAutores().stream().allMatch(
				a -> a.getIdNatural().equals(autor1.getIdAutor()) &&
				a.getNome().equals(autor1.getNome()) &&
				a.getStatus().equals(autor1.getStatusAtivo())
				));
		
		Assertions.assertEquals(1, dimTituloAtualizado.getCategorias().size());
		Assertions.assertTrue(dimTituloAtualizado.getCategorias().stream().allMatch(
				c -> c.getIdNatural().equals(categoria1.getIdCategoria()) &&
				c.getNome().equals(categoria1.getNome()) &&
				c.getStatus().equals(categoria1.getStatusAtivo())
				));
	}
}

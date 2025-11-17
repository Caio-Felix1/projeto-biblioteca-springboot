package com.projeto.sistemabiblioteca.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.sistemabiblioteca.DTOs.TituloCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.TituloUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.AutorJaCadastradoException;
import com.projeto.sistemabiblioteca.exceptions.TituloJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;
import com.projeto.sistemabiblioteca.services.AutorService;
import com.projeto.sistemabiblioteca.services.CategoriaService;
import com.projeto.sistemabiblioteca.services.TituloService;

@ExtendWith(MockitoExtension.class)
public class TituloServiceTest {
	
	@Mock
	private TituloRepository tituloRepository;
	
	@Mock
	private CategoriaService categoriaService;
	
	@Mock
	private AutorService autorService;
	
	@InjectMocks
	private TituloService tituloService;
	
	@Test
	void deveCadastrarTitulo() {
		List<Autor> autores = List.of(new Autor("Autor 1"), new Autor("Autor 2"));
		List<Categoria> categorias = List.of(new Categoria("Categoria 1"), new Categoria("Categoria 2"));
		
		TituloCreateDTO tituloCreateDTO = new TituloCreateDTO(
				"Título 1",
				"Descrição 1",
				Set.of(1L, 2L),
				Set.of(1L, 2L));
		
		when(autorService.buscarTodosPorId(anySet())).thenReturn(autores);
		when(categoriaService.buscarTodosPorId(anySet())).thenReturn(categorias);
		when(tituloRepository.existsByNome(any(String.class))).thenReturn(false);
		when(tituloRepository.save(any(Titulo.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Titulo titulo = tituloService.cadastrarTitulo(tituloCreateDTO);
		
		verify(autorService).buscarTodosPorId(anySet());
		verify(categoriaService).buscarTodosPorId(anySet());
		verify(tituloRepository).existsByNome(any(String.class));
		verify(tituloRepository).save(argThat(t ->
		t.getNome().equals("Título 1") &&
		t.getDescricao().equals("Descrição 1") &&
		t.getStatusAtivo() == StatusAtivo.ATIVO &&
		t.getAutores().size() == 2 &&
		t.getCategorias().size() == 2));
		
		Assertions.assertTrue(titulo.getAutores().stream().allMatch(a -> a.getNome().equals("Autor 1") || a.getNome().equals("Autor 2")));
		Assertions.assertTrue(titulo.getCategorias().stream().allMatch(c -> c.getNome().equals("Categoria 1") || c.getNome().equals("Categoria 2")));
	}
	
	@Test
	void deveLancarExcecaoAoCadastrarTituloComAutorInativo() {
		Autor autorInativo = new Autor("Autor 2");
		autorInativo.inativar();
		
		List<Autor> autores = List.of(new Autor("Autor 1"), autorInativo);
		
		TituloCreateDTO tituloCreateDTO = new TituloCreateDTO(
				"Título 1",
				"Descrição 1",
				Set.of(1L, 2L),
				Set.of(1L));
		
		when(autorService.buscarTodosPorId(anySet())).thenReturn(autores);
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> tituloService.cadastrarTitulo(tituloCreateDTO),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar título com um autor inativo");
	}
	
	@Test
	void deveLancarExcecaoAoCadastrarTituloComCategoriaInativa() {
		List<Autor> autores = List.of(new Autor("Autor 1"), new Autor("Autor 2"));
		
		Categoria categoriaInativa = new Categoria("Categoria 2");
		categoriaInativa.inativar();
		
		List<Categoria> categorias = List.of(new Categoria("Categoria 1"), categoriaInativa);
		
		TituloCreateDTO tituloCreateDTO = new TituloCreateDTO(
				"Título 1",
				"Descrição 1",
				Set.of(1L, 2L),
				Set.of(1L, 2L));
		
		when(autorService.buscarTodosPorId(anySet())).thenReturn(autores);
		when(categoriaService.buscarTodosPorId(anySet())).thenReturn(categorias);
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> tituloService.cadastrarTitulo(tituloCreateDTO),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar título com uma categoria inativa");
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarTituloJaExistente() {
		List<Autor> autores = List.of(new Autor("Autor 1"), new Autor("Autor 2"));
		List<Categoria> categorias = List.of(new Categoria("Categoria 1"), new Categoria("Categoria 2"));
		
		TituloCreateDTO tituloCreateDTO = new TituloCreateDTO(
				"Título 1",
				"Descrição 1",
				Set.of(1L, 2L),
				Set.of(1L, 2L));
		
		when(autorService.buscarTodosPorId(anySet())).thenReturn(autores);
		when(categoriaService.buscarTodosPorId(anySet())).thenReturn(categorias);
		when(tituloRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(TituloJaCadastradoException.class,
				() -> tituloService.cadastrarTitulo(tituloCreateDTO),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar um título já existente no banco de dados");
	}
	
	@Test
	void deveAtualizarTitulo() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				"Título 2", "Descrição 2");
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(tituloRepository.existsByNome(any(String.class))).thenReturn(false);
		
		tituloService.atualizar(1L, tituloUpdateDTO);
		
		verify(tituloRepository).findById(any(Long.class));
		verify(tituloRepository).existsByNome(any(String.class));
		verify(tituloRepository).save(argThat(t ->
		t.getNome().equals("Título 2") &&
		t.getDescricao().equals("Descrição 2") &&
		t.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveAtualizarAutorComMesmoNome() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				"Título 1", "Descrição 2");
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		
		tituloService.atualizar(1L, tituloUpdateDTO);
		
		verify(tituloRepository).findById(any(Long.class));
		verify(tituloRepository).save(argThat(t ->
		t.getNome().equals("Título 1") &&
		t.getDescricao().equals("Descrição 2") &&
		t.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarAutorComUmNomeJaExistente() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		TituloUpdateDTO tituloUpdateDTO = new TituloUpdateDTO(
				"Título 2", "Descrição 2");
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(tituloRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(TituloJaCadastradoException.class,
				() -> tituloService.atualizar(1L, tituloUpdateDTO),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar um título com um nome já existente no banco de dados");
	}
	
	@Test
	void deveAtualizarTituloCompleto() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		List.of(new Autor("Autor 1"), new Autor("Autor 2")).forEach(titulo::adicionarAutor);
		List.of(new Categoria("Categoria 1"), new Categoria("Categoria 2")).forEach(titulo::adicionarCategoria);
		
		TituloCreateDTO tituloCreateDTO = new TituloCreateDTO(
				"Título 2",
				"Descrição 2",
				Set.of(3L),
				Set.of(3L));
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(tituloRepository.existsByNome(any(String.class))).thenReturn(false);
		when(autorService.buscarTodosPorId(any())).thenReturn(List.of(new Autor("Autor 3")));
		when(categoriaService.buscarTodosPorId(any())).thenReturn(List.of(new Categoria("Categoria 3")));
		when(tituloRepository.save(any(Titulo.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Titulo tituloAtualizado = tituloService.atualizarTituloCompleto(1L, tituloCreateDTO);
		
		verify(tituloRepository).findById(any(Long.class));
		verify(tituloRepository).existsByNome(any(String.class));
		verify(autorService).buscarTodosPorId(any());
		verify(categoriaService).buscarTodosPorId(any());		
		verify(tituloRepository).save(argThat(t ->
		t.getNome().equals("Título 2") &&
		t.getDescricao().equals("Descrição 2") &&
		t.getStatusAtivo() == StatusAtivo.ATIVO &&
		t.getAutores().size() == 1 &&
		t.getCategorias().size() == 1));
		
		Assertions.assertTrue(tituloAtualizado.getAutores().stream().allMatch(a -> a.getNome().equals("Autor 3")));
		Assertions.assertTrue(tituloAtualizado.getCategorias().stream().allMatch(c -> c.getNome().equals("Categoria 3")));
	}
	
	@Test
	void deveAdicionarCategoriasNoTitulo() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		List<Categoria> categorias = List.of(new Categoria("Categoria 1"), new Categoria("Categoria 2"));
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(categoriaService.buscarTodosPorId(anySet())).thenReturn(categorias);
		
		tituloService.adicionarCategorias(1L, Set.of(1L, 2L));
		
		Assertions.assertEquals(2, titulo.getCategorias().size());
		Assertions.assertTrue(titulo.getCategorias().stream().allMatch(c -> c.getNome().equals("Categoria 1") || c.getNome().equals("Categoria 2")));
	}
	
	@Test
	void deveLancarExcecaoAoAdicionarCategoriasNoTituloComCategoriaInativA() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		Categoria categoriaInativa = new Categoria("Categoria 2");
		categoriaInativa.inativar();
		
		List<Categoria> categorias = List.of(new Categoria("Categoria 1"), categoriaInativa);
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(categoriaService.buscarTodosPorId(anySet())).thenReturn(categorias);
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> tituloService.adicionarCategorias(1L, Set.of(1L, 2L)),
				"Era esperado que fosse lançada uma exceção ao tentar adicionar categorias no título com categoria inativa");
	}
	
	@Test
	void deveRemoverCategoriasDoTitulo() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		List<Categoria> categorias = List.of(new Categoria("Categoria 1"), new Categoria("Categoria 2"));
		
		categorias.forEach(titulo::adicionarCategoria);
		
		Assertions.assertEquals(2, titulo.getCategorias().size());
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(categoriaService.buscarTodosPorId(anySet())).thenReturn(categorias);
		
		tituloService.removerCategorias(1L, Set.of(1L, 2L));
		
		Assertions.assertEquals(0, titulo.getCategorias().size());
	}
	
	@Test
	void deveAdicionarAutoresNoTitulo() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		List<Autor> autores = List.of(new Autor("Autor 1"), new Autor("Autor 2"));
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(autorService.buscarTodosPorId(anySet())).thenReturn(autores);
		
		tituloService.adicionarAutores(1L, Set.of(1L, 2L));
		
		Assertions.assertEquals(2, titulo.getAutores().size());
		Assertions.assertTrue(titulo.getAutores().stream().allMatch(a -> a.getNome().equals("Autor 1") || a.getNome().equals("Autor 2")));
	}
	
	@Test
	void deveLancarExcecaoAoAdicionarAutoresNoTituloComAutorInativo() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		Autor autorInativo = new Autor("Autor 2");
		autorInativo.inativar();
		
		List<Autor> autores = List.of(new Autor("Autor 1"), autorInativo);
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(autorService.buscarTodosPorId(anySet())).thenReturn(autores);
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> tituloService.adicionarAutores(1L, Set.of(1L, 2L)),
				"Era esperado que fosse lançada uma exceção ao tentar adicionar autores no título com autor inativo");
	}
	
	@Test
	void deveRemoverAutoresDoTitulo() {
		Titulo titulo = new Titulo("Título 1", "Descrição 1");
		
		List<Autor> autores = List.of(new Autor("Autor 1"), new Autor("Autor 2"));
		
		autores.forEach(titulo::adicionarAutor);
		
		Assertions.assertEquals(2, titulo.getAutores().size());
		
		when(tituloRepository.findById(any(Long.class))).thenReturn(Optional.of(titulo));
		when(autorService.buscarTodosPorId(anySet())).thenReturn(autores);
		
		tituloService.removerAutores(1L, Set.of(1L, 2L));
		
		Assertions.assertEquals(0, titulo.getAutores().size());
	}
}

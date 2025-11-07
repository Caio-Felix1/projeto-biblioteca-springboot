package com.projeto.sistemabiblioteca.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.CategoriaJaCadastradaException;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;
import com.projeto.sistemabiblioteca.services.CategoriaService;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {
	
	@Mock
	private CategoriaRepository categoriaRepository;
	
	@InjectMocks
	private CategoriaService categoriaService;
	
	@Test
	void deveCadastrarCategoria() {
		Categoria categoria = new Categoria("Categoria 1");
		
		when(categoriaRepository.existsByNome(any(String.class))).thenReturn(false);
		
		categoriaService.inserir(categoria);
		
		verify(categoriaRepository).existsByNome(any(String.class));
		verify(categoriaRepository).save(argThat(cat ->
		cat.getNome().equals("Categoria 1") &&
		cat.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarCategoriaJaExistente() {
		Categoria categoria = new Categoria("Categoria 1");
		
		when(categoriaRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(CategoriaJaCadastradaException.class,
				() -> categoriaService.inserir(categoria),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar uma categoria já existente no banco de dados");
	}
	
	@Test
	void deveAtualizarCategoria() {
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		when(categoriaRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria1));
		when(categoriaRepository.existsByNome(any(String.class))).thenReturn(false);
		
		categoriaService.atualizar(1L, categoria2);
		
		verify(categoriaRepository).findById(any(Long.class));
		verify(categoriaRepository).existsByNome(any(String.class));
		verify(categoriaRepository).save(argThat(cat ->
		cat.getNome().equals("Categoria 2") &&
		cat.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveAtualizarAutorComMesmoNome() {
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 1");
		
		when(categoriaRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria1));
		
		categoriaService.atualizar(1L, categoria2);
		
		verify(categoriaRepository).findById(any(Long.class));
		verify(categoriaRepository).save(argThat(cat ->
		cat.getNome().equals("Categoria 1") &&
		cat.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarAutorComUmNomeJaExistente() {
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		when(categoriaRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria1));
		when(categoriaRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(CategoriaJaCadastradaException.class,
				() -> categoriaService.atualizar(1L, categoria2),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar uma categoria com um nome já existente no banco de dados");
	}
}

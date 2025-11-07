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

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.AutorJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;
import com.projeto.sistemabiblioteca.services.AutorService;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {
	
	@Mock
	private AutorRepository autorRepository;
	
	@InjectMocks
	private AutorService autorService;
	
	@Test
	void deveCadastrarAutor() {
		Autor autor = new Autor("Autor 1");
		
		when(autorRepository.existsByNome(any(String.class))).thenReturn(false);
		
		autorService.inserir(autor);
		
		verify(autorRepository).existsByNome(any(String.class));
		verify(autorRepository).save(argThat(aut ->
		aut.getNome().equals("Autor 1") &&
		aut.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarAutorJaExistente() {
		Autor autor = new Autor("Autor 1");
		
		when(autorRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(AutorJaCadastradoException.class,
				() -> autorService.inserir(autor),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar um autor já existente no banco de dados");
	}
	
	@Test
	void deveAtualizarAutor() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		when(autorRepository.findById(any(Long.class))).thenReturn(Optional.of(autor1));
		when(autorRepository.existsByNome(any(String.class))).thenReturn(false);
		
		autorService.atualizar(1L, autor2);
		
		verify(autorRepository).findById(any(Long.class));
		verify(autorRepository).existsByNome(any(String.class));
		verify(autorRepository).save(argThat(aut ->
		aut.getNome().equals("Autor 2") &&
		aut.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveAtualizarAutorComMesmoNome() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 1");
		
		when(autorRepository.findById(any(Long.class))).thenReturn(Optional.of(autor1));
		
		autorService.atualizar(1L, autor2);
		
		verify(autorRepository).findById(any(Long.class));
		verify(autorRepository).save(argThat(aut ->
		aut.getNome().equals("Autor 1") &&
		aut.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarAutorComUmNomeJaExistente() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		when(autorRepository.findById(any(Long.class))).thenReturn(Optional.of(autor1));
		when(autorRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(AutorJaCadastradoException.class,
				() -> autorService.atualizar(1L, autor2),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar um autor com um nome já existente no banco de dados");
	}
}

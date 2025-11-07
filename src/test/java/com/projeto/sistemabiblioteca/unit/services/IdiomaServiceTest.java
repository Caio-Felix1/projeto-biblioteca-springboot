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

import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.IdiomaJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;
import com.projeto.sistemabiblioteca.services.IdiomaService;

@ExtendWith(MockitoExtension.class)
public class IdiomaServiceTest {
	
	@Mock
	private IdiomaRepository idiomaRepository;
	
	@InjectMocks
	private IdiomaService idiomaService;
	
	@Test
	void deveCadastrarIdioma() {
		Idioma idioma = new Idioma("Idioma 1");
		
		when(idiomaRepository.existsByNome(any(String.class))).thenReturn(false);
		
		idiomaService.inserir(idioma);
		
		verify(idiomaRepository).existsByNome(any(String.class));
		verify(idiomaRepository).save(argThat(aut ->
		aut.getNome().equals("Idioma 1") &&
		aut.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarIdiomaJaExistente() {
		Idioma idioma = new Idioma("Idioma 1");
		
		when(idiomaRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(IdiomaJaCadastradoException.class,
				() -> idiomaService.inserir(idioma),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar um idioma já existente no banco de dados");
	}
	
	@Test
	void deveAtualizarIdioma() {
		Idioma idioma1 = new Idioma("Idioma 1");
		Idioma idioma2 = new Idioma("Idioma 2");
		
		when(idiomaRepository.findById(any(Long.class))).thenReturn(Optional.of(idioma1));
		when(idiomaRepository.existsByNome(any(String.class))).thenReturn(false);
		
		idiomaService.atualizar(1L, idioma2);
		
		verify(idiomaRepository).findById(any(Long.class));
		verify(idiomaRepository).existsByNome(any(String.class));
		verify(idiomaRepository).save(argThat(aut ->
		aut.getNome().equals("Idioma 2") &&
		aut.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveAtualizarIdiomaComMesmoNome() {
		Idioma idioma1 = new Idioma("Idioma 1");
		Idioma idioma2 = new Idioma("Idioma 1");
		
		when(idiomaRepository.findById(any(Long.class))).thenReturn(Optional.of(idioma1));
		
		idiomaService.atualizar(1L, idioma2);
		
		verify(idiomaRepository).findById(any(Long.class));
		verify(idiomaRepository).save(argThat(aut ->
		aut.getNome().equals("Idioma 1") &&
		aut.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarIdiomaComUmNomeJaExistente() {
		Idioma idioma1 = new Idioma("Idioma 1");
		Idioma idioma2 = new Idioma("Idioma 2");
		
		when(idiomaRepository.findById(any(Long.class))).thenReturn(Optional.of(idioma1));
		when(idiomaRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(IdiomaJaCadastradoException.class,
				() -> idiomaService.atualizar(1L, idioma2),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar um idioma com um nome já existente no banco de dados");
	}
}

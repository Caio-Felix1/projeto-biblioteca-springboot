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

import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.PaisJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;
import com.projeto.sistemabiblioteca.services.PaisService;

@ExtendWith(MockitoExtension.class)
public class PaisServiceTest {
	
	@Mock
	private PaisRepository paisRepository;
	
	@InjectMocks
	private PaisService paisService;
	
	@Test
	void deveCadastrarPais() {
		Pais pais = new Pais("País 1");
		
		when(paisRepository.existsByNome(any(String.class))).thenReturn(false);
		
		paisService.inserir(pais);
		
		verify(paisRepository).existsByNome(any(String.class));
		verify(paisRepository).save(argThat(p ->
		p.getNome().equals("País 1") &&
		p.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarPaisJaExistente() {
		Pais pais = new Pais("País 1");
		
		when(paisRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(PaisJaCadastradoException.class,
				() -> paisService.inserir(pais),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar um país já existente no banco de dados");
	}
	
	@Test
	void deveAtualizarPais() {
		Pais pais1 = new Pais("País 1");
		Pais pais2 = new Pais("País 2");
		
		when(paisRepository.findById(any(Long.class))).thenReturn(Optional.of(pais1));
		when(paisRepository.existsByNome(any(String.class))).thenReturn(false);
		
		paisService.atualizar(1L, pais2);
		
		verify(paisRepository).findById(any(Long.class));
		verify(paisRepository).existsByNome(any(String.class));
		verify(paisRepository).save(argThat(p ->
		p.getNome().equals("País 2") &&
		p.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveAtualizarPaisComMesmoNome() {
		Pais pais1 = new Pais("País 1");
		Pais pais2 = new Pais("País 1");
		
		when(paisRepository.findById(any(Long.class))).thenReturn(Optional.of(pais1));
		
		paisService.atualizar(1L, pais2);
		
		verify(paisRepository).findById(any(Long.class));
		verify(paisRepository).save(argThat(p ->
		p.getNome().equals("País 1") &&
		p.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarPaisComUmNomeJaExistente() {
		Pais pais1 = new Pais("País 1");
		Pais pais2 = new Pais("País 2");
		
		when(paisRepository.findById(any(Long.class))).thenReturn(Optional.of(pais1));
		when(paisRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(PaisJaCadastradoException.class,
				() -> paisService.atualizar(1L, pais2),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar um país com um nome já existente no banco de dados");
	}
}

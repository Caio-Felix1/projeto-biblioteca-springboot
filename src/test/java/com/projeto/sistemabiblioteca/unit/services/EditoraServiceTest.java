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

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.EditoraJaCadastradaException;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;
import com.projeto.sistemabiblioteca.services.EditoraService;

@ExtendWith(MockitoExtension.class)
public class EditoraServiceTest {
	
	@Mock
	private EditoraRepository editoraRepository;
	
	@InjectMocks
	private EditoraService editoraService;
	
	@Test
	void deveCadastrarEditora() {
		Editora editora = new Editora("Editora 1");
		
		when(editoraRepository.existsByNome(any(String.class))).thenReturn(false);
		
		editoraService.inserir(editora);
		
		verify(editoraRepository).existsByNome(any(String.class));
		verify(editoraRepository).save(argThat(edit ->
		edit.getNome().equals("Editora 1") &&
		edit.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarEditoraJaExistente() {
		Editora editora = new Editora("Editora 1");
		
		when(editoraRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(EditoraJaCadastradaException.class,
				() -> editoraService.inserir(editora),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar uma editora já existente no banco de dados");
	}
	
	@Test
	void deveAtualizarEditora() {
		Editora editora1 = new Editora("Editora 1");
		Editora editora2 = new Editora("Editora 2");
		
		when(editoraRepository.findById(any(Long.class))).thenReturn(Optional.of(editora1));
		when(editoraRepository.existsByNome(any(String.class))).thenReturn(false);
		
		editoraService.atualizar(1L, editora2);
		
		verify(editoraRepository).findById(any(Long.class));
		verify(editoraRepository).existsByNome(any(String.class));
		verify(editoraRepository).save(argThat(edit ->
		edit.getNome().equals("Editora 2") &&
		edit.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveAtualizarEditoraComMesmoNome() {
		Editora editora1 = new Editora("Editora 1");
		Editora editora2 = new Editora("Editora 1");
		
		when(editoraRepository.findById(any(Long.class))).thenReturn(Optional.of(editora1));
		
		editoraService.atualizar(1L, editora2);
		
		verify(editoraRepository).findById(any(Long.class));
		verify(editoraRepository).save(argThat(edit ->
		edit.getNome().equals("Editora 1") &&
		edit.getStatusAtivo() == StatusAtivo.ATIVO));
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarEditoraComUmNomeJaExistente() {
		Editora editora1 = new Editora("Editora 1");
		Editora editora2 = new Editora("Editora 2");
		
		when(editoraRepository.findById(any(Long.class))).thenReturn(Optional.of(editora1));
		when(editoraRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(EditoraJaCadastradaException.class,
				() -> editoraService.atualizar(1L, editora2),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar uma editora com um nome já existente no banco de dados");
	}
}

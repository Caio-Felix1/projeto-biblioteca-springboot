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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.sistemabiblioteca.DTOs.EstadoDTO;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.exceptions.EstadoJaCadastradoException;
import com.projeto.sistemabiblioteca.repositories.EstadoRepository;
import com.projeto.sistemabiblioteca.services.EstadoService;
import com.projeto.sistemabiblioteca.services.PaisService;

@ExtendWith(MockitoExtension.class)
public class EstadoServiceTest {
	
	@Mock
	private EstadoRepository estadoRepository;
	
	@Mock
	private PaisService paisService;
	
	@InjectMocks
	private EstadoService estadoService;
	
	@Test
	void deveCadastrarEstado() {
		Pais pais = new Pais("País 1");
		EstadoDTO estadoDTO = new EstadoDTO("Estado 1", 1L);
		
		when(paisService.buscarPorId(any(Long.class))).thenReturn(pais);
		when(estadoRepository.existsByNome(any(String.class))).thenReturn(false);
		
		estadoService.cadastrar(estadoDTO);
		
		verify(paisService).buscarPorId(any(Long.class));
		verify(estadoRepository).existsByNome(any(String.class));
		verify(estadoRepository).save(argThat(estado ->
		estado.getNome().equals("Estado 1") &&
		estado.getStatusAtivo() == StatusAtivo.ATIVO &&
		estado.getPais().getNome().equals("País 1")));
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarEstadoJaExistente() {
		Pais pais = new Pais("País 1");
		EstadoDTO estadoDTO = new EstadoDTO("Estado 1", 1L);
		
		when(paisService.buscarPorId(any(Long.class))).thenReturn(pais);
		when(estadoRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(EstadoJaCadastradoException.class,
				() -> estadoService.cadastrar(estadoDTO),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar um estado já existente no banco de dados");
	}
	
	@Test
	void deveLancarExcecaoAoTentarCadastrarEstadoComPaisInativo() {
		Pais pais = new Pais("País 1");
		pais.inativar();
		
		EstadoDTO estadoDTO = new EstadoDTO("Estado 1", 1L);
		
		when(paisService.buscarPorId(any(Long.class))).thenReturn(pais);
		
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> estadoService.cadastrar(estadoDTO),
				"Era esperado que fosse lançada uma exceção ao tentar cadastrar um estado com um país inativo");
	}
	
	@Test
	void deveAtualizarEstado() {
		Pais pais = new Pais("País 2");
		
		Pais paisMock = Mockito.mock(Pais.class);
		
		Estado estado1 = Mockito.spy(new Estado("Estado 1", paisMock));
		when(estado1.getPais().getIdPais()).thenReturn(1L);
		
		EstadoDTO estadoDTO = new EstadoDTO("Estado 2", 2L);
		
		when(paisService.buscarPorId(any(Long.class))).thenReturn(pais);
		when(estadoRepository.findById(any(Long.class))).thenReturn(Optional.of(estado1));
		when(estadoRepository.existsByNome(any(String.class))).thenReturn(false);
		
		estadoService.atualizar(1L, estadoDTO);
		
		verify(paisService).buscarPorId(any(Long.class));
		verify(estadoRepository).findById(any(Long.class));
		verify(estadoRepository).existsByNome(any(String.class));
		verify(estadoRepository).save(argThat(estado ->
		estado.getNome().equals("Estado 2") &&
		estado.getStatusAtivo() == StatusAtivo.ATIVO &&
		estado.getPais().getNome().equals("País 2")));
	}
	
	@Test
	void deveAtualizarEstadoComOMesmoPais() {
		Pais pais = Mockito.spy(new Pais("País 1"));
		when(pais.getIdPais()).thenReturn(1L);
		
		Estado estado1 = Mockito.spy(new Estado("Estado 1", pais));
		
		EstadoDTO estadoDTO = new EstadoDTO("Estado 2", 1L);
		
		when(estadoRepository.findById(any(Long.class))).thenReturn(Optional.of(estado1));
		when(estadoRepository.existsByNome(any(String.class))).thenReturn(false);
		
		estadoService.atualizar(1L, estadoDTO);
		
		verify(estadoRepository).findById(any(Long.class));
		verify(estadoRepository).existsByNome(any(String.class));
		verify(estadoRepository).save(argThat(estado ->
		estado.getNome().equals("Estado 2") &&
		estado.getStatusAtivo() == StatusAtivo.ATIVO &&
		estado.getPais().getNome().equals("País 1")));
	}
	
	@Test
	void deveAtualizarEstadoComMesmoNome() {
		Pais pais = new Pais("País 2");
		
		Pais paisMock = Mockito.mock(Pais.class);
		
		Estado estado1 = Mockito.spy(new Estado("Estado 1", paisMock));
		when(estado1.getPais().getIdPais()).thenReturn(1L);
		
		EstadoDTO estadoDTO = new EstadoDTO("Estado 1", 2L);
		
		when(paisService.buscarPorId(any(Long.class))).thenReturn(pais);
		when(estadoRepository.findById(any(Long.class))).thenReturn(Optional.of(estado1));
		
		estadoService.atualizar(1L, estadoDTO);
		
		verify(paisService).buscarPorId(any(Long.class));
		verify(estadoRepository).findById(any(Long.class));
		verify(estadoRepository).save(argThat(estado ->
		estado.getNome().equals("Estado 1") &&
		estado.getStatusAtivo() == StatusAtivo.ATIVO &&
		estado.getPais().getNome().equals("País 2")));
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarEstadoComPaisInativo() {
		Pais pais = new Pais("País 2");
		pais.inativar();
		
		Pais paisMock = Mockito.mock(Pais.class);
		
		Estado estado1 = Mockito.spy(new Estado("Estado 1", paisMock));
		when(estado1.getPais().getIdPais()).thenReturn(1L);
		
		EstadoDTO estadoDTO = new EstadoDTO("Estado 2", 2L);
		
		when(paisService.buscarPorId(any(Long.class))).thenReturn(pais);
		when(estadoRepository.findById(any(Long.class))).thenReturn(Optional.of(estado1));
			
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> estadoService.atualizar(1L, estadoDTO),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar um estado com um país inativo");
	}
	
	@Test
	void deveLancarExcecaoAoTentarAtualizarEstadoComUmNomeJaExistente() {
		Pais pais = new Pais("País 2");
		
		Pais paisMock = Mockito.mock(Pais.class);
		
		Estado estado1 = Mockito.spy(new Estado("Estado 1", paisMock));
		when(estado1.getPais().getIdPais()).thenReturn(1L);
		
		EstadoDTO estadoDTO = new EstadoDTO("Estado 2", 2L);
		
		when(paisService.buscarPorId(any(Long.class))).thenReturn(pais);
		when(estadoRepository.findById(any(Long.class))).thenReturn(Optional.of(estado1));
		when(estadoRepository.existsByNome(any(String.class))).thenReturn(true);
		
		Assertions.assertThrows(EstadoJaCadastradoException.class,
				() -> estadoService.atualizar(1L, estadoDTO),
				"Era esperado que fosse lançada uma exceção ao tentar atualizar um estado com um nome já existente no banco de dados");
	}
}

package com.projeto.sistemabiblioteca.dashboard.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimAutorRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimAutorService;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.repositories.AutorRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimAutorServiceIntegrationTest {
	
	@Autowired
	private DimAutorService dimAutorService;
	
	@Autowired
	private DimAutorRepository dimAutorRepository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Test
	void deveCriarUmNovoDimAutor() {
		Autor autor = new Autor("Autor 1");
		
		autorRepository.save(autor);
		
		DimAutor dimAutorNovo = dimAutorService.atualizar(autor);
		
		Assertions.assertTrue(dimAutorNovo.getSurrogateKey() != null && dimAutorNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(autor.getIdAutor(), dimAutorNovo.getIdNatural());
		Assertions.assertEquals(autor.getNome(), dimAutorNovo.getNome());
		Assertions.assertEquals(autor.getStatusAtivo(), dimAutorNovo.getStatus());
	}
	
	@Test
	void deveAtualizarDimAutorJaExistente() {
		Autor autor = new Autor("Autor 1");
		
		autorRepository.save(autor);
		
		DimAutor dimAutor = new DimAutor(autor);
		
		dimAutorRepository.save(dimAutor);
		
		autor.setNome("Autor 2");
		autor.inativar();
		
		DimAutor dimAutorAtualizado = dimAutorService.atualizar(autor);
		
		Assertions.assertEquals(dimAutor.getSurrogateKey(), dimAutorAtualizado.getSurrogateKey());
		Assertions.assertEquals(autor.getIdAutor(), dimAutorAtualizado.getIdNatural());
		Assertions.assertEquals(autor.getNome(), dimAutorAtualizado.getNome());
		Assertions.assertEquals(autor.getStatusAtivo(), dimAutorAtualizado.getStatus());
	}
}

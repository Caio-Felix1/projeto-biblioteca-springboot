package com.projeto.sistemabiblioteca.dashboard.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimIdioma;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimIdiomaRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimIdiomaService;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimIdiomaServiceIntegrationTest {
	
	@Autowired
	private DimIdiomaService dimIdiomaService;
	
	@Autowired
	private DimIdiomaRepository dimIdiomaRepository;
	
	@Autowired
	private IdiomaRepository IdiomaRepository;
	
	@Test
	void deveCriarUmNovoDimIdioma() {
		Idioma idioma = new Idioma("Idioma 1");
		
		IdiomaRepository.save(idioma);
		
		DimIdioma dimIdiomaNovo = dimIdiomaService.atualizar(idioma);
		
		Assertions.assertTrue(dimIdiomaNovo.getSurrogateKey() != null && dimIdiomaNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(idioma.getIdIdioma(), dimIdiomaNovo.getIdNatural());
		Assertions.assertEquals(idioma.getNome(), dimIdiomaNovo.getNome());
		Assertions.assertEquals(idioma.getStatusAtivo(), dimIdiomaNovo.getStatus());
	}
	
	@Test
	void deveAtualizarDimIdiomaJaExistente() {
		Idioma idioma = new Idioma("Idioma 1");
		
		IdiomaRepository.save(idioma);
		
		DimIdioma dimIdioma = new DimIdioma(idioma);
		
		dimIdiomaRepository.save(dimIdioma);
		
		idioma.setNome("Idioma 2");
		idioma.inativar();
		
		DimIdioma dimIdiomaAtualizado = dimIdiomaService.atualizar(idioma);
		
		Assertions.assertEquals(dimIdioma.getSurrogateKey(), dimIdiomaAtualizado.getSurrogateKey());
		Assertions.assertEquals(idioma.getIdIdioma(), dimIdiomaAtualizado.getIdNatural());
		Assertions.assertEquals(idioma.getNome(), dimIdiomaAtualizado.getNome());
		Assertions.assertEquals(idioma.getStatusAtivo(), dimIdiomaAtualizado.getStatus());
	}
}

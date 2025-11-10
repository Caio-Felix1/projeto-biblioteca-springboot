package com.projeto.sistemabiblioteca.dashboard.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimCategoriaRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimCategoriaService;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.repositories.CategoriaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimCategoriaServiceIntegrationTest {
	
	@Autowired
	private DimCategoriaService dimCategoriaService;
	
	@Autowired
	private DimCategoriaRepository dimCategoriaRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Test
	void deveCriarUmNovoDimCategoria() {
		Categoria categoria = new Categoria("Categoria 1");
		
		categoriaRepository.save(categoria);
		
		DimCategoria dimCategoriaNovo = dimCategoriaService.atualizar(categoria);
		
		Assertions.assertTrue(dimCategoriaNovo.getSurrogateKey() != null && dimCategoriaNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(categoria.getIdCategoria(), dimCategoriaNovo.getIdNatural());
		Assertions.assertEquals(categoria.getNome(), dimCategoriaNovo.getNome());
		Assertions.assertEquals(categoria.getStatusAtivo(), dimCategoriaNovo.getStatus());
	}
	
	@Test
	void deveAtualizarDimCategoriaJaExistente() {
		Categoria categoria = new Categoria("Categoria 1");
		
		categoriaRepository.save(categoria);
		
		DimCategoria dimCategoria = new DimCategoria(categoria);
		
		dimCategoriaRepository.save(dimCategoria);
		
		categoria.setNome("Categoria 2");
		categoria.inativar();
		
		DimCategoria dimCategoriaAtualizado = dimCategoriaService.atualizar(categoria);
		
		Assertions.assertEquals(dimCategoria.getSurrogateKey(), dimCategoriaAtualizado.getSurrogateKey());
		Assertions.assertEquals(categoria.getIdCategoria(), dimCategoriaAtualizado.getIdNatural());
		Assertions.assertEquals(categoria.getNome(), dimCategoriaAtualizado.getNome());
		Assertions.assertEquals(categoria.getStatusAtivo(), dimCategoriaAtualizado.getStatus());
	}
}

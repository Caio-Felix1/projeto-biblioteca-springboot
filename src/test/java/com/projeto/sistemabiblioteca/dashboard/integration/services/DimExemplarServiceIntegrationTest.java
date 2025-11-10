package com.projeto.sistemabiblioteca.dashboard.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimExemplar;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimExemplarRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimExemplarService;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimExemplarServiceIntegrationTest {
	
	@Autowired
	private DimExemplarService dimExemplarService;
	
	@Autowired
	private DimExemplarRepository dimExemplarRepository;
	
	@Autowired
	private ExemplarRepository exemplarRepository;
	
	@Test
	void deveCriarUmNovoDimExemplar() {
		Exemplar exemplar = new Exemplar(EstadoFisico.EXCELENTE, null);
		
		exemplarRepository.save(exemplar);
		
		DimExemplar dimExemplarNovo = dimExemplarService.atualizar(exemplar);
		
		Assertions.assertTrue(dimExemplarNovo.getSurrogateKey() != null && dimExemplarNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(exemplar.getIdExemplar(), dimExemplarNovo.getIdNatural());
		Assertions.assertEquals(exemplar.getEstadoFisico(), dimExemplarNovo.getEstadoFisico());
		Assertions.assertEquals(exemplar.getStatus(), dimExemplarNovo.getStatus());
	}
	
	@Test
	void deveAtualizarDimExemplarJaExistente() {
		Exemplar exemplar = new Exemplar(EstadoFisico.EXCELENTE, null);
		
		exemplarRepository.save(exemplar);
		
		DimExemplar dimExemplar = new DimExemplar(exemplar);
		
		dimExemplarRepository.save(dimExemplar);
		
		exemplar.setEstadoFisico(EstadoFisico.MUITO_RUIM);
		exemplar.alugar();
		
		DimExemplar dimExemplarAtualizado = dimExemplarService.atualizar(exemplar);
		
		Assertions.assertEquals(dimExemplar.getSurrogateKey(), dimExemplarAtualizado.getSurrogateKey());
		Assertions.assertEquals(exemplar.getIdExemplar(), dimExemplarAtualizado.getIdNatural());
		Assertions.assertEquals(exemplar.getEstadoFisico(), dimExemplarAtualizado.getEstadoFisico());
		Assertions.assertEquals(exemplar.getStatus(), dimExemplarAtualizado.getStatus());
	}
}

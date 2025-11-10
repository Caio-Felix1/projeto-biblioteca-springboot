package com.projeto.sistemabiblioteca.dashboard.integration.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEditora;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimEditoraRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimEditoraService;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimEditoraServiceIntegrationTest {
	
	@Autowired
	private DimEditoraService dimEditoraService;
	
	@Autowired
	private DimEditoraRepository dimEditoraRepository;
	
	@Autowired
	private EditoraRepository editoraRepository;
	
	@Test
	void deveCriarUmNovoDimEditora() {
		Editora editora = new Editora("Editora 1");
		
		editoraRepository.save(editora);
		
		DimEditora dimEditoraNovo = dimEditoraService.atualizar(editora);
		
		Assertions.assertTrue(dimEditoraNovo.getSurrogateKey() != null && dimEditoraNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(editora.getIdEditora(), dimEditoraNovo.getIdNatural());
		Assertions.assertEquals(editora.getNome(), dimEditoraNovo.getNome());
		Assertions.assertEquals(editora.getStatusAtivo(), dimEditoraNovo.getStatus());
	}
	
	@Test
	void deveAtualizarDimEditoraJaExistente() {
		Editora editora = new Editora("Editora 1");
		
		editoraRepository.save(editora);
		
		DimEditora dimEditora = new DimEditora(editora);
		
		dimEditoraRepository.save(dimEditora);
		
		editora.setNome("Editora 2");
		editora.inativar();
		
		DimEditora dimEditoraAtualizado = dimEditoraService.atualizar(editora);
		
		Assertions.assertEquals(dimEditora.getSurrogateKey(), dimEditoraAtualizado.getSurrogateKey());
		Assertions.assertEquals(editora.getIdEditora(), dimEditoraAtualizado.getIdNatural());
		Assertions.assertEquals(editora.getNome(), dimEditoraAtualizado.getNome());
		Assertions.assertEquals(editora.getStatusAtivo(), dimEditoraAtualizado.getStatus());
	}
}

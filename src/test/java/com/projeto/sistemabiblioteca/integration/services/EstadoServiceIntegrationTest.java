package com.projeto.sistemabiblioteca.integration.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.PaisRepository;
import com.projeto.sistemabiblioteca.services.EstadoService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class EstadoServiceIntegrationTest {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private PaisRepository paisRepository;
	
	@Test
	void deveBuscarTodosFiltrandoPorPaisEStatusAtivo() {
		Pais pais1 = new Pais("Brasil");
		Pais pais2 = new Pais("Estados Unidos");
		
		paisRepository.save(pais1);
		paisRepository.save(pais2);
		
		Estado estado1 = new Estado("São Paulo", pais1);
		Estado estado2 = new Estado("Rio de Janeiro", pais1);
		
		Estado estado3 = new Estado("Espírito Santo", pais1);
		estado3.inativar();
		Estado estado4 = new Estado("Minas Gerais", pais1);
		estado4.inativar();
		
		Estado estado5 = new Estado("Nova York", pais2);
		Estado estado6 = new Estado("Flórida", pais2);
		
		estadoService.inserir(estado1);
		estadoService.inserir(estado2);
		estadoService.inserir(estado3);
		estadoService.inserir(estado4);
		estadoService.inserir(estado5);
		estadoService.inserir(estado6);
		
		List<Estado> estados = estadoService.buscarTodosFiltrandoPorPais(pais1.getIdPais());
		
		Assertions.assertEquals(2, estados.size());
		Assertions.assertTrue(estados.stream().allMatch(e -> e.getPais().getNome().equals("Brasil")));
		Assertions.assertTrue(estados.stream().allMatch(e -> List.of("São Paulo", "Rio de Janeiro").contains(e.getNome())));
		Assertions.assertTrue(estados.stream().allMatch(e -> e.getStatusAtivo() == StatusAtivo.ATIVO));
	}
}

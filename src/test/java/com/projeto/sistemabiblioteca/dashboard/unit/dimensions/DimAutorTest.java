package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public class DimAutorTest {
	
	@Test
	void deveConverterAutorParaDimAutorCorretamente() {
		Autor autor = new Autor("Autor 1");
		
		DimAutor dimAutor = new DimAutor(autor);
		
		Assertions.assertEquals("Autor 1", dimAutor.getNome());
		Assertions.assertEquals(StatusAtivo.ATIVO, dimAutor.getStatus());
	}
}

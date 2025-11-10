package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimIdioma;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public class DimIdiomaTest {
	
	@Test
	void deveConverterIdiomaParaDimIdiomaCorretamente() {
		Idioma idioma = new Idioma("Idioma 1");
		
		DimIdioma dimIdioma = new DimIdioma(idioma);
		
		Assertions.assertEquals("Idioma 1", dimIdioma.getNome());
		Assertions.assertEquals(StatusAtivo.ATIVO, dimIdioma.getStatus());
	}
}

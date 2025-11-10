package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEditora;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public class DimEditoraTest {
	
	@Test
	void deveConverterEditoraParaDimEditoraCorretamente() {
		Editora editora = new Editora("Editora 1");
		
		DimEditora dimEditora = new DimEditora(editora);
		
		Assertions.assertEquals("Editora 1", dimEditora.getNome());
		Assertions.assertEquals(StatusAtivo.ATIVO, dimEditora.getStatus());
	}
}

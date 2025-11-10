package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public class DimCategoriaTest {
	
	@Test
	void deveConverterCategoriaParaDimCategoriaCorretamente() {
		Categoria categoria = new Categoria("Categoria 1");
		
		DimCategoria dimCategoria = new DimCategoria(categoria);
		
		Assertions.assertEquals("Categoria 1", dimCategoria.getNome());
		Assertions.assertEquals(StatusAtivo.ATIVO, dimCategoria.getStatus());
	}
}

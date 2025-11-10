package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimTitulo;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public class DimTituloTest {
	
	@Test
	void deveConverterTituloParaDimTituloCorretamente() {
		Autor autor1 = new Autor("Autor 1");
		Autor autor2 = new Autor("Autor 2");
		
		Categoria categoria1 = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 2");
		
		Set<DimAutor> autores = Set.of(new DimAutor(autor1), new DimAutor(autor2));
		Set<DimCategoria> categorias = Set.of(new DimCategoria(categoria1), new DimCategoria(categoria2));
		
		Titulo titulo = new Titulo("Titulo 1", "Descrição 1");
		
		DimTitulo dimTitulo = new DimTitulo(titulo, categorias, autores);
		
		Assertions.assertEquals("Titulo 1", dimTitulo.getNome());
		Assertions.assertEquals(StatusAtivo.ATIVO, dimTitulo.getStatus());
		
		Assertions.assertEquals(2, dimTitulo.getAutores().size());
		Assertions.assertTrue(dimTitulo.getAutores().stream().allMatch(a -> List.of("Autor 1", "Autor 2").contains(a.getNome())));
		
		Assertions.assertEquals(2, dimTitulo.getCategorias().size());
		Assertions.assertTrue(dimTitulo.getCategorias().stream().allMatch(c -> List.of("Categoria 1", "Categoria 2").contains(c.getNome())));
	}
}

package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimExemplar;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;

public class DimExemplarTest {
	
	@Test
	void deveConverterExemplarParaDimExemplarCorretamente() {
		Exemplar exemplar = new Exemplar(EstadoFisico.EXCELENTE, null);
		
		DimExemplar dimExemplar = new DimExemplar(exemplar);
		
		Assertions.assertEquals(EstadoFisico.EXCELENTE, dimExemplar.getEstadoFisico());
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, dimExemplar.getStatus());
	}
}

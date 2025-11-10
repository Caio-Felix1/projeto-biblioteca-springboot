package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEdicao;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;

public class DimEdicaoTest {
	
	@Test
	void deveConverterEdicaoParaDimEdicaoCorretamente() {
		Edicao edicao = new Edicao(
				"Edição de Colecionador",
				TipoCapa.DURA,
				350,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2020, 2, 2),
				null,
				null,
				null,
				null);
		
		DimEdicao dimEdicao = new DimEdicao(edicao);
		
		Assertions.assertEquals("Edição de Colecionador", dimEdicao.getDescricaoEdicao());
		Assertions.assertEquals(TipoCapa.DURA, dimEdicao.getTipoCapa());
		Assertions.assertEquals(350, dimEdicao.getQtdPaginas());
		Assertions.assertEquals(TamanhoEdicao.MEDIO, dimEdicao.getTamanho());
		Assertions.assertEquals(ClassificacaoIndicativa.C14, dimEdicao.getClassificacao());
		Assertions.assertEquals(LocalDate.of(2020, 2, 2), dimEdicao.getDtPublicacao());
		Assertions.assertEquals(StatusAtivo.ATIVO, dimEdicao.getStatus());
	}
}

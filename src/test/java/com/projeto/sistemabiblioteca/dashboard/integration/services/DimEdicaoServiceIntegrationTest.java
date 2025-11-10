package com.projeto.sistemabiblioteca.dashboard.integration.services;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEdicao;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimEdicaoRepository;
import com.projeto.sistemabiblioteca.dashboard.services.DimEdicaoService;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class DimEdicaoServiceIntegrationTest {
	
	@Autowired
	private DimEdicaoService dimEdicaoService;
	
	@Autowired
	private DimEdicaoRepository dimEdicaoRepository;
	
	@Autowired
	private EdicaoRepository edicaoRepository;
	
	@Test
	void deveCriarUmNovoDimEdicao() {
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
		
		edicaoRepository.save(edicao);
		
		DimEdicao dimEdicaoNovo = dimEdicaoService.atualizar(edicao);
		
		Assertions.assertTrue(dimEdicaoNovo.getSurrogateKey() != null && dimEdicaoNovo.getSurrogateKey().getClass().equals(Long.class));
		Assertions.assertEquals(edicao.getIdEdicao(), dimEdicaoNovo.getIdNatural());
		Assertions.assertEquals("Edição de Colecionador", dimEdicaoNovo.getDescricaoEdicao());
		Assertions.assertEquals(TipoCapa.DURA, dimEdicaoNovo.getTipoCapa());
		Assertions.assertEquals(350, dimEdicaoNovo.getQtdPaginas());
		Assertions.assertEquals(TamanhoEdicao.MEDIO, dimEdicaoNovo.getTamanho());
		Assertions.assertEquals(ClassificacaoIndicativa.C14, dimEdicaoNovo.getClassificacao());
		Assertions.assertEquals(LocalDate.of(2020, 2, 2), dimEdicaoNovo.getDtPublicacao());
		Assertions.assertEquals(StatusAtivo.ATIVO, dimEdicaoNovo.getStatus());
	}
	
	@Test
	void deveAtualizarDimEdicaoJaExistente() {
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
		
		edicaoRepository.save(edicao);
		
		DimEdicao dimEdicao = new DimEdicao(edicao);
		
		dimEdicaoRepository.save(dimEdicao);
		
		edicao.setDescricaoEdicao("Edição de aniversário");
		edicao.setTipoCapa(TipoCapa.MOLE);
		edicao.setQtdPaginas(950);
		edicao.setTamanho(TamanhoEdicao.GRANDE);
		edicao.setClassificacao(ClassificacaoIndicativa.C18);
		edicao.setDtPublicacao(LocalDate.of(2021, 3, 3));
		edicao.inativar();
		
		dimEdicaoService.atualizar(edicao);
		
		DimEdicao dimEdicaoAtualizado = dimEdicaoRepository.findAll().get(0);
		
		Assertions.assertEquals(dimEdicao.getSurrogateKey(), dimEdicaoAtualizado.getSurrogateKey());
		Assertions.assertEquals(edicao.getIdEdicao(), dimEdicaoAtualizado.getIdNatural());
		Assertions.assertEquals("Edição de aniversário", dimEdicaoAtualizado.getDescricaoEdicao());
		Assertions.assertEquals(TipoCapa.MOLE, dimEdicaoAtualizado.getTipoCapa());
		Assertions.assertEquals(950, dimEdicaoAtualizado.getQtdPaginas());
		Assertions.assertEquals(TamanhoEdicao.GRANDE, dimEdicaoAtualizado.getTamanho());
		Assertions.assertEquals(ClassificacaoIndicativa.C18, dimEdicaoAtualizado.getClassificacao());
		Assertions.assertEquals(LocalDate.of(2021, 3, 3), dimEdicaoAtualizado.getDtPublicacao());
		Assertions.assertEquals(StatusAtivo.INATIVO, dimEdicaoAtualizado.getStatus());
	}
}

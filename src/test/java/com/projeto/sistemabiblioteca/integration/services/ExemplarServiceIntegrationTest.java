package com.projeto.sistemabiblioteca.integration.services;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.DTOs.ExemplarCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.ExemplarUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;
import com.projeto.sistemabiblioteca.services.ExemplarService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ExemplarServiceIntegrationTest {
	
	@Autowired
	private ExemplarService exemplarService;
	
	@Autowired
	private EdicaoRepository edicaoRepository;
	
	@Test
	void deveBuscarTodosFiltrandoPeloIdDaEdicao() {
		Edicao edicao1 = new Edicao(
				TipoCapa.DURA,
				100,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2025, 10, 10),
				null,
				null,
				null,
				null);
		
		Edicao edicao2 = new Edicao(
				TipoCapa.MOLE,
				2000,
				TamanhoEdicao.GRANDE,
				ClassificacaoIndicativa.C18,
				LocalDate.of(2024, 9, 9),
				null,
				null,
				null,
				null);
		
		edicaoRepository.save(edicao1);
		edicaoRepository.save(edicao2);
		
		Exemplar exemplar1 = new Exemplar(EstadoFisico.BOM, edicao1);
		Exemplar exemplar2 = new Exemplar(EstadoFisico.EXCELENTE, edicao2);
		
		exemplarService.inserir(exemplar1);
		exemplarService.inserir(exemplar2);
		
		List<Exemplar> exemplares = exemplarService.buscarTodosComEdicaoComIdIgualA(edicao2.getIdEdicao());
		
		Assertions.assertEquals(1, exemplares.size());
		Assertions.assertEquals(EstadoFisico.EXCELENTE, exemplares.get(0).getEstadoFisico());
		Assertions.assertEquals(edicao2.getIdEdicao(), exemplares.get(0).getEdicao().getIdEdicao());
	}
	
	@Test
	void buscarPrimeiroExemplarDisponivelFiltrandoPeloIdDaEdicaoEStatusEOrdenandoPeloEstadoFisico() {
		Edicao edicao1 = new Edicao(
				TipoCapa.DURA,
				100,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2025, 10, 10),
				null,
				null,
				null,
				null);
		
		Edicao edicao2 = new Edicao(
				TipoCapa.MOLE,
				2000,
				TamanhoEdicao.GRANDE,
				ClassificacaoIndicativa.C18,
				LocalDate.of(2024, 9, 9),
				null,
				null,
				null,
				null);
		
		edicaoRepository.save(edicao1);
		edicaoRepository.save(edicao2);
		
		Exemplar exemplar1 = new Exemplar(EstadoFisico.RUIM, edicao1);
		Exemplar exemplar2 = new Exemplar(EstadoFisico.BOM, edicao1);
		exemplar2.remover();
		Exemplar exemplar3 = new Exemplar(EstadoFisico.BOM, edicao1);
		Exemplar exemplar4 = new Exemplar(EstadoFisico.EXCELENTE, edicao2);
		
		exemplarService.inserir(exemplar1);
		exemplarService.inserir(exemplar2);
		exemplarService.inserir(exemplar3);
		exemplarService.inserir(exemplar4);
		
		Exemplar exemplarObtido = exemplarService.buscarPrimeiroExemplarPorEdicaoEStatus(edicao1.getIdEdicao(), StatusExemplar.DISPONIVEL);
		
		Assertions.assertEquals(EstadoFisico.BOM, exemplarObtido.getEstadoFisico());
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, exemplarObtido.getStatus());
		Assertions.assertEquals(edicao1.getIdEdicao(), exemplarObtido.getEdicao().getIdEdicao());
	}
	
	@Test
	void deveCadastrarExemplares() {
		Edicao edicao = new Edicao(
				TipoCapa.DURA,
				100,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2025, 10, 10),
				null,
				null,
				null,
				null);
		
		edicaoRepository.save(edicao);
		
		ExemplarCreateDTO exemplarCreateDTO = new ExemplarCreateDTO(
				EstadoFisico.BOM,
				3,
				edicao.getIdEdicao());
		
		List<Exemplar> exemplares = exemplarService.cadastrarExemplares(exemplarCreateDTO);
		
		Assertions.assertEquals(3, exemplares.size());
		
		for (Exemplar exemplar : exemplares) {
			Assertions.assertEquals(EstadoFisico.BOM, exemplar.getEstadoFisico());
			Assertions.assertEquals(TipoCapa.DURA, exemplar.getEdicao().getTipoCapa());
			Assertions.assertEquals(100, exemplar.getEdicao().getQtdPaginas());
			Assertions.assertEquals(TamanhoEdicao.MEDIO, exemplar.getEdicao().getTamanho());
			Assertions.assertEquals(LocalDate.of(2025, 10, 10), exemplar.getEdicao().getDtPublicacao());
		}
	}
	
	@Test
	void deveAtualizarExemplar() {
		Edicao edicao1 = new Edicao(
				TipoCapa.DURA,
				100,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2025, 10, 10),
				null,
				null,
				null,
				null);
		
		Edicao edicao2 = new Edicao(
				TipoCapa.MOLE,
				2000,
				TamanhoEdicao.GRANDE,
				ClassificacaoIndicativa.C18,
				LocalDate.of(2024, 9, 9),
				null,
				null,
				null,
				null);
		
		edicaoRepository.save(edicao1);
		edicaoRepository.save(edicao2);
		
		Exemplar exemplar = new Exemplar(EstadoFisico.BOM, edicao1);
		
		exemplarService.inserir(exemplar);
		
		ExemplarUpdateDTO exemplarUpdateDTO = new ExemplarUpdateDTO(
				EstadoFisico.RUIM,
				edicao2.getIdEdicao());
		
		Exemplar exemplarAtualizado = exemplarService.atualizar(exemplar.getIdExemplar(), exemplarUpdateDTO);
		
		Assertions.assertEquals(EstadoFisico.RUIM, exemplarAtualizado.getEstadoFisico());
		Assertions.assertEquals(TipoCapa.MOLE, exemplarAtualizado.getEdicao().getTipoCapa());
		Assertions.assertEquals(2000, exemplarAtualizado.getEdicao().getQtdPaginas());
		Assertions.assertEquals(TamanhoEdicao.GRANDE, exemplarAtualizado.getEdicao().getTamanho());
		Assertions.assertEquals(LocalDate.of(2024, 9, 9), exemplarAtualizado.getEdicao().getDtPublicacao());
	}
}

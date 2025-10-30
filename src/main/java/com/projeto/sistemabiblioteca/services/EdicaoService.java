package com.projeto.sistemabiblioteca.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.EdicaoDTO;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EdicaoService {

	private EdicaoRepository edicaoRepository;
	private TituloService tituloService;
	private EditoraService editoraService;
	private IdiomaService idiomaService;

	public EdicaoService(
			EdicaoRepository edicaoRepository,
			TituloService tituloService,
			EditoraService editoraService,
			IdiomaService idiomaService
	) {
		this.edicaoRepository = edicaoRepository;
		this.tituloService = tituloService;
		this.editoraService = editoraService;
		this.idiomaService = idiomaService;
	}
	
	public List<Edicao> buscarTodos() {
		return edicaoRepository.findAll();
	}
	
	public List<Edicao> buscarTodosComStatusIgualA(StatusAtivo status) {
		return edicaoRepository.findAllByStatusEquals(status);
	}
	
	public List<Edicao> buscarTodosComAutorComNomeContendo(String nome) {
		return edicaoRepository.findAllByTituloAutoresNomeContainingIgnoreCase(nome);
	}
	
	public List<Edicao> buscarTodosComTituloComNomeContendo(String nome) {
		return edicaoRepository.findAllByTituloNomeContainingIgnoreCase(nome);
	}
	
	public List<Edicao> buscarTodosComCategoriaComIdIgualA(Long id) {
		return edicaoRepository.findAllByTituloCategoriasIdCategoria(id);
	}
	
	public Edicao buscarPorId(Long id) {
		Optional<Edicao> edicao = edicaoRepository.findById(id);
		if (edicao.isEmpty()) {
			throw new EntityNotFoundException("Erro: edição com id correspondente não foi encontrada.");
		}
		return edicao.get();
	}
	
	@Transactional
	public Edicao cadastrarEdicao(EdicaoDTO edicaoDTO, String imagemUrl) {
		Titulo titulo = tituloService.buscarPorId(edicaoDTO.tituloId());
		Editora editora = editoraService.buscarPorId(edicaoDTO.editoraId());
		Idioma idioma = idiomaService.buscarPorId(edicaoDTO.idiomaId());
		
		if (titulo.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalArgumentException("Erro: não é possível associar uma edição a um título com status inativo.");
		}
		if (editora.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalArgumentException("Erro: não é possível associar uma edição a uma editora com status inativo.");
		}
		if (idioma.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalArgumentException("Erro: não é possível associar uma edição a uma idioma com status inativo.");
		}

		Edicao edicao = new Edicao(
				edicaoDTO.tipoCapa(),
				edicaoDTO.qtdPaginas(),
				edicaoDTO.tamanho(),
				edicaoDTO.classificacao(),
				edicaoDTO.dtPublicacao(),
				imagemUrl,
				titulo,
				editora,
				idioma
		);
		
		return inserir(edicao);
	}
	
	public Edicao inserir(Edicao edicao) {
		return edicaoRepository.save(edicao);
	}
	
	public void inativar(Long id) {
		Edicao edicao = buscarPorId(id);
		if (edicao.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: edição já está inativa.");
		}
		edicao.inativar();
		edicaoRepository.save(edicao);
	}
	
	@Transactional
	public Edicao atualizar(Long id, EdicaoDTO edicaoDTO, String imagemUrl) {
		Edicao edicao1 = buscarPorId(id);
		
		Titulo titulo;
		Editora editora;
		Idioma idioma;
		if (edicao1.getTitulo().getIdTitulo().equals(edicaoDTO.tituloId())) {
			titulo = edicao1.getTitulo();
		}
		else {
			titulo = tituloService.buscarPorId(edicaoDTO.tituloId());
			
			if (titulo.getStatusAtivo() == StatusAtivo.INATIVO) {
				throw new IllegalArgumentException("Erro: não é possível associar uma edição a um título com status inativo ao atualizar.");
			}
		}
		
		if (edicao1.getEditora().getIdEditora().equals(edicaoDTO.editoraId())) {
			editora = edicao1.getEditora();
		}
		else {
			editora = editoraService.buscarPorId(edicaoDTO.editoraId());
			
			if (editora.getStatusAtivo() == StatusAtivo.INATIVO) {
				throw new IllegalArgumentException("Erro: não é possível associar uma edição a uma editora com status inativo ao atualizar.");
			}
		}
		
		if (edicao1.getIdioma().getIdIdioma().equals(edicaoDTO.idiomaId())) {
			idioma = edicao1.getIdioma();
		}
		else {
			idioma = idiomaService.buscarPorId(edicaoDTO.idiomaId());
			
			if (idioma.getStatusAtivo() == StatusAtivo.INATIVO) {
				throw new IllegalArgumentException("Erro: não é possível associar uma edição a um idioma com status inativo ao atualizar.");
			}
		}
		
		if (imagemUrl != null) {
			try {
				String imagemUrlAntiga = edicao1.getImagemUrl();
				if (imagemUrlAntiga != null) {
					String nomeArquivo = Paths.get(new URI(imagemUrlAntiga).getPath()).getFileName().toString();
					
					Path caminho = Paths.get("imagens").resolve(nomeArquivo);
					
					Files.deleteIfExists(caminho);
				}
			}
			catch (IOException | URISyntaxException e) {
				throw new RuntimeException("Erro: não foi possível deletar a imagem antiga.");
			}
		}
		else {
			imagemUrl = edicao1.getImagemUrl();
		}
		
		Edicao edicao2 = new Edicao(
				edicaoDTO.tipoCapa(),
				edicaoDTO.qtdPaginas(),
				edicaoDTO.tamanho(),
				edicaoDTO.classificacao(),
				edicaoDTO.dtPublicacao(),
				imagemUrl,
				titulo,
				editora,
				idioma
		);
		
		atualizarDados(edicao1, edicao2);
		return edicaoRepository.save(edicao1);
	}
	
	private void atualizarDados(Edicao edicao1, Edicao edicao2) {
		edicao1.setTipoCapa(edicao2.getTipoCapa());
		edicao1.setQtdPaginas(edicao2.getQtdPaginas());
		edicao1.setTamanho(edicao2.getTamanho());
		edicao1.setClassificacao(edicao2.getClassificacao());
		edicao1.setDtPublicacao(edicao2.getDtPublicacao());
		edicao1.setImagemUrl(edicao2.getImagemUrl());
		edicao1.setTitulo(edicao2.getTitulo());
		edicao1.setEditora(edicao2.getEditora());
		edicao1.setIdioma(edicao2.getIdioma());
	}
}

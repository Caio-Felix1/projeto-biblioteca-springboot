package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.DTOs.EdicaoCreateDTO;
import com.projeto.sistemabiblioteca.repositories.EditoraRepository;
import com.projeto.sistemabiblioteca.repositories.IdiomaRepository;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;
import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EdicaoService {

	private EdicaoRepository edicaoRepository;
	private TituloRepository tituloRepository;
	private EditoraRepository editoraRepository;
	private IdiomaRepository idiomaRepository;

	public EdicaoService(
			EdicaoRepository edicaoRepository,
			TituloRepository tituloRepository,
			EditoraRepository editoraRepository,
			IdiomaRepository idiomaRepository
	) {
		this.edicaoRepository = edicaoRepository;
		this.tituloRepository = tituloRepository;
		this.editoraRepository = editoraRepository;
		this.idiomaRepository = idiomaRepository;
	}
	
	public List<Edicao> buscarTodos() {
		return edicaoRepository.findAll();
	}
	
	public List<Edicao> buscarTodosComStatusIgualA(StatusAtivo status) {
		return edicaoRepository.findAllByStatusEquals(status);
	}
	
	public Edicao buscarPorId(Long id) {
		Optional<Edicao> edicao = edicaoRepository.findById(id);
		if (edicao.isEmpty()) {
			throw new EntityNotFoundException("Erro: edição com id correspondente não foi encontrada.");
		}
		return edicao.get();
	}

	public Edicao inserir(EdicaoCreateDTO dto) {
		var titulo = tituloRepository.findById(dto.tituloId())
				.orElseThrow(() -> new RuntimeException("Título não encontrado"));
		var editora = editoraRepository.findById(dto.editoraId())
				.orElseThrow(() -> new RuntimeException("Editora não encontrada"));
		var idioma = idiomaRepository.findById(dto.idiomaId())
				.orElseThrow(() -> new RuntimeException("Idioma não encontrado"));

		var edicao = new Edicao(
				dto.tipoCapa(),
				dto.qtdPaginas(),
				dto.tamanho(),
				dto.classificacao(),
				dto.dtPublicacao(),
				titulo,
				editora,
				idioma
		);

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
	
	public Edicao atualizar(Long id, Edicao edicao2) {
		Edicao edicao1 = buscarPorId(id);
		atualizarDados(edicao1, edicao2);
		return edicaoRepository.save(edicao1);
	}
	
	private void atualizarDados(Edicao edicao1, Edicao edicao2) {
		edicao1.setTipoCapa(edicao2.getTipoCapa());
		edicao1.setQtdPaginas(edicao2.getQtdPaginas());
		edicao1.setTamanho(edicao2.getTamanho());
		edicao1.setIdioma(edicao2.getIdioma());
		edicao1.setClassificacao(edicao2.getClassificacao());
		edicao1.setDtPublicacao(edicao2.getDtPublicacao());
		edicao1.setTitulo(edicao2.getTitulo());
		edicao1.setEditora(edicao2.getEditora());
	}
}

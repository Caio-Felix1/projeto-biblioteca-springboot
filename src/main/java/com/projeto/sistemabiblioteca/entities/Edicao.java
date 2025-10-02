package com.projeto.sistemabiblioteca.entities;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;
import com.projeto.sistemabiblioteca.entities.interfaces.Ativavel;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "edicao")
public class Edicao implements Ativavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEdicao;

	@Enumerated(EnumType.STRING)
	private TipoCapa tipoCapa;
	
	private int qtdPaginas;
	
	@Enumerated(EnumType.STRING)
	private TamanhoEdicao tamanho;
	
	@Enumerated(EnumType.STRING)
	private ClassificacaoIndicativa classificacao;
	
	private LocalDate dtPublicacao;
	
	@Enumerated(EnumType.STRING)
	private StatusAtivo status;
	
	@ManyToOne
	@JoinColumn(name = "id_titulo")
	private Titulo titulo;
	
	@ManyToOne
	@JoinColumn(name = "id_editora")
	private Editora editora;
	
	@ManyToOne
	@JoinColumn(name = "id_idioma")
	private Idioma idioma;
	
	protected Edicao() {
		
	}

	public Edicao(TipoCapa tipoCapa, int qtdPaginas, TamanhoEdicao tamanho,  ClassificacaoIndicativa classificacao, 
			LocalDate dtPublicacao, Titulo titulo, Editora editora, Idioma idioma) {
		this.tipoCapa = tipoCapa;
		setQtdPaginas(qtdPaginas);
		this.tamanho = tamanho;
		this.classificacao = classificacao;
		this.dtPublicacao = dtPublicacao;
		this.titulo = titulo;
		this.editora = editora;
		this.idioma = idioma;
		ativar();
	}
	
	public Long getIdEdicao() {
		return idEdicao;
	}

	public TipoCapa getTipoCapa() {
		return tipoCapa;
	}

	public void setTipoCapa(TipoCapa tipoCapa) {
		this.tipoCapa = tipoCapa;
	}

	public int getQtdPaginas() {
		return qtdPaginas;
	}

	public void setQtdPaginas(int qtdPaginas) {
		if (qtdPaginas <= 0) {
			throw new IllegalArgumentException("Erro: Número de páginas não pode ser menor ou igual a 0.");
		}
		this.qtdPaginas = qtdPaginas;
	}

	public TamanhoEdicao getTamanho() {
		return tamanho;
	}

	public void setTamanho(TamanhoEdicao tamanho) {
		this.tamanho = tamanho;
	}

	public ClassificacaoIndicativa getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoIndicativa classificacao) {
		this.classificacao = classificacao;
	}

	public LocalDate getDtPublicacao() {
		return dtPublicacao;
	}

	public void setDtPublicacao(LocalDate dtPublicacao) {
		this.dtPublicacao = dtPublicacao;
	}

	public Titulo getTitulo() {
		return titulo;
	}

	public void setTitulo(Titulo titulo) {
		this.titulo = titulo;
	}

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}
	
	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}
	
	@Override
	public StatusAtivo getStatusAtivo() {
		return status;
	}

	@Override
	public void ativar() {
		status = StatusAtivo.ATIVO;
	}

	@Override
	public void inativar() {
		status = StatusAtivo.INATIVO;
	}
}

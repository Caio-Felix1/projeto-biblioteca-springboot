package com.projeto.sistemabiblioteca.dashboard.dimensions;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "dim_edicao")
@AttributeOverrides({
	@AttributeOverride(name = "surrogateKey", column = @Column(name = "sk_edicao")),
	@AttributeOverride(name = "idNatural", column = @Column(name = "id_natural_edicao"))
})
public class DimEdicao {
	
	private String descricaoEdicao;
	private TipoCapa tipoCapa;
	private int qtdPaginas;
	private TamanhoEdicao tamanho;
	private ClassificacaoIndicativa classificacao;
	private LocalDate dtPublicacao;
	private StatusAtivo status;
	
	protected DimEdicao() {
		super();
	}
	
	public DimEdicao(Edicao edicao) {
		descricaoEdicao = edicao.getDescricaoEdicao();
		tipoCapa = edicao.getTipoCapa();
		qtdPaginas = edicao.getQtdPaginas();
		tamanho = edicao.getTamanho();
		classificacao = edicao.getClassificacao();
		dtPublicacao = edicao.getDtPublicacao();
		status = edicao.getStatusAtivo();
	}

	public String getDescricaoEdicao() {
		return descricaoEdicao;
	}

	public void setDescricaoEdicao(String descricaoEdicao) {
		this.descricaoEdicao = descricaoEdicao;
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

	public StatusAtivo getStatus() {
		return status;
	}

	public void setStatus(StatusAtivo status) {
		this.status = status;
	}
}

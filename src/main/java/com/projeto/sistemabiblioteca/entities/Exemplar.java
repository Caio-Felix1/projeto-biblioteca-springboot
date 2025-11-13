package com.projeto.sistemabiblioteca.entities;

import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;

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
@Table(name = "exemplar")
public class Exemplar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idExemplar;
	
	@Enumerated(EnumType.STRING)
	private StatusExemplar status;
	
	@Enumerated(EnumType.STRING)
	private EstadoFisico estadoFisico;
	
	private String motivoSolicitacaoExclusao;
	
	@ManyToOne
	@JoinColumn(name = "id_edicao")
	private Edicao edicao;
	
	protected Exemplar() {
		
	}
	
	public Exemplar(EstadoFisico estadoFisico, Edicao edicao) {
		status = StatusExemplar.DISPONIVEL;
		this.estadoFisico = estadoFisico;
		this.edicao = edicao;
	}
	
	public Long getIdExemplar() {
		return idExemplar;
	}

	public StatusExemplar getStatus() {
		return status;
	}

	public EstadoFisico getEstadoFisico() {
		return estadoFisico;
	}
	
	public void setEstadoFisico(EstadoFisico estadoFisico) {
		this.estadoFisico = estadoFisico;
	}
	
	public String getMotivoSolicitacaoExclusao() {
		return motivoSolicitacaoExclusao;
	}

	public void setMotivoSolicitacaoExclusao(String motivoSolicitacaoExclusao) {
		this.motivoSolicitacaoExclusao = motivoSolicitacaoExclusao;
	}

	public Edicao getEdicao() {
		return edicao;
	}

	public void setEdicao(Edicao edicao) {
		this.edicao = edicao;
	}
	
	public void alugar() {
		if (status != StatusExemplar.DISPONIVEL) {
			throw new IllegalStateException("Erro: o exemplar está indisponível.");
		}
		
		status = StatusExemplar.ALUGADO;
	}
	
	public void devolver() {
		if (status != StatusExemplar.ALUGADO && status != StatusExemplar.PERDIDO) {
			throw new IllegalStateException("Erro: o exemplar não está alugado ou perdido. Não é possível devolver.");
		}
		
		status = StatusExemplar.DISPONIVEL;
	}
	
	public void registrarPerda() {
		if (status == StatusExemplar.PERDIDO) {
			throw new IllegalStateException("Erro: já foi registrado a perda do exemplar.");
		}
		if (status == StatusExemplar.REMOVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi removido. Não é possível registrar a perda.");
		}
		
		status = StatusExemplar.PERDIDO;
	}
	
	public void solicitarExclusao() {
		if (status == StatusExemplar.EM_ANALISE_EXCLUSAO) {
			throw new IllegalStateException("Erro: já foi solicitado a exclusão do exemplar.");
		}
		if (status != StatusExemplar.DISPONIVEL) {
			throw new IllegalStateException("Erro: o exemplar não está disponível. Não é possível solicitar a exclusão.");
		}
		
		status = StatusExemplar.EM_ANALISE_EXCLUSAO;
	}
	
	public void rejeitarExclusao() {
		if (status != StatusExemplar.EM_ANALISE_EXCLUSAO) {
			throw new IllegalStateException("Erro: o exemplar não está em análise para exclusão.");
		}
		
		status = StatusExemplar.DISPONIVEL;
	}
	
	public void remover() {
		if (status != StatusExemplar.DISPONIVEL && status != StatusExemplar.EM_ANALISE_EXCLUSAO) {
			throw new IllegalStateException("Erro: o exemplar não está disponível ou em análise para exclusão. Não é possível remover.");
		}
		
		status = StatusExemplar.REMOVIDO;
	}
}

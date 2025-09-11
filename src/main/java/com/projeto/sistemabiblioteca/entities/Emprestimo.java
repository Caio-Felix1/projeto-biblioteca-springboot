package com.projeto.sistemabiblioteca.entities;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

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
@Table(name = "tb_emprestimo")
public class Emprestimo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate dtEmprestimo;
	private LocalDate dtDevolucao;
	
	@Enumerated(EnumType.STRING)
	private StatusEmprestimo status;
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento statusPagamento;
	
	@ManyToOne
	@JoinColumn(name = "id_exemplar")
	private Exemplar exemplar;
	
	public Emprestimo() {
		
	}

	public Emprestimo(LocalDate dtEmprestimo, LocalDate dtDevolucao, StatusEmprestimo status,
			StatusPagamento statusPagamento, Exemplar exemplar) {
		this.dtEmprestimo = dtEmprestimo;
		setDtDevolucao(dtDevolucao);
		this.status = status;
		this.statusPagamento = statusPagamento;
		this.exemplar = exemplar;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getDtEmprestimo() {
		return dtEmprestimo;
	}

	public void setDtEmprestimo(LocalDate dtEmprestimo) {
		this.dtEmprestimo = dtEmprestimo;
	}

	public LocalDate getDtDevolucao() {
		return dtDevolucao;
	}

	public void setDtDevolucao(LocalDate dtDevolucao) {
		if (dtDevolucao.isBefore(dtEmprestimo)) {
			throw new IllegalArgumentException("Erro: A data de devolução é anterior a data de empréstimo.");
		}
		this.dtDevolucao = dtDevolucao;
	}

	public StatusEmprestimo getStatus() {
		return status;
	}

	public void setStatus(StatusEmprestimo status) {
		this.status = status;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(StatusPagamento statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public Exemplar getExemplar() {
		return exemplar;
	}

	public void setExemplar(Exemplar exemplar) {
		this.exemplar = exemplar;
	}
}

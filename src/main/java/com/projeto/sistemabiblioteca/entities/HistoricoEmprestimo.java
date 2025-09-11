package com.projeto.sistemabiblioteca.entities;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_historico_emprestimo")
public class HistoricoEmprestimo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private long idPessoa;
	private String nomePessoa;
	private long idExemplar;
	private String tituloExemplar;
	private LocalDate dtEmprestimo;
	private LocalDate dtDevolucao;
	private StatusEmprestimo status;
	private StatusPagamento statusPagamento;
	
	public HistoricoEmprestimo() {
		
	}

	public HistoricoEmprestimo(long idPessoa, String nomePessoa, long idExemplar, String tituloExemplar,
			LocalDate dtEmprestimo, LocalDate dtDevolucao, StatusEmprestimo status, StatusPagamento statusPagamento) {
		this.idPessoa = idPessoa;
		this.nomePessoa = nomePessoa;
		this.idExemplar = idExemplar;
		this.tituloExemplar = tituloExemplar;
		this.dtEmprestimo = dtEmprestimo;
		this.dtDevolucao = dtDevolucao;
		this.status = status;
		this.statusPagamento = statusPagamento;
	}

	public Long getId() {
		return id;
	}

	public long getIdPessoa() {
		return idPessoa;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public long getIdExemplar() {
		return idExemplar;
	}

	public String getTituloExemplar() {
		return tituloExemplar;
	}

	public LocalDate getDtEmprestimo() {
		return dtEmprestimo;
	}

	public LocalDate getDtDevolucao() {
		return dtDevolucao;
	}

	public StatusEmprestimo getStatus() {
		return status;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}
}

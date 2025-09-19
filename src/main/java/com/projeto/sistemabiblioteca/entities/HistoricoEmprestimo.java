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
import jakarta.persistence.Table;

@Entity
@Table(name = "historico_emprestimo")
public class HistoricoEmprestimo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idHistoricoEmprestimo;
	
	private long idPessoa;
	private String nomePessoa;
	private long idExemplar;
	private String tituloExemplar;
	private LocalDate dtInicioEmprestimo;
	private LocalDate dtRetiradaExemplar;
	private LocalDate dtDevolucaoPrevista;
	private LocalDate dtDevolvidoExemplar;
	
	@Enumerated(EnumType.STRING)
	private StatusEmprestimo status;
	
	private double multa;
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento statusPagamento;
	
	public HistoricoEmprestimo() {
		
	}

	public HistoricoEmprestimo(long idPessoa, String nomePessoa, long idExemplar, String tituloExemplar, LocalDate dtInicioEmprestimo, LocalDate dtRetiradaExemplar, 
			LocalDate dtDevolucaoPrevista, LocalDate dtDevolvidoExemplar, StatusEmprestimo status, double multa, StatusPagamento statusPagamento) {
		this.idPessoa = idPessoa;
		this.nomePessoa = nomePessoa;
		this.idExemplar = idExemplar;
		this.tituloExemplar = tituloExemplar;
		this.dtInicioEmprestimo = dtInicioEmprestimo;
		this.dtRetiradaExemplar = dtRetiradaExemplar;
		this.dtDevolucaoPrevista = dtDevolucaoPrevista;
		this.dtDevolvidoExemplar = dtDevolvidoExemplar;
		this.status = status;
		this.multa = multa;
		this.statusPagamento = statusPagamento;
	}

	public Long getIdHistoricoEmprestimo() {
		return idHistoricoEmprestimo;
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

	public LocalDate getDtInicioEmprestimo() {
		return dtInicioEmprestimo;
	}
	
	public LocalDate getDtRetiradaExemplar() {
		return dtRetiradaExemplar;
	}

	public LocalDate getDtDevolucaoPrevista() {
		return dtDevolucaoPrevista;
	}
	
	public LocalDate getDtDevolvidoExemplar() {
		return dtDevolvidoExemplar;
	}

	public StatusEmprestimo getStatus() {
		return status;
	}
	
	public double getMulta() {
		return multa;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}
}

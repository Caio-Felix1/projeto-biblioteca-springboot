package com.projeto.sistemabiblioteca.dashboard.facts;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
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
@Table(name = "fato_emprestimo")
public class FatoEmprestimo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long skFato;
	
	private Long idNaturalEmprestimo;
	private Long idNaturalMulta;
	
	private LocalDate dtInicioEmprestimo;
	private LocalDate dtSeparacaoExemplar;
	private LocalDate dtRetiradaExemplar;
	private LocalDate dtDevolucaoPrevista;
	private LocalDate dtDevolvidoExemplar;
	
	@Enumerated(EnumType.STRING)
	private StatusEmprestimo status;
	
	private int diasAtraso;
	
	private double valorMulta;
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento statusPagamento;
	
	private Long skCliente;
	private Long skExemplar;
	private Long skEdicao;
	private Long skIdioma;
	private Long skEditora;
	private Long skTitulo;
	
	protected FatoEmprestimo() {
		
	}
	
	public FatoEmprestimo(Emprestimo emprestimo, Long skCliente, Long skExemplar, Long skEdicao,
			Long skIdioma, Long skEditora, Long skTitulo) {
		idNaturalEmprestimo = emprestimo.getIdEmprestimo();
		idNaturalMulta = emprestimo.getMulta().getIdMulta();
		dtInicioEmprestimo = emprestimo.getDtInicioEmprestimo();
		dtSeparacaoExemplar = emprestimo.getDtSeparacaoExemplar();
		dtRetiradaExemplar = emprestimo.getDtRetiradaExemplar();
		dtDevolucaoPrevista = emprestimo.getDtDevolucaoPrevista();
		dtDevolvidoExemplar = emprestimo.getDtDevolvidoExemplar();
		status = emprestimo.getStatus();
		diasAtraso = (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) ? emprestimo.calcularDiasDeAtraso(emprestimo.getDtDevolvidoExemplar()) : 0;
		valorMulta = emprestimo.getMulta().getValor();
		statusPagamento = emprestimo.getMulta().getStatusPagamento();
		
		this.skCliente = skCliente;
		this.skExemplar = skExemplar;
		this.skEdicao = skEdicao;
		this.skIdioma = skIdioma;
		this.skEditora = skEditora;
		this.skTitulo = skTitulo;
	}

	public Long getSkFato() {
		return skFato;
	}

	public void setSkFato(Long skFato) {
		this.skFato = skFato;
	}

	public Long getIdNaturalEmprestimo() {
		return idNaturalEmprestimo;
	}

	public void setIdNaturalEmprestimo(Long idNaturalEmprestimo) {
		this.idNaturalEmprestimo = idNaturalEmprestimo;
	}

	public Long getIdNaturalMulta() {
		return idNaturalMulta;
	}

	public void setIdNaturalMulta(Long idNaturalMulta) {
		this.idNaturalMulta = idNaturalMulta;
	}

	public LocalDate getDtInicioEmprestimo() {
		return dtInicioEmprestimo;
	}

	public void setDtInicioEmprestimo(LocalDate dtInicioEmprestimo) {
		this.dtInicioEmprestimo = dtInicioEmprestimo;
	}

	public LocalDate getDtSeparacaoExemplar() {
		return dtSeparacaoExemplar;
	}

	public void setDtSeparacaoExemplar(LocalDate dtSeparacaoExemplar) {
		this.dtSeparacaoExemplar = dtSeparacaoExemplar;
	}

	public LocalDate getDtRetiradaExemplar() {
		return dtRetiradaExemplar;
	}

	public void setDtRetiradaExemplar(LocalDate dtRetiradaExemplar) {
		this.dtRetiradaExemplar = dtRetiradaExemplar;
	}

	public LocalDate getDtDevolucaoPrevista() {
		return dtDevolucaoPrevista;
	}

	public void setDtDevolucaoPrevista(LocalDate dtDevolucaoPrevista) {
		this.dtDevolucaoPrevista = dtDevolucaoPrevista;
	}

	public LocalDate getDtDevolvidoExemplar() {
		return dtDevolvidoExemplar;
	}

	public void setDtDevolvidoExemplar(LocalDate dtDevolvidoExemplar) {
		this.dtDevolvidoExemplar = dtDevolvidoExemplar;
	}

	public StatusEmprestimo getStatus() {
		return status;
	}

	public void setStatus(StatusEmprestimo status) {
		this.status = status;
	}

	public int getDiasAtraso() {
		return diasAtraso;
	}

	public void setDiasAtraso(int diasAtraso) {
		this.diasAtraso = diasAtraso;
	}

	public double getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(double valorMulta) {
		this.valorMulta = valorMulta;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(StatusPagamento statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public Long getSkCliente() {
		return skCliente;
	}

	public void setSkCliente(Long skCliente) {
		this.skCliente = skCliente;
	}

	public Long getSkExemplar() {
		return skExemplar;
	}

	public void setSkExemplar(Long skExemplar) {
		this.skExemplar = skExemplar;
	}

	public Long getSkEdicao() {
		return skEdicao;
	}

	public void setSkEdicao(Long skEdicao) {
		this.skEdicao = skEdicao;
	}

	public Long getSkIdioma() {
		return skIdioma;
	}

	public void setSkIdioma(Long skIdioma) {
		this.skIdioma = skIdioma;
	}

	public Long getSkEditora() {
		return skEditora;
	}

	public void setSkEditora(Long skEditora) {
		this.skEditora = skEditora;
	}

	public Long getSkTitulo() {
		return skTitulo;
	}

	public void setSkTitulo(Long skTitulo) {
		this.skTitulo = skTitulo;
	}
}

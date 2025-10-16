package com.projeto.sistemabiblioteca.entities;

import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "multa")
public class Multa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idMulta;
	
	private double valor;
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento statusPagamento;
	
	protected Multa() {
		statusPagamento = StatusPagamento.NAO_APLICAVEL;
	}
	
	public static Multa criarMultaVazia() {
		return new Multa();
	}

	public Long getIdMulta() {
		return idMulta;
	}

	public double getValor() {
		return valor;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}
	
	/**
	 * Aplica multa por perda do exemplar.
	 * 
	 * Chama o método calcularMultaPorPerda, atribui o resultado na variável vlor e
	 * muda o statusPagamento para PENDENTE.
	 */
	public void aplicarMultaPorPerda() {
		if (statusPagamento == StatusPagamento.PAGO) {
			throw new IllegalStateException("Erro: a multa já foi paga.");
		}
		if (statusPagamento == StatusPagamento.PERDOADO) {
			throw new IllegalStateException("Erro: a multa já foi perdoada.");
		}
		
		valor = calcularMultaPorPerda();
		statusPagamento = StatusPagamento.PENDENTE;
	}
	
	public double calcularMultaPorPerda() {
		return 50.0;
	}

	/**
	 * Aplica multa por atraso.
	 * 
	 * Chama o método calcularMultaDiaria, atribui o resultado na variável multa e 
	 * muda o statusPagamento para PENDENTE.
	 * 
	 * @param dias
	 */
	public void aplicarMulta(int dias) {
		if (dias <= 0) {
			throw new IllegalArgumentException("Erro: o parãmetro 'dias' deve ser maior que zero.");
		}
		if (statusPagamento == StatusPagamento.PAGO) {
			throw new IllegalStateException("Erro: a multa já foi paga.");
		}
		if (statusPagamento == StatusPagamento.PERDOADO) {
			throw new IllegalStateException("Erro: a multa já foi perdoada.");
		}
		
		double resultado = calcularMultaDiaria(dias);
		if (resultado < valor) {
			throw new IllegalArgumentException("Erro: o valor da multa não pode diminuir.");
		}
		if (resultado >= 50.0) {
			valor = 50.0;
		}
		else {
			valor = resultado;
		} 
		statusPagamento = StatusPagamento.PENDENTE;
	}
	
	public double calcularMultaDiaria(int dias) {
		if (dias <= 0) {
			throw new IllegalArgumentException("Erro: o parãmetro 'dias' deve ser maior que zero.");
		}
		return 1.0 * dias;
	}
	
	public void pagarMulta() {
		if (statusPagamento == StatusPagamento.PAGO) {
			throw new IllegalStateException("Erro: a multa já foi paga.");
		}
		if (statusPagamento == StatusPagamento.PERDOADO) {
			throw new IllegalStateException("Erro: a multa já foi perdoada.");
		}
		if (statusPagamento != StatusPagamento.PENDENTE) {
			throw new IllegalStateException("Erro: não há multa pendente.");
		}
		
		statusPagamento = StatusPagamento.PAGO;
	}
	
	public void perdoarMulta() {
		if (statusPagamento == StatusPagamento.PAGO) {
			throw new IllegalStateException("Erro: a multa já foi paga.");
		}
		if (statusPagamento == StatusPagamento.PERDOADO) {
			throw new IllegalStateException("Erro: a multa já foi perdoada.");
		}
		if (statusPagamento != StatusPagamento.PENDENTE) {
			throw new IllegalStateException("Erro: não há multa pendente.");
		}
		
		statusPagamento = StatusPagamento.PERDOADO;
	}
}

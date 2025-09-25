package com.projeto.sistemabiblioteca.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

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
@Table(name = "emprestimo")
public class Emprestimo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEmprestimo;
	
	private LocalDate dtInicioEmprestimo;
	private LocalDate dtRetiradaExemplar;
	private LocalDate dtDevolucaoPrevista;
	private LocalDate dtDevolvidoExemplar;
	
	@Enumerated(EnumType.STRING)
	private StatusEmprestimo status;
	
	private double multa;
	
	@Enumerated(EnumType.STRING)
	private StatusPagamento statusPagamento;
	
	@ManyToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name = "id_exemplar")
	private Exemplar exemplar;
	
	public Emprestimo() {
		
	}
	
	/**
	 * Cria uma instância de empréstimo com o a data de devolução prevista definida automaticamente
	 * A data de início do empréstimo é definida com a data em que foi instanciado, o status inicia definido 
	 * como RESERVADO e o statusPagamento como NAO_APLICAVEL.
	 * 
	 * @param pessoa
	 * @param exemplar
	 */
	public Emprestimo(Pessoa pessoa, Exemplar exemplar) {
		dtInicioEmprestimo = LocalDate.now();
		dtDevolucaoPrevista = calcularDataDeDevolucao();
		status = StatusEmprestimo.RESERVADO;
		multa = 0.0;
		statusPagamento = StatusPagamento.NAO_APLICAVEL;
		this.pessoa = pessoa;
		this.exemplar = exemplar;
	}
	
	/**
	 * Cria uma instância do empréstimo com data de devolução prevista definida manualmente.
	 * A data de início do empréstimo é definida com a data em que foi instanciado, o status inicia definido 
	 * como RESERVADO e o statusPagamento como NAO_APLICAVEL.
	 * 
	 * @param dtDevolucaoPrevista
	 * @param pessoa
	 * @param exemplar
	 */
	public Emprestimo(LocalDate dtDevolucaoPrevista, Pessoa pessoa, Exemplar exemplar) {
		dtInicioEmprestimo = LocalDate.now();
		setDtDevolucaoPrevista(dtDevolucaoPrevista);
		status = StatusEmprestimo.RESERVADO;
		multa = 0.0;
		statusPagamento = StatusPagamento.NAO_APLICAVEL;
		this.pessoa = pessoa;
		this.exemplar = exemplar;
	}

	public Long getIdEmprestimo() {
		return idEmprestimo;
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

	public void setDtDevolucaoPrevista(LocalDate dtDevolucaoPrevista) {
		if (dtDevolucaoPrevista.isBefore(dtInicioEmprestimo)) {
			throw new IllegalArgumentException("Erro: a data de devolução prevista é anterior à data de início do empréstimo.");
		}
		if (dtDevolucaoPrevista.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Erro: a data de devolução prevista não pode ser no passado.");
		}
		if (ChronoUnit.DAYS.between(dtInicioEmprestimo, dtDevolucaoPrevista) >  90) {
			throw new IllegalArgumentException("Erro: a data de devolução inserida ultrapassou o prazo máximo de 90 dias.");
		}
		this.dtDevolucaoPrevista = dtDevolucaoPrevista;
	}
	
	public LocalDate getDtDevolvidoExemplar() {
		return dtDevolvidoExemplar;
	}

	public StatusEmprestimo getStatus() {
		return status;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}
	
	public double getMulta() {
		return multa;
	}

	public Exemplar getExemplar() {
		return exemplar;
	}

	public void setExemplar(Exemplar exemplar) {
		this.exemplar = exemplar;
	}
	
	/**
	 * Registra a retirada do Exemplar.
	 * 
	 * Muda o status para EM_ANDAMENTO e atribui a data atual à variável dtRetiradaExemplar.
	 */
	public void retirarExemplar() {
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo foi cancelado anteriormente. Não é possível retirar exemplar.");
		}
		if (status != StatusEmprestimo.RESERVADO) {
			throw new IllegalStateException("Erro: o exemplar já foi retirado.");
		}
		
		status = StatusEmprestimo.EM_ANDAMENTO;
		dtRetiradaExemplar = LocalDate.now();
	}
	
	/**
	 * Registra a devolução do exemplar.
	 * 
	 * Muda o status para DEVOLVIDO, atribui a data atual à variável dtDevolvidoExemplar
	 * e muda o status do exemplar para DISPONIVEL.
	 */
	public void devolverExemplar() {
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo foi cancelado anteriormente. Não é possível devolver exemplar.");
		}
		if (status == StatusEmprestimo.RESERVADO) {
			throw new IllegalStateException("Erro: o exemplar ainda não foi retirado.");
		}
		if (status == StatusEmprestimo.EXEMPLAR_PERDIDO) {
			throw new IllegalStateException("Erro: o exemplar foi perdido. Não é mais possível devolver.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		
		status = StatusEmprestimo.DEVOLVIDO;
		dtDevolvidoExemplar = LocalDate.now();
		exemplar.devolver();
	}
	
	/**
	 * Registra a perda do exemplar.
	 * 
	 * Muda o status para EXEMPLAR_PERDIDO, chama o método aplicarMultaPorPerda e 
	 * muda o status do exemplar para PERDIDO.
	 */
	public void registrarPerdaDoExemplar() {
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo foi cancelado anteriormente. Não é possível registrar a perda do exemplar.");
		}
		if (status == StatusEmprestimo.RESERVADO) {
			throw new IllegalStateException("Erro: o exemplar ainda não foi retirado.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		if (status == StatusEmprestimo.EXEMPLAR_PERDIDO) {
			throw new IllegalStateException("Erro: já foi registrado a perda do exemplar.");
		}
		
		status = StatusEmprestimo.EXEMPLAR_PERDIDO;
		aplicarMultaPorPerda();
		exemplar.registrarPerda();
	}
	
	/**
	 * Registra o atraso do empréstimo.
	 * 
	 * Muda o status para ATRASADO, chama o método aplicarMulta e chama o método calcularMultaDiaria.
	 */
	public void registrarAtraso() {
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo já foi cancelado.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		if (status == StatusEmprestimo.EXEMPLAR_PERDIDO) {
			throw new IllegalStateException("Erro: o exemplar foi perdido. Não é possível registrar o atraso.");
		}
		if (calcularDiasDeAtraso() <= 0) {
			throw new IllegalStateException("Erro: o empréstimo ainda não está atrasado.");
		}
		if (status == StatusEmprestimo.ATRASADO) {
			throw new IllegalStateException("Erro: já foi registrado o atraso.");
		}
		
		status = StatusEmprestimo.ATRASADO;
		aplicarMulta();
		calcularMultaDiaria();
	}
	
	public void cancelarReserva() {
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo já foi cancelado.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		if (status != StatusEmprestimo.RESERVADO) {
			throw new IllegalStateException("Erro: o exemplar já foi retirado.");
		}
		
		status = StatusEmprestimo.CANCELADO;
	}
	
	/**
	 * Aplica multa por perda do exemplar
	 * 
	 * Chama o método calcularMultaPorPerda, atribui o resultado na variável multa e
	 * muda o statusPagamento para PENDENTE.
	 */
	public void aplicarMultaPorPerda() {
		multa = calcularMultaPorPerda();
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
	 */
	public void aplicarMulta() {
		multa = calcularMultaDiaria();
		statusPagamento = StatusPagamento.PENDENTE;
	}
	
	public double calcularMultaDiaria() {
		return 1.0 * calcularDiasDeAtraso();
	}
	
	public void pagarMulta() {
		if (statusPagamento != StatusPagamento.PENDENTE) {
			throw new IllegalStateException("Erro: não há multa pendente.");
		}
		
		statusPagamento = StatusPagamento.PAGO;
	}
	
	public void perdoarMulta() {
		if (statusPagamento != StatusPagamento.PENDENTE) {
			throw new IllegalStateException("Erro: não há multa pendente.");
		}
		
		statusPagamento = StatusPagamento.PERDOADO;
	}
	
	public int calcularDiasDeAtraso() {
		if (status != StatusEmprestimo.ATRASADO) {
			throw new IllegalStateException("Erro: não há atraso.");
		}
		
		return (int) ChronoUnit.DAYS.between(dtDevolucaoPrevista, LocalDate.now());
	}
	
	public int calcularDiasDeEmprestimo() {
		if (!Arrays.asList(StatusEmprestimo.RESERVADO, StatusEmprestimo.EM_ANDAMENTO, StatusEmprestimo.ATRASADO).contains(status)) {
			throw new IllegalStateException("Erro: o empréstimo não está ativo.");
		}
		
		return (int) ChronoUnit.DAYS.between(dtInicioEmprestimo, LocalDate.now());
	}
	
	public LocalDate calcularDataDeDevolucao() {
		if (dtDevolucaoPrevista != null) {
			throw new IllegalStateException("Erro: já tem data de devolução prevista.");
		}
		
		int qtdPaginas = exemplar.getEdicao().getQtdPaginas();
		if (qtdPaginas <= 100) {
			return dtInicioEmprestimo.plusDays(9);
		}
		else if (qtdPaginas <= 500) {
			return dtInicioEmprestimo.plusDays(16);
		}
		else if (qtdPaginas <= 1000) {
			return dtInicioEmprestimo.plusDays(23);
		}
		else if (qtdPaginas <= 3000){
			return dtInicioEmprestimo.plusDays(30);
		}
		else {
			return dtInicioEmprestimo.plusDays(37);
		}
	}
	
	public int calcularDiasRestantes() {
		if (!Arrays.asList(StatusEmprestimo.RESERVADO, StatusEmprestimo.EM_ANDAMENTO).contains(status)) {
			throw new IllegalStateException("Erro: O empréstimo não está ativo ou já está atrasado. Não é possível calcular dias restantes.");
		}
		
		return (int) ChronoUnit.DAYS.between(LocalDate.now(), dtDevolucaoPrevista);
	}
}

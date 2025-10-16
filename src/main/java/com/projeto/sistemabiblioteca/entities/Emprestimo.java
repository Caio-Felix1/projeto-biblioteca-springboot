package com.projeto.sistemabiblioteca.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {
	
	private static final int MAXIMO_DIAS = 90;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEmprestimo;
	
	private LocalDate dtInicioEmprestimo;
	private LocalDate dtSeparacaoExemplar;
	private LocalDate dtRetiradaExemplar;
	private LocalDate dtDevolucaoPrevista;
	private LocalDate dtDevolvidoExemplar;
	
	@Enumerated(EnumType.STRING)
	private StatusEmprestimo status;
	
	@ManyToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name = "id_exemplar")
	private Exemplar exemplar;
	
	@OneToOne
	@JoinColumn(name = "id_multa")
	private Multa multa;
	
	protected Emprestimo() {
		
	}
	
	public Emprestimo(LocalDate dtInicioEmprestimo, Pessoa pessoa, Exemplar exemplar, Multa multa) {
		if (dtInicioEmprestimo == null) {
			throw new IllegalArgumentException("Erro: a data de início do empréstimo não pode ser configurada como nula.");
		}
		this.dtInicioEmprestimo = dtInicioEmprestimo;
		status = StatusEmprestimo.RESERVADO;
		this.pessoa = pessoa;
		this.exemplar = exemplar;
		this.multa = multa;
	}

	public Long getIdEmprestimo() {
		return idEmprestimo;
	}

	public LocalDate getDtInicioEmprestimo() {
		return dtInicioEmprestimo;
	}
	
	public LocalDate getDtSeparacaoExemplar() {
		return dtSeparacaoExemplar;
	}

	public LocalDate getDtRetiradaExemplar() {
		return dtRetiradaExemplar;
	}

	public LocalDate getDtDevolucaoPrevista() {
		return dtDevolucaoPrevista;
	}

	private void setDtDevolucaoPrevista(LocalDate dtDevolucaoPrevista) {
		this.dtDevolucaoPrevista = dtDevolucaoPrevista;
	}
	
	public LocalDate getDtDevolvidoExemplar() {
		return dtDevolvidoExemplar;
	}

	public StatusEmprestimo getStatus() {
		return status;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		if (status != StatusEmprestimo.RESERVADO && status != StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar já foi retirado. Não é possível alterar o cliente ligado ao empréstimo.");
		}
		this.pessoa = pessoa;
	}

	public Exemplar getExemplar() {
		return exemplar;
	}
	
	/**
	 * Altera o exemplar ligado ao empréstimo.
	 * 
	 * Muda o status do exemplar anterior para DISPONIVEL e atribui o valor do parâmetro 'exemplar' à variável exemplar.
	 * 
	 * @param exemplar
	 */
	public void setExemplar(Exemplar exemplar) {
		if (status != StatusEmprestimo.RESERVADO && status != StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar já foi retirado. Não é possível alterar o exemplar ligado ao empréstimo.");
		}
		this.exemplar.devolver();
		this.exemplar = exemplar;
	}
	
	public Multa getMulta() {
		return multa;
	}
	
	public void definirDataDevolucaoPrevista(LocalDate dtDevolucaoPrevista, LocalDate hoje) {
		validarDataDevolucaoPrevista(dtDevolucaoPrevista, hoje);
		setDtDevolucaoPrevista(dtDevolucaoPrevista);
	}
	
	private void validarDataDevolucaoPrevista(LocalDate dtDevolucaoPrevista, LocalDate hoje) {
		if (dtDevolucaoPrevista == null) {
			throw new IllegalArgumentException("Erro: a data de devolução prevista não pode ser configurada como nula.");
		}
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (status != StatusEmprestimo.EM_ANDAMENTO) {
			throw new IllegalStateException("Erro: só é possível definir a data de devolução prevista quando o empréstimo estiver em andamento.");
		}
//		if (dtInicioEmprestimo.isAfter(hoje)) {
//			throw new IllegalArgumentException("Erro: a data atual não pode ser anterior à data de início do empréstimo.");
//		} 
		if (this.dtDevolucaoPrevista != null && !dtDevolucaoPrevista.isAfter(this.dtDevolucaoPrevista)) {
			throw new IllegalArgumentException("Erro: a data de devolução prevista nova deve ser posterior à data de devolução prevista anterior.");
		}
//		if (!dtDevolucaoPrevista.isAfter(dtInicioEmprestimo)) {
//			throw new IllegalArgumentException("Erro: a data de devolução prevista deve ser posterior à data de início do empréstimo.");
//		}
		if (!dtDevolucaoPrevista.isAfter(hoje)) {
			throw new IllegalArgumentException("Erro: a data de devolução prevista deve ser posterior à data atual.");
		}
		if (ChronoUnit.DAYS.between(dtInicioEmprestimo, dtDevolucaoPrevista) >  MAXIMO_DIAS) {
			throw new IllegalArgumentException("Erro: a data de devolução inserida ultrapassou o prazo máximo de " + MAXIMO_DIAS + " dias.");
		}
	}
	
	/**
	 * Registra que o Exemplar foi separado para retirada.
	 * 
	 * Muda o status para SEPARADO e atribui o valor do parâmetro 'hoje' à variável dtSeparacaoExemplar.
	 * 
	 * @param hoje
	 */
	public void separarExemplar(LocalDate hoje) {
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (dtInicioEmprestimo.isAfter(hoje)) {
			throw new IllegalArgumentException("Erro: a data de separação do exemplar não pode ser anterior à data de início do empréstimo.");
		}
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo foi cancelado anteriormente. Não é possível retirar exemplar.");
		}
		if (status != StatusEmprestimo.RESERVADO && status != StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar já foi retirado.");
		}
		
		status = StatusEmprestimo.SEPARADO;
		dtSeparacaoExemplar = hoje;
	}

	/**
	 * Registra a retirada do Exemplar com data de devolução prevista inserida manualmente.
	 * 
	 * Muda o status para EM_ANDAMENTO, atribui o valor do parâmetro 'hoje' à variável dtRetiradaExemplar e
	 * o valor do parâmetro 'dtDevolucaoPrevista' à varíavel dtDevolucaoPrevista.
	 * 
	 * @param hoje
	 * @param dtDevolucaoPrevista
	 */
	public void retirarExemplar(LocalDate hoje, LocalDate dtDevolucaoPrevista) {
		if (dtDevolucaoPrevista == null) {
			throw new IllegalArgumentException("Erro: a data de devolução prevista não pode ser configurada como nula.");
		}
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (status == StatusEmprestimo.RESERVADO) {
			throw new IllegalStateException("Erro: o exemplar ainda não foi separado.");
		}
		if (dtSeparacaoExemplar.isAfter(hoje)) {
			throw new IllegalArgumentException("Erro: a data de retirada do exemplar não pode ser anterior à data de separação do exemplar.");
		}
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo foi cancelado anteriormente. Não é possível retirar exemplar.");
		}
		if (status != StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar já foi retirado.");
		}
		
		status = StatusEmprestimo.EM_ANDAMENTO;
		dtRetiradaExemplar = hoje;
		definirDataDevolucaoPrevista(dtDevolucaoPrevista, hoje);
	}
	
	/**
	 * Registra a retirada do Exemplar com data de devolução prevista calculada automaticamente.
	 * 
	 * Chama o método retirarExemplar com dois parâmetros, passando o valor do parâmetro 'hoje' e o 
	 * resultado do cálculo da data de devolução prevista.
	 * 
	 * @param hoje
	 */
	public void retirarExemplar(LocalDate hoje) {
		retirarExemplar(hoje, calcularDataDeDevolucaoPrevista());
	}
	
	/**
	 * Registra a devolução do exemplar.
	 * 
	 * Muda o status para DEVOLVIDO, atribui o valor do parâmetro 'hoje' à variável dtDevolvidoExemplar
	 * e muda o status do exemplar para DISPONIVEL.
	 * 
	 * @param hoje
	 */
	public void devolverExemplar(LocalDate hoje) {
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo foi cancelado anteriormente. Não é possível devolver exemplar.");
		}
		if (status == StatusEmprestimo.RESERVADO || status == StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar ainda não foi retirado.");
		}
		if (dtRetiradaExemplar.isAfter(hoje)) {
			throw new IllegalArgumentException("Erro: a data de devolução do exemplar não pode ser anterior à data de retirada do exemplar.");
		}
		if (status == StatusEmprestimo.EXEMPLAR_PERDIDO) {
			throw new IllegalStateException("Erro: o exemplar foi perdido. Não é mais possível devolver.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		if (status == StatusEmprestimo.ATRASADO && !hoje.isAfter(dtDevolucaoPrevista)) {
			throw new IllegalArgumentException("Erro: a data de devolução do exemplar não pode ser anterior à data de devolução prevista quando foi registrado o atraso.");
		}
		
		status = StatusEmprestimo.DEVOLVIDO;
		dtDevolvidoExemplar = hoje;
		exemplar.devolver();
	}
	
	/**
	 * Registra a perda do exemplar.
	 * 
	 * Muda o status para EXEMPLAR_PERDIDO, chama o método aplicarMultaPorPerda do atributo multa e 
	 * muda o status do exemplar para PERDIDO.
	 */
	public void registrarPerdaDoExemplar() {
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo foi cancelado anteriormente. Não é possível registrar a perda do exemplar.");
		}
		if (status == StatusEmprestimo.RESERVADO || status == StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar ainda não foi retirado.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		if (status == StatusEmprestimo.EXEMPLAR_PERDIDO) {
			throw new IllegalStateException("Erro: já foi registrado a perda do exemplar.");
		}
		
		status = StatusEmprestimo.EXEMPLAR_PERDIDO;
		multa.aplicarMultaPorPerda();
		exemplar.registrarPerda();
	}
	
	/**
	 * Registra o atraso do empréstimo.
	 * 
	 * Muda o status para ATRASADO e chama o método aplicarMulta do atributo multa.
	 * 
	 * @param hoje
	 */
	public void registrarAtraso(LocalDate hoje) {
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo já foi cancelado.");
		}
		if (status == StatusEmprestimo.RESERVADO || status == StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar ainda não foi retirado.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		if (status == StatusEmprestimo.EXEMPLAR_PERDIDO) {
			throw new IllegalStateException("Erro: o exemplar foi perdido. Não é possível registrar o atraso.");
		}
		if (calcularDiasDeAtraso(hoje) <= 0) {
			throw new IllegalStateException("Erro: o empréstimo ainda não está atrasado.");
		}
		if (status == StatusEmprestimo.ATRASADO) {
			throw new IllegalStateException("Erro: já foi registrado o atraso.");
		}
		
		status = StatusEmprestimo.ATRASADO;
		multa.aplicarMulta(calcularDiasDeAtraso(hoje));
	}
	
	public void cancelarReserva() {
		if (status == StatusEmprestimo.CANCELADO) {
			throw new IllegalStateException("Erro: o empréstimo já foi cancelado.");
		}
		if (status == StatusEmprestimo.DEVOLVIDO) {
			throw new IllegalStateException("Erro: o exemplar já foi devolvido.");
		}
		if (status == StatusEmprestimo.RESERVADO) {
			throw new IllegalStateException("Erro: o exemplar ainda não foi separado.");
		}
		if (status != StatusEmprestimo.SEPARADO) {
			throw new IllegalStateException("Erro: o exemplar já foi retirado.");
		}
		
		status = StatusEmprestimo.CANCELADO;
		exemplar.devolver();
	}
	
	public int calcularDiasDeAtraso(LocalDate hoje) {
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (dtDevolucaoPrevista == null) {
			throw new IllegalStateException("Erro: data de devolução prevista não foi definida.");
		}
		
		return (int) Math.max(0, ChronoUnit.DAYS.between(dtDevolucaoPrevista, hoje));
	}
	
	public boolean isPrazoDeRetiradaExpirado(LocalDate hoje, int prazoRetirada) {
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (prazoRetirada <= 0) {
			throw new IllegalArgumentException("Erro: o prazo de retirada deve ser maior que zero.");
		}
		if (dtSeparacaoExemplar == null) {
			throw new IllegalStateException("Erro: data de separação do exemplar não foi definida.");
		}
		if (dtSeparacaoExemplar.isAfter(hoje)) {
			throw new IllegalArgumentException("Erro: data de hoje deve ser igual ou posterior à data de separação do exemplar.");
		}
		
		return ChronoUnit.DAYS.between(dtSeparacaoExemplar, hoje) > prazoRetirada;
	}
	
	public LocalDate calcularDataDeDevolucaoPrevista() {
		if (dtDevolucaoPrevista != null) {
			throw new IllegalStateException("Erro: já tem data de devolução prevista.");
		}
		if (dtSeparacaoExemplar == null) {
			throw new IllegalStateException("Erro: data de separação do exemplar não foi definida.");
		}
		
		int qtdPaginas = exemplar.getEdicao().getQtdPaginas();
		if (qtdPaginas <= 100) {
			return dtSeparacaoExemplar.plusDays(9);
		}
		else if (qtdPaginas <= 500) {
			return dtSeparacaoExemplar.plusDays(16);
		}
		else if (qtdPaginas <= 1000) {
			return dtSeparacaoExemplar.plusDays(23);
		}
		else if (qtdPaginas <= 3000){
			return dtSeparacaoExemplar.plusDays(30);
		}
		else {
			return dtSeparacaoExemplar.plusDays(37);
		}
	}
	
	public int calcularDiasRestantes(LocalDate hoje) {
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (status == StatusEmprestimo.ATRASADO) {
			throw new IllegalStateException("Erro: O empréstimo já está atrasado. Não é possível calcular dias restantes.");
		}
		if (status != StatusEmprestimo.EM_ANDAMENTO) {
			throw new IllegalStateException("Erro: O empréstimo não está ativo. Não é possível calcular dias restantes.");
		}
		
		return (int) Math.max(0, ChronoUnit.DAYS.between(hoje, dtDevolucaoPrevista));
	}
}

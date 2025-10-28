package com.projeto.sistemabiblioteca.schedulers;

import static com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo.SEPARADO;
import static com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo.EM_ANDAMENTO;
import static com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo.ATRASADO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;
import com.projeto.sistemabiblioteca.services.EmprestimoService;

import jakarta.transaction.Transactional;

@Component
public class EmprestimoScheduler {
	
	private EmprestimoService emprestimoService;
	
	private MultaRepository multaRepository;
	
	private ExemplarRepository exemplarRepository;
	
	public EmprestimoScheduler(EmprestimoService emprestimoService, MultaRepository multaRepository, ExemplarRepository exemplarRepository) {
		this.emprestimoService = emprestimoService;
		this.multaRepository = multaRepository;
		this.exemplarRepository = exemplarRepository;
	}
	
	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void verificarEmprestimos() {
		LocalDate hoje = LocalDate.now();
		List<Emprestimo> emprestimos = emprestimoService.buscarTodosEmprestimosParaVerificacaoDiaria(Set.of(SEPARADO, EM_ANDAMENTO, ATRASADO));
		
		for (Emprestimo emp : emprestimos) {
			switch (emp.getStatus()) {
				case SEPARADO:
					processarEmprestimoComStatusSeparado(emp, hoje);
					break;
				case EM_ANDAMENTO:
					processarEmprestimoComStatusEmAndamento(emp, hoje);
					break;
				case ATRASADO:
					processarEmprestimoComStatusAtrasado(emp, hoje);
					break;
				default:
					break;
			}
			
			emprestimoService.inserir(emp);
		}
	}
	
	private void processarEmprestimoComStatusSeparado(Emprestimo emp, LocalDate hoje) {
		if (emp.isPrazoDeRetiradaExpirado(LocalDate.now(), 2)) {
			emp.cancelarReserva();
			
			exemplarRepository.save(emp.getExemplar());
		}
	}
	
	private void processarEmprestimoComStatusEmAndamento(Emprestimo emp, LocalDate hoje) {
		if (emp.calcularDiasDeAtraso(LocalDate.now()) > 0) {
			emp.registrarAtraso(LocalDate.now());
			
			multaRepository.save(emp.getMulta());
		}
		
		/* enviar email avisando que falta 7 dias para devolução
		if (emp.getStatus() == StatusEmprestimo.EM_ANDAMENTO && emp.calcularDiasRestantes(LocalDate.now()) == 7) {
			
		}
		*/
	}
	
	private void processarEmprestimoComStatusAtrasado(Emprestimo emp, LocalDate hoje) {
		if (emp.getMulta().getStatusPagamento() != StatusPagamento.PERDOADO) {
			int diasAtraso = emp.calcularDiasDeAtraso(LocalDate.now());
				
			Multa multa = emp.getMulta();
			multa.aplicarMulta(diasAtraso);
				
			multaRepository.save(multa);
		}
	}
}

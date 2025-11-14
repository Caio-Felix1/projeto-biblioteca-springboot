package com.projeto.sistemabiblioteca.schedulers;

import static com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo.ATRASADO;
import static com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo.EM_ANDAMENTO;
import static com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo.SEPARADO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projeto.sistemabiblioteca.DTOs.EmailDTO;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;
import com.projeto.sistemabiblioteca.services.EmailService;
import com.projeto.sistemabiblioteca.services.EmprestimoService;

import jakarta.transaction.Transactional;

@Component
public class EmprestimoScheduler {
	
	private EmprestimoService emprestimoService;
	
	private MultaRepository multaRepository;
	
	private ExemplarRepository exemplarRepository;
	
	private EmailService emailService;
	
	public EmprestimoScheduler(EmprestimoService emprestimoService, MultaRepository multaRepository, ExemplarRepository exemplarRepository, EmailService emailService) {
		this.emprestimoService = emprestimoService;
		this.multaRepository = multaRepository;
		this.exemplarRepository = exemplarRepository;
		this.emailService = emailService;
	}
	
	@Scheduled(cron = "0 0 0 * * ?")
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
		if (emp.isPrazoDeRetiradaExpirado(hoje, 2)) {
			emp.cancelarReserva();
			
			exemplarRepository.save(emp.getExemplar());
			
			if (Arrays.asList(StatusConta.ATIVA, StatusConta.EM_ANALISE_EXCLUSAO).contains(emp.getPessoa().getStatusConta())) {
				EmailDTO emailDTO = new EmailDTO(
						emp.getPessoa().getEmail().getEndereco(),
						"Aviso: Empréstimo cancelado por não retirada",
						"Olá, " + emp.getPessoa().getNome() + ",\n\n"
							    + "Informamos que o empréstimo do livro \"" + emp.getExemplar().getEdicao().getTitulo().getNome() + "\" foi cancelado, "
							    + "pois não houve retirada dentro do prazo estabelecido.\n\n"
							    + "Caso ainda tenha interesse no livro, será necessário realizar uma nova reserva ou "
							    + "solicitar um novo empréstimo.\n\n"
							    + "Atenciosamente,\n"
							    + "Equipe da Biblioteca");
				
				emailService.sendEmail(emailDTO);
			}
		}
	}
	
	private void processarEmprestimoComStatusEmAndamento(Emprestimo emp, LocalDate hoje) {
		int diasAtraso = emp.calcularDiasDeAtraso(hoje);
		
		if (diasAtraso > 0) {
			emp.registrarAtraso(hoje);
			
			multaRepository.save(emp.getMulta());
			
			if (Arrays.asList(StatusConta.ATIVA, StatusConta.EM_ANALISE_EXCLUSAO).contains(emp.getPessoa().getStatusConta())) {
				EmailDTO emailDTO = new EmailDTO(
						emp.getPessoa().getEmail().getEndereco(),
						"Aviso: Empréstimo em atraso (1 dia)",
						"Olá, " + emp.getPessoa().getNome() + ",\n\n"
							    + "Identificamos que o prazo de devolução do livro \"" + emp.getExemplar().getEdicao().getTitulo().getNome() + "\" "
							    + "venceu em " + emp.getDtDevolucaoPrevista() + " e atualmente está em atraso de "
							    + "1 dia.\n\n"
							    + "Pedimos que realize a devolução o quanto antes para evitar multas adicionais.\n\n"
							    + "Atenciosamente,\n"
							    + "Equipe da Biblioteca");
				
				emailService.sendEmail(emailDTO);
			}
		}
		else if (emp.getStatus() == StatusEmprestimo.EM_ANDAMENTO && Arrays.asList(StatusConta.ATIVA, StatusConta.EM_ANALISE_EXCLUSAO).contains(emp.getPessoa().getStatusConta())){
		
			int diasRestantes = emp.calcularDiasRestantes(hoje);
			
			if (Arrays.asList(1, 3, 7, 0).contains(diasRestantes)) {
				String nomePessoa = emp.getPessoa().getNome();
				String email = emp.getPessoa().getEmail().getEndereco();
				String nomeDoLivro = emp.getExemplar().getEdicao().getTitulo().getNome();				
				
				String assunto = (diasRestantes > 0) ? "Aviso de Devolução de Empréstimo" : "Lembrete: Devolução vence hoje";
				
				String corpo = (diasRestantes > 0) ? 
						"Olá, " + nomePessoa + ",\n\n"
					    + "Este é um lembrete de que faltam " + diasRestantes + " dias para a data de devolução prevista "
					    + "do livro \"" + nomeDoLivro + "\".\n\n"
					    + "Data prevista para devolução: " + emp.getDtDevolucaoPrevista() + "\n\n"
					    + "Por favor, organize-se para realizar a devolução dentro do prazo e evitar multas.\n\n"
					    + "Atenciosamente,\n"
					    + "Equipe da Biblioteca"
					    :
						"Olá, " + nomePessoa + ",\n\n"
						+ "Este é um lembrete de que a data de devolução prevista para o livro \"" 
						+ nomeDoLivro + "\" é hoje (" + emp.getDtDevolucaoPrevista() + ").\n\n"
						+ "Pedimos que realize a devolução até o fim do dia para evitar multas.\n\n"
						+ "Atenciosamente,\n"
						+ "Equipe da Biblioteca";
				
				EmailDTO emailDTO = new EmailDTO(
							email,
							assunto,
							corpo);
				
				emailService.sendEmail(emailDTO);
			}
		}
	}
	
	private void processarEmprestimoComStatusAtrasado(Emprestimo emp, LocalDate hoje) {
		if (emp.getMulta().getStatusPagamento() != StatusPagamento.PERDOADO) {
			int diasAtraso = emp.calcularDiasDeAtraso(hoje);
			
			if (diasAtraso <= 50) {
				Multa multa = emp.getMulta();
				multa.aplicarMulta(diasAtraso);
					
				multaRepository.save(multa);
				
				if (Arrays.asList(7, 15, 30, 50).contains(diasAtraso) && Arrays.asList(StatusConta.ATIVA, StatusConta.EM_ANALISE_EXCLUSAO).contains(emp.getPessoa().getStatusConta())) {
					String nomePessoa = emp.getPessoa().getNome();
					String email = emp.getPessoa().getEmail().getEndereco();
					String nomeDoLivro = emp.getExemplar().getEdicao().getTitulo().getNome();
					
					String assunto = (diasAtraso < 50) ? "Aviso: Empréstimo em atraso (" + diasAtraso + " dias)" : "Último Aviso: Empréstimo em atraso (50 dias)";;
					
					String corpo = (diasAtraso < 50) ? 
							"Olá, " + nomePessoa + ",\n\n"
						    + "Identificamos que o prazo de devolução do livro \"" + nomeDoLivro + "\" "
						    + "venceu em " + emp.getDtDevolucaoPrevista() + " e atualmente está em atraso de "
						    + diasAtraso + " dias.\n\n"
						    + "Pedimos que realize a devolução o quanto antes para evitar multas adicionais.\n\n"
						    + "Atenciosamente,\n"
						    + "Equipe da Biblioteca"
						    :
						    "Olá, " + nomePessoa + ",\n\n"
						    + "O livro \"" + nomeDoLivro + "\" está em atraso há 50 dias. "
						    + "A multa atingiu o valor máximo permitido e não aumentará mais, mas a devolução do livro "
						    + "continua obrigatória.\n\n"
						    + "Este é o último aviso antes de medidas administrativas adicionais.\n\n"
						    + "Atenciosamente,\nEquipe da Biblioteca";
					
					EmailDTO emailDTO = new EmailDTO(
							email,
							assunto,
							corpo);
				
					emailService.sendEmail(emailDTO);
				}
			}
		}
	}
}

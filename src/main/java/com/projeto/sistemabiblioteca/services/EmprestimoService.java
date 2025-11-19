package com.projeto.sistemabiblioteca.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.EmprestimoCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;
import com.projeto.sistemabiblioteca.repositories.EmprestimoRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EmprestimoService {
	
	private EmprestimoRepository emprestimoRepository;
	
	private MultaRepository multaRepository;
	
	private PessoaService pessoaService;
	
	private ExemplarService exemplarService;

	public EmprestimoService(EmprestimoRepository emprestimoRepository, MultaRepository multaRepository, PessoaService pessoaService, ExemplarService exemplarService) {
		this.emprestimoRepository = emprestimoRepository;
		this.multaRepository = multaRepository;
		this.pessoaService = pessoaService;
		this.exemplarService = exemplarService;
	}
	
	public List<Emprestimo> buscarTodosParaDashboard() {
		return emprestimoRepository.findAll();
	}
	
	public Page<Emprestimo> buscarTodos(Pageable pageable) {
		return emprestimoRepository.findAll(pageable);
	}
	
	public Page<Emprestimo> buscarTodosComStatusIgualA(StatusEmprestimo statusEmprestimo, Pageable pageable) {
		return emprestimoRepository.findAllByStatusEquals(statusEmprestimo, pageable);
	}
	
	public Page<Emprestimo> buscarTodosPorIdPessoa(Long id, Pageable pageable) {
		return emprestimoRepository.findAllByPessoaIdPessoa(id, pageable);
	}
	
	public Page<Emprestimo> buscarTodosPorEmailDoUsuario(String email, Pageable pageable) {
		return emprestimoRepository.findAllByPessoaEmailEndereco(email, pageable);
	}
	
	public Page<Emprestimo> buscarTodosPorCpfDoUsuario(String cpf, Pageable pageable) {
		return emprestimoRepository.findAllByPessoaCpfValor(cpf, pageable);
	}
	
	public List<Emprestimo> buscarTodosEmprestimosParaVerificacaoDiaria(Set<StatusEmprestimo> statusSet) {
		return emprestimoRepository.findAllByStatusIn(statusSet);
	}
	
	public Emprestimo buscarPorId(Long id) {
		Optional<Emprestimo> emprestimo = emprestimoRepository.findById(id);
		if (emprestimo.isEmpty()) {
			throw new EntityNotFoundException("Erro: emprestimo com id correspondente não foi encontrado.");
		}
		return emprestimo.get();
	}
	
	private long quantidadeDeEmprestimosAtivosDoUsuario(Pessoa pessoa) {
		long qtdEmprestimo = emprestimoRepository.countByPessoaAndStatusIn(
				pessoa, 
				Set.of(
						StatusEmprestimo.RESERVADO, 
						StatusEmprestimo.SEPARADO,
						StatusEmprestimo.EM_ANDAMENTO,
						StatusEmprestimo.ATRASADO));
		
		return qtdEmprestimo;
	}
	
	private boolean verificarSeUsuarioTemMultaPendente(Pessoa pessoa) {
		return emprestimoRepository.existsByPessoaAndMultaStatusPagamento(pessoa, StatusPagamento.PENDENTE);
	}
	
	@Transactional
	public List<Emprestimo> cadastrarEmprestimos(EmprestimoCreateDTO emprestimoCreateDTO) {
		Pessoa pessoa = pessoaService.buscarPorId(emprestimoCreateDTO.idPessoa());
		
		if (pessoa.getStatusConta() != StatusConta.ATIVA) {
			throw new IllegalArgumentException("Erro: não é possível associar um empréstimo a um usuário com conta que não está ativa.");
		}
		if (pessoa.getFuncao() != FuncaoUsuario.CLIENTE) {
			throw new IllegalArgumentException("Erro: não é possível associar um empréstimo a um usuário que não é cliente.");
		}
		
		long qtdEmprestimos = quantidadeDeEmprestimosAtivosDoUsuario(pessoa);
		int qtdPedidos = emprestimoCreateDTO.idsEdicao().size();
		
		if (qtdEmprestimos == 5) {
			throw new IllegalArgumentException("Erro: não é possível cadastrar um novo empréstimo para esse usuário. Ele já atingiu o limite de 5 empréstimos ativos por usuário.");
		}
		if (qtdEmprestimos + qtdPedidos > 5) {
			throw new IllegalArgumentException("Erro: não é possível cadastrar " + qtdPedidos + " empréstimos para esse usuário. Ele já possui " + qtdEmprestimos + " empréstimos ativos e o limite é 5.");
		}
		if (verificarSeUsuarioTemMultaPendente(pessoa)) {
			throw new IllegalArgumentException("Erro: não é possível cadastrar um novo empréstimo para esse usuário. Ele possui multa pendente.");
		}
		
		LocalDate hoje = LocalDate.now();
		List<Emprestimo> emprestimos = new ArrayList<>();
		for (Long idEdicao : emprestimoCreateDTO.idsEdicao()) {
			Exemplar exemplar = exemplarService.buscarPrimeiroExemplarPorEdicaoEStatus(idEdicao, StatusExemplar.DISPONIVEL);
			
			exemplar.alugar();
			
			exemplarService.inserir(exemplar);
			
			Multa multa = Multa.criarMultaVazia();
			
			multaRepository.save(multa);
			
			Emprestimo emprestimo = new Emprestimo(hoje, pessoa, exemplar, multa);
			emprestimos.add(inserir(emprestimo));
		}
		return emprestimos;
	}
	
	public Emprestimo inserir(Emprestimo emprestimo) {
		return emprestimoRepository.save(emprestimo);
	}
	
	@Transactional
	public Emprestimo atualizar(Long id, EmprestimoUpdateDTO emprestimoUpdateDTO) {
		Emprestimo emprestimo1 = buscarPorId(id);
		
		Pessoa pessoa;
		if (emprestimo1.getPessoa().getIdPessoa().equals(emprestimoUpdateDTO.idPessoa())) {
			pessoa = emprestimo1.getPessoa();
		}
		else {
			pessoa = pessoaService.buscarPorId(emprestimoUpdateDTO.idPessoa());
			
			if (pessoa.getStatusConta() != StatusConta.ATIVA) {
				throw new IllegalArgumentException("Erro: não é possível associar um empréstimo a um usuário com conta que não está ativa ao atualizar.");
			}
			if (pessoa.getFuncao() != FuncaoUsuario.CLIENTE) {
				throw new IllegalArgumentException("Erro: não é possível associar um empréstimo a um usuário que não é cliente ao atualizar.");
			}
			if (quantidadeDeEmprestimosAtivosDoUsuario(pessoa) == 5) {
				throw new IllegalArgumentException("Erro: não é possível associar um empréstimo para esse usuário ao atualizar. Ele já atingiu o limite de 5 empréstimos ativos por usuário.");
			}
			if (verificarSeUsuarioTemMultaPendente(pessoa)) {
				throw new IllegalArgumentException("Erro: não é possível associar um empréstimo para esse usuário ao atualizar. Ele possui multa pendente.");
			}
		}
		
		Exemplar exemplar;
		if (emprestimo1.getExemplar().getIdExemplar().equals(emprestimoUpdateDTO.idExemplar())) {
			exemplar = emprestimo1.getExemplar();
		}
		else {
			exemplar = exemplarService.buscarPorId(emprestimoUpdateDTO.idExemplar());
			
			if (exemplar.getStatus() != StatusExemplar.DISPONIVEL) {
				throw new IllegalArgumentException("Erro: não é possível associar um empréstimo a um exemplar que não está disponível ao atualizar.");
			}
			
			exemplar.alugar();
			
			exemplarService.inserir(exemplar);
		}
		
		Emprestimo emprestimo2 = new Emprestimo(LocalDate.now(), pessoa, exemplar, null);
		
		atualizarDados(emprestimo1, emprestimo2);
		return emprestimoRepository.save(emprestimo1);
	}
	
	private void atualizarDados(Emprestimo emprestimo1, Emprestimo emprestimo2) {
		emprestimo1.setPessoa(emprestimo2.getPessoa());
		emprestimo1.setExemplar(emprestimo2.getExemplar());
	}
	
	public Emprestimo registrarSeparacaoDoExemplar(Long id) {
		Emprestimo emprestimo = buscarPorId(id);
		
		emprestimo.separarExemplar(LocalDate.now());
		
		return emprestimoRepository.save(emprestimo);
	}
	
	public void registrarRetiradaDoExemplar(Long id, LocalDate dtDevolucaoPrevista) {
		Emprestimo emprestimo = buscarPorId(id);
		
		emprestimo.retirarExemplar(LocalDate.now(), dtDevolucaoPrevista);
		
		emprestimoRepository.save(emprestimo);
	}
	
	public void registrarRetiradaDoExemplar(Long id) {
		Emprestimo emprestimo = buscarPorId(id);
		
		emprestimo.retirarExemplar(LocalDate.now());
		
		emprestimoRepository.save(emprestimo);
	}
	
	public void renovarDataDeDevolucaoPrevista(Long id, LocalDate dtDevolucaoPrevista) {
		Emprestimo emprestimo = buscarPorId(id);
		
		emprestimo.definirDataDevolucaoPrevista(dtDevolucaoPrevista, LocalDate.now());
		
		emprestimoRepository.save(emprestimo);
	}
	
	@Transactional
	public void registrarDevolucaoDoExemplar(Long id) {
		Emprestimo emprestimo = buscarPorId(id);
		
		emprestimo.devolverExemplar(LocalDate.now());
		
		exemplarService.inserir(emprestimo.getExemplar());
		emprestimoRepository.save(emprestimo);
	}
	
	@Transactional
	public void pagarMulta(Long id) {
		Emprestimo emprestimo = buscarPorId(id);
		
		if (emprestimo.getStatus() != StatusEmprestimo.DEVOLVIDO && emprestimo.getStatus() != StatusEmprestimo.EXEMPLAR_PERDIDO) {
			throw new IllegalStateException("Erro: não é possível pagar a multa antes da devolução ou perda do exemplar.");
		}
		
		Multa multa = emprestimo.getMulta();
		multa.pagarMulta();
		
		multaRepository.save(multa);
	}
	
	@Transactional
	public Emprestimo registrarPerdaDoExemplar(Long id) {
		Emprestimo emprestimo = buscarPorId(id);
		
		emprestimo.registrarPerdaDoExemplar();
		
		exemplarService.inserir(emprestimo.getExemplar());
		return emprestimoRepository.save(emprestimo);
	}
}

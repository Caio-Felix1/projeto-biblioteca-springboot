package com.projeto.sistemabiblioteca.integration.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.DTOs.EmprestimoCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.ClassificacaoIndicativa;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;
import com.projeto.sistemabiblioteca.entities.enums.TamanhoEdicao;
import com.projeto.sistemabiblioteca.entities.enums.TipoCapa;
import com.projeto.sistemabiblioteca.repositories.EdicaoRepository;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.services.EmprestimoService;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class EmprestimoServiceIntegrationTest {
	
	@Autowired
	private EmprestimoService emprestimoService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ExemplarRepository exemplarRepository;
	
	@Autowired
	private MultaRepository multaRepository;
	
	@Autowired
	private EdicaoRepository edicaoRepository;
	
	private final LocalDate dtInicioEmprestimoPadrao = LocalDate.parse("2025-10-10");
	private final LocalDate hojePadrao = LocalDate.parse("2025-10-12");
	private final LocalDate dtDevolucaoPrevista = LocalDate.parse("2025-10-15");
	
	private Exemplar criarExemplarAlugado() {
		Exemplar exemplar = new Exemplar(null, null);
		exemplar.alugar();
		return exemplar;
	}
	
	private Emprestimo criarEmprestimoComStatusReservado() {
		return new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
	}
	
	private Emprestimo criarEmprestimoComStatusSeparado() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusEmAndamento() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		return emprestimo;
	}
	
	private Emprestimo criarEmprestimoComStatusAtrasado() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		emprestimo.registrarAtraso(dtDevolucaoPrevista.plusDays(1));
		return emprestimo;
	}
	
	@Test
	void deveBuscarTodosEmprestimosFiltrandoPelaPessoa() {
		Pessoa pessoa1 = new Pessoa(
                "Maria Joana",
                new Cpf("11111111111"),
                Sexo.FEMININO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("maria@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		Pessoa pessoa2 = new Pessoa(
                "João Silva",
                new Cpf("22222222222"),
                Sexo.MASCULINO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("joao@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		pessoaRepository.save(pessoa1);
		pessoaRepository.save(pessoa2);
		
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo2 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo3 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo4 = criarEmprestimoComStatusReservado();
		
		emprestimo1.setPessoa(pessoa1);
		emprestimo2.setPessoa(pessoa1);
		emprestimo3.setPessoa(pessoa2);
		emprestimo4.setPessoa(pessoa2);
		
		exemplarRepository.save(emprestimo1.getExemplar());
		exemplarRepository.save(emprestimo2.getExemplar());
		exemplarRepository.save(emprestimo3.getExemplar());
		exemplarRepository.save(emprestimo4.getExemplar());
		
		multaRepository.save(emprestimo1.getMulta());
		multaRepository.save(emprestimo2.getMulta());
		multaRepository.save(emprestimo3.getMulta());
		multaRepository.save(emprestimo4.getMulta());
		
		emprestimoService.inserir(emprestimo1);
		emprestimoService.inserir(emprestimo2);
		emprestimoService.inserir(emprestimo3);
		emprestimoService.inserir(emprestimo4);
		
		List<Emprestimo> emprestimos = emprestimoService.buscarTodosPorIdPessoa(pessoa1.getIdPessoa());
		
		Assertions.assertEquals(2, emprestimos.size());
		for (Emprestimo emp : emprestimos) {
			Assertions.assertEquals(pessoa1.getIdPessoa(), emp.getPessoa().getIdPessoa());
			Assertions.assertEquals("Maria Joana", emp.getPessoa().getNome());
		}
	}
	
	@Test
	void deveBuscarTodosEmprestimosFiltrandoPeloEmailDoUsuario() {
		Pessoa pessoa1 = new Pessoa(
                "Maria Joana",
                new Cpf("11111111111"),
                Sexo.FEMININO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("maria@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		Pessoa pessoa2 = new Pessoa(
                "João Silva",
                new Cpf("22222222222"),
                Sexo.MASCULINO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("joao@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		pessoaRepository.save(pessoa1);
		pessoaRepository.save(pessoa2);
		
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo2 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo3 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo4 = criarEmprestimoComStatusReservado();
		
		emprestimo1.setPessoa(pessoa1);
		emprestimo2.setPessoa(pessoa1);
		emprestimo3.setPessoa(pessoa2);
		emprestimo4.setPessoa(pessoa2);
		
		exemplarRepository.save(emprestimo1.getExemplar());
		exemplarRepository.save(emprestimo2.getExemplar());
		exemplarRepository.save(emprestimo3.getExemplar());
		exemplarRepository.save(emprestimo4.getExemplar());
		
		multaRepository.save(emprestimo1.getMulta());
		multaRepository.save(emprestimo2.getMulta());
		multaRepository.save(emprestimo3.getMulta());
		multaRepository.save(emprestimo4.getMulta());
		
		emprestimoService.inserir(emprestimo1);
		emprestimoService.inserir(emprestimo2);
		emprestimoService.inserir(emprestimo3);
		emprestimoService.inserir(emprestimo4);
		
		List<Emprestimo> emprestimos = emprestimoService.buscarTodosPorEmailDoUsuario(pessoa1.getEmail().getEndereco());
		
		Assertions.assertEquals(2, emprestimos.size());
		for (Emprestimo emp : emprestimos) {
			Assertions.assertEquals(pessoa1.getEmail().getEndereco(), emp.getPessoa().getEmail().getEndereco());
			Assertions.assertEquals("Maria Joana", emp.getPessoa().getNome());
		}
	}
	
	@Test
	void deveBuscarTodosEmprestimosFiltrandoPeloCpfDoUsuario() {
		Pessoa pessoa1 = new Pessoa(
                "Maria Joana",
                new Cpf("11111111111"),
                Sexo.FEMININO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("maria@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		Pessoa pessoa2 = new Pessoa(
                "João Silva",
                new Cpf("22222222222"),
                Sexo.MASCULINO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("joao@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		pessoaRepository.save(pessoa1);
		pessoaRepository.save(pessoa2);
		
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo2 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo3 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo4 = criarEmprestimoComStatusReservado();
		
		emprestimo1.setPessoa(pessoa1);
		emprestimo2.setPessoa(pessoa1);
		emprestimo3.setPessoa(pessoa2);
		emprestimo4.setPessoa(pessoa2);
		
		exemplarRepository.save(emprestimo1.getExemplar());
		exemplarRepository.save(emprestimo2.getExemplar());
		exemplarRepository.save(emprestimo3.getExemplar());
		exemplarRepository.save(emprestimo4.getExemplar());
		
		multaRepository.save(emprestimo1.getMulta());
		multaRepository.save(emprestimo2.getMulta());
		multaRepository.save(emprestimo3.getMulta());
		multaRepository.save(emprestimo4.getMulta());
		
		emprestimoService.inserir(emprestimo1);
		emprestimoService.inserir(emprestimo2);
		emprestimoService.inserir(emprestimo3);
		emprestimoService.inserir(emprestimo4);
		
		List<Emprestimo> emprestimos = emprestimoService.buscarTodosPorCpfDoUsuario(pessoa2.getCpf().getValor());
		
		Assertions.assertEquals(2, emprestimos.size());
		for (Emprestimo emp : emprestimos) {
			Assertions.assertEquals(pessoa2.getCpf().getValor(), emp.getPessoa().getCpf().getValor());
			Assertions.assertEquals("João Silva", emp.getPessoa().getNome());
		}
	}
	
	@Test
	void deveBuscarTodosEmprestimosFiltrandoPorVariosStatus() {
		Emprestimo emprestimo1 = criarEmprestimoComStatusReservado();
		Emprestimo emprestimo2 = criarEmprestimoComStatusSeparado();
		Emprestimo emprestimo3 = criarEmprestimoComStatusEmAndamento();
		Emprestimo emprestimo4 = criarEmprestimoComStatusAtrasado();
		
		exemplarRepository.save(emprestimo1.getExemplar());
		exemplarRepository.save(emprestimo2.getExemplar());
		exemplarRepository.save(emprestimo3.getExemplar());
		exemplarRepository.save(emprestimo4.getExemplar());
		
		multaRepository.save(emprestimo1.getMulta());
		multaRepository.save(emprestimo2.getMulta());
		multaRepository.save(emprestimo3.getMulta());
		multaRepository.save(emprestimo4.getMulta());
		
		emprestimoService.inserir(emprestimo1);
		emprestimoService.inserir(emprestimo2);
		emprestimoService.inserir(emprestimo3);
		emprestimoService.inserir(emprestimo4);
		
		List<Emprestimo> emprestimos = emprestimoService.buscarTodosEmprestimosParaVerificacaoDiaria(
				Set.of(StatusEmprestimo.SEPARADO,
						StatusEmprestimo.EM_ANDAMENTO,
						StatusEmprestimo.ATRASADO));
		
		Assertions.assertEquals(3, emprestimos.size());
		for (Emprestimo emp : emprestimos) {
			Assertions.assertTrue(
					List.of(StatusEmprestimo.SEPARADO,
						StatusEmprestimo.EM_ANDAMENTO,
						StatusEmprestimo.ATRASADO)
					.contains(emp.getStatus()));
		}
	}
	
	@Test
	void deveCadastrarEmprestimos() {
		Pessoa pessoa = new Pessoa(
                "Maria Joana",
                new Cpf("11111111111"),
                Sexo.FEMININO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("maria@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		pessoaRepository.save(pessoa);
		
		Edicao edicao1 = new Edicao(
				"Edição 1",
				TipoCapa.DURA,
				100,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2025, 10, 10),
				null,
				null,
				null,
				null);
		
		Edicao edicao2 = new Edicao(
				"Edição 2",
				TipoCapa.MOLE,
				200,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C18,
				LocalDate.of(2020, 9, 9),
				null,
				null,
				null,
				null);
		
		edicaoRepository.save(edicao1);
		edicaoRepository.save(edicao2);
		
		Exemplar exemplar1 = new Exemplar(EstadoFisico.BOM, edicao1);
		Exemplar exemplar2 = new Exemplar(EstadoFisico.OTIMO, edicao2);
		Exemplar exemplar3 = new Exemplar(EstadoFisico.EXCELENTE, edicao2);
		
		exemplarRepository.save(exemplar1);
		exemplarRepository.save(exemplar2);
		exemplarRepository.save(exemplar3);
		
		EmprestimoCreateDTO emprestimoDTO = new EmprestimoCreateDTO(
				pessoa.getIdPessoa(),
				List.of(edicao1.getIdEdicao(), edicao2.getIdEdicao()));
		
		List<Emprestimo> emprestimos = emprestimoService.cadastrarEmprestimos(emprestimoDTO);
		
		LocalDate hoje = LocalDate.now();
		
		Assertions.assertEquals(2, emprestimos.size());
		
		for (Emprestimo emp : emprestimos) {
			Assertions.assertEquals(hoje, emp.getDtInicioEmprestimo());
			Assertions.assertEquals(pessoa.getIdPessoa(), emp.getPessoa().getIdPessoa());
			
			Exemplar exemplar = emp.getExemplar();
			
			Assertions.assertTrue(exemplar.getEstadoFisico() == EstadoFisico.BOM || exemplar.getEstadoFisico() == EstadoFisico.EXCELENTE);
			Assertions.assertEquals(StatusExemplar.ALUGADO, exemplar.getStatus());
			Assertions.assertEquals(StatusPagamento.NAO_APLICAVEL, emp.getMulta().getStatusPagamento());
		}
	}
	
	@Test
	void deveAtualizarEmprestimo() {
		Pessoa pessoa1 = new Pessoa(
                "Maria Joana",
                new Cpf("11111111111"),
                Sexo.FEMININO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1990, 10, 10),
                LocalDate.of(2025, 10, 10),
                new Telefone("1234567891"),
                new Email("maria@gmail.com"),
                "senhaHash",
                StatusConta.ATIVA,
                null
            );
		
		Pessoa pessoa2 = new Pessoa(
                "João Silva",
                new Cpf("22222222222"),
                Sexo.MASCULINO,
                FuncaoUsuario.CLIENTE,
                LocalDate.of(1995, 9, 9),
                LocalDate.of(2025, 10, 10),
                new Telefone("9876543210"),
                new Email("joao@gmail.com"),
                "senhaHashNova",
                StatusConta.ATIVA,
                null
            );
		
		pessoaRepository.save(pessoa1);
		pessoaRepository.save(pessoa2);
		
		Exemplar exemplar1 = new Exemplar(EstadoFisico.EXCELENTE, null);
		exemplar1.alugar();
		Exemplar exemplar2 = new Exemplar(EstadoFisico.BOM, null);
		
		exemplarRepository.save(exemplar1);
		exemplarRepository.save(exemplar2);
		
		Emprestimo emprestimo = new Emprestimo(LocalDate.now(), pessoa1, exemplar1, Multa.criarMultaVazia());
		
		multaRepository.save(emprestimo.getMulta());
		
		emprestimoService.inserir(emprestimo);
		
		EmprestimoUpdateDTO emprestimoUpdateDTO = new EmprestimoUpdateDTO(pessoa2.getIdPessoa(), exemplar2.getIdExemplar());
		
		Emprestimo emprestimoAtualizado = emprestimoService.atualizar(emprestimo.getIdEmprestimo(), emprestimoUpdateDTO);
		
		Assertions.assertEquals(StatusExemplar.DISPONIVEL, exemplar1.getStatus());
		Assertions.assertEquals(pessoa2.getIdPessoa(), emprestimoAtualizado.getPessoa().getIdPessoa());
		Assertions.assertEquals(exemplar2.getIdExemplar(), emprestimoAtualizado.getExemplar().getIdExemplar());
	}
}

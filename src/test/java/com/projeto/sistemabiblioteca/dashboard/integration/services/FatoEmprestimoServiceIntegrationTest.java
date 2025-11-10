package com.projeto.sistemabiblioteca.dashboard.integration.services;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCliente;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEdicao;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEditora;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimExemplar;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimIdioma;
import com.projeto.sistemabiblioteca.dashboard.dimensions.DimTitulo;
import com.projeto.sistemabiblioteca.dashboard.facts.FatoEmprestimo;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimAutorRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimCategoriaRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimClienteRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimEdicaoRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimEditoraRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimExemplarRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimIdiomaRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimTituloRepository;
import com.projeto.sistemabiblioteca.dashboard.repositories.FatoEmprestimoRepository;
import com.projeto.sistemabiblioteca.dashboard.services.FatoEmprestimoService;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.Titulo;
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
import com.projeto.sistemabiblioteca.repositories.EmprestimoRepository;
import com.projeto.sistemabiblioteca.repositories.ExemplarRepository;
import com.projeto.sistemabiblioteca.repositories.MultaRepository;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class FatoEmprestimoServiceIntegrationTest {
	
	@Autowired
	private FatoEmprestimoService fatoEmprestimoService;
	
	@Autowired
	private FatoEmprestimoRepository fatoEmprestimoRepository;
	
	@Autowired
	private DimClienteRepository dimClienteRepository;
	
	@Autowired
	private DimAutorRepository dimAutorRepository;
	
	@Autowired
	private DimCategoriaRepository dimCategoriaRepository;
	
	@Autowired
	private DimEdicaoRepository dimEdicaoRepository;
	
	@Autowired
	private DimExemplarRepository dimExemplarRepository;
	
	@Autowired
	private DimIdiomaRepository dimIdiomaRepository;
	
	@Autowired
	private DimEditoraRepository dimEditoraRepository;
	
	@Autowired
	private DimTituloRepository dimTituloRepository;
	
	@Autowired
	private EmprestimoRepository emprestimoRepository;
	
	@Autowired
	private ExemplarRepository exemplarRepository;
	
	@Autowired
	private MultaRepository multaRepository;
	
	private final LocalDate dtInicioEmprestimoPadrao = LocalDate.parse("2025-10-10");
	private final LocalDate hojePadrao = LocalDate.parse("2025-10-12");
	private final LocalDate dtDevolucaoPrevista = LocalDate.parse("2025-10-15");
	
	private DimCliente criarDimCliente() {
		Pais pais = new Pais("Brasil");
		Estado estado = new Estado("São Paulo", pais);
		
		Endereco endereco = new Endereco(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				estado);
		
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
				endereco);
		
		return new DimCliente(pessoa);
	}
	
	private DimAutor criarDimAutor() {
		return new DimAutor(new Autor("Autor 1"));
	}
	
	private DimCategoria criarDimCategoria() {
		return new DimCategoria(new Categoria("Categoria 1"));
	}
	
	private DimTitulo criarDimTitulo(DimAutor dimAutor, DimCategoria dimCategoria) {
		return new DimTitulo(new Titulo("Título 1", null), Set.of(dimCategoria), Set.of(dimAutor));
	}
	
	private DimIdioma criarDimIdioma() {
		return new DimIdioma(new Idioma("Idioma 1"));
	}
	
	private DimEditora criarDimEditora() {
		return new DimEditora(new Editora("Editora 1"));
	}
	
	private DimEdicao criarDimEdicao() {
		return new DimEdicao(new Edicao(
				"Edição de Colecionador",
				TipoCapa.DURA,
				350,
				TamanhoEdicao.MEDIO,
				ClassificacaoIndicativa.C14,
				LocalDate.of(2020, 2, 2),
				null,
				null,
				null,
				null));
	}
	
	private DimExemplar criarDimExemplar() {
		return new DimExemplar(criarExemplarAlugado());
	}
	
	private Exemplar criarExemplarAlugado() {
		Exemplar exemplar = new Exemplar(EstadoFisico.EXCELENTE, null);
		exemplar.alugar();
		return exemplar;
	}
	
	private Emprestimo criarEmprestimoComStatusEmAndamento() {
		Emprestimo emprestimo = new Emprestimo(dtInicioEmprestimoPadrao, null, criarExemplarAlugado(), Multa.criarMultaVazia());
		emprestimo.separarExemplar(hojePadrao);
		emprestimo.retirarExemplar(hojePadrao, dtDevolucaoPrevista);
		return emprestimo;
	}
	
	@Test
	void deveCriarUmNovoFatoEmprestimo() {
		DimAutor dimAutor = criarDimAutor();
		
		dimAutorRepository.save(dimAutor);
		
		DimCategoria dimCategoria = criarDimCategoria();
		
		dimCategoriaRepository.save(dimCategoria);
		
		DimTitulo dimTitulo = criarDimTitulo(dimAutor, dimCategoria);
		
		dimTituloRepository.save(dimTitulo);
		
		DimIdioma dimIdioma = criarDimIdioma();
		
		dimIdiomaRepository.save(dimIdioma);
		
		DimEditora dimEditora = criarDimEditora();
		
		dimEditoraRepository.save(dimEditora);
		
		DimEdicao dimEdicao = criarDimEdicao();
		
		dimEdicaoRepository.save(dimEdicao);
		
		DimExemplar dimExemplar = criarDimExemplar();
		
		dimExemplarRepository.save(dimExemplar);
		
		DimCliente dimCliente = criarDimCliente();
		
		dimClienteRepository.save(dimCliente);
		
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		emprestimoRepository.save(emprestimo);
		
		exemplarRepository.save(emprestimo.getExemplar());
		
		multaRepository.save(emprestimo.getMulta());
		
		FatoEmprestimo fatoEmprestimoNovo = fatoEmprestimoService.atualizar(emprestimo,
				dimCliente.getSurrogateKey(),
				dimExemplar.getSurrogateKey(),
				dimEdicao.getSurrogateKey(),
				dimIdioma.getSurrogateKey(),
				dimEditora.getSurrogateKey(),
				dimTitulo.getSurrogateKey());
		
		Assertions.assertTrue(fatoEmprestimoNovo.getSkFato() != null && fatoEmprestimoNovo.getSkFato().getClass().equals(Long.class));
		Assertions.assertEquals(emprestimo.getIdEmprestimo(), fatoEmprestimoNovo.getIdNaturalEmprestimo());
		Assertions.assertEquals(emprestimo.getMulta().getIdMulta(), fatoEmprestimoNovo.getIdNaturalMulta());
		Assertions.assertEquals(dtInicioEmprestimoPadrao, fatoEmprestimoNovo.getDtInicioEmprestimo());
		Assertions.assertEquals(hojePadrao, fatoEmprestimoNovo.getDtSeparacaoExemplar());
		Assertions.assertEquals(hojePadrao, fatoEmprestimoNovo.getDtRetiradaExemplar());
		Assertions.assertEquals(dtDevolucaoPrevista, fatoEmprestimoNovo.getDtDevolucaoPrevista());
		Assertions.assertEquals(null, fatoEmprestimoNovo.getDtDevolvidoExemplar());
		Assertions.assertEquals(StatusEmprestimo.EM_ANDAMENTO, fatoEmprestimoNovo.getStatus());
		Assertions.assertEquals(0, fatoEmprestimoNovo.getDiasAtraso());
		Assertions.assertEquals(0.0, fatoEmprestimoNovo.getValorMulta());
		Assertions.assertEquals(StatusPagamento.NAO_APLICAVEL, fatoEmprestimoNovo.getStatusPagamento());
		Assertions.assertEquals(dimCliente.getSurrogateKey(), fatoEmprestimoNovo.getSkCliente());
		Assertions.assertEquals(dimExemplar.getSurrogateKey(), fatoEmprestimoNovo.getSkExemplar());
		Assertions.assertEquals(dimEdicao.getSurrogateKey(), fatoEmprestimoNovo.getSkEdicao());
		Assertions.assertEquals(dimIdioma.getSurrogateKey(), fatoEmprestimoNovo.getSkIdioma());
		Assertions.assertEquals(dimEditora.getSurrogateKey(), fatoEmprestimoNovo.getSkEditora());
		Assertions.assertEquals(dimTitulo.getSurrogateKey(), fatoEmprestimoNovo.getSkTitulo());
	}
	
	@Test
	void deveAtualizarFatoEmprestimoJaExistente() {
		DimAutor dimAutor = criarDimAutor();
		
		dimAutorRepository.save(dimAutor);
		
		DimCategoria dimCategoria = criarDimCategoria();
		
		dimCategoriaRepository.save(dimCategoria);
		
		DimTitulo dimTitulo = criarDimTitulo(dimAutor, dimCategoria);
		
		dimTituloRepository.save(dimTitulo);
		
		DimIdioma dimIdioma = criarDimIdioma();
		
		dimIdiomaRepository.save(dimIdioma);
		
		DimEditora dimEditora = criarDimEditora();
		
		dimEditoraRepository.save(dimEditora);
		
		DimEdicao dimEdicao = criarDimEdicao();
		
		dimEdicaoRepository.save(dimEdicao);
		
		DimExemplar dimExemplar = criarDimExemplar();
		
		dimExemplarRepository.save(dimExemplar);
		
		DimCliente dimCliente = criarDimCliente();
		
		dimClienteRepository.save(dimCliente);
		
		Emprestimo emprestimo = criarEmprestimoComStatusEmAndamento();
		
		emprestimoRepository.save(emprestimo);
		
		exemplarRepository.save(emprestimo.getExemplar());
		
		multaRepository.save(emprestimo.getMulta());
		
		FatoEmprestimo fatoEmprestimo = new FatoEmprestimo(
				emprestimo,
				dimCliente.getSurrogateKey(),
				dimExemplar.getSurrogateKey(),
				dimEdicao.getSurrogateKey(),
				dimIdioma.getSurrogateKey(),
				dimEditora.getSurrogateKey(),
				dimTitulo.getSurrogateKey());
		
		fatoEmprestimoRepository.save(fatoEmprestimo);
		
		emprestimo.registrarPerdaDoExemplar();
		dimExemplar.setStatus(emprestimo.getExemplar().getStatus());
		
		FatoEmprestimo fatoEmprestimoAtualizado = fatoEmprestimoService.atualizar(emprestimo,
				dimCliente.getSurrogateKey(),
				dimExemplar.getSurrogateKey(),
				dimEdicao.getSurrogateKey(),
				dimIdioma.getSurrogateKey(),
				dimEditora.getSurrogateKey(),
				dimTitulo.getSurrogateKey());
		
		Assertions.assertEquals(fatoEmprestimo.getSkFato(), fatoEmprestimoAtualizado.getSkFato());
		Assertions.assertEquals(emprestimo.getIdEmprestimo(), fatoEmprestimoAtualizado.getIdNaturalEmprestimo());
		Assertions.assertEquals(emprestimo.getMulta().getIdMulta(), fatoEmprestimoAtualizado.getIdNaturalMulta());
		Assertions.assertEquals(dtInicioEmprestimoPadrao, fatoEmprestimoAtualizado.getDtInicioEmprestimo());
		Assertions.assertEquals(hojePadrao, fatoEmprestimoAtualizado.getDtSeparacaoExemplar());
		Assertions.assertEquals(hojePadrao, fatoEmprestimoAtualizado.getDtRetiradaExemplar());
		Assertions.assertEquals(dtDevolucaoPrevista, fatoEmprestimoAtualizado.getDtDevolucaoPrevista());
		Assertions.assertEquals(null, fatoEmprestimoAtualizado.getDtDevolvidoExemplar());
		Assertions.assertEquals(StatusEmprestimo.EXEMPLAR_PERDIDO, fatoEmprestimoAtualizado.getStatus());
		Assertions.assertEquals(0, fatoEmprestimoAtualizado.getDiasAtraso());
		Assertions.assertEquals(50.0, fatoEmprestimoAtualizado.getValorMulta());
		Assertions.assertEquals(StatusPagamento.PENDENTE, fatoEmprestimoAtualizado.getStatusPagamento());
		Assertions.assertEquals(dimCliente.getSurrogateKey(), fatoEmprestimoAtualizado.getSkCliente());
		Assertions.assertEquals(dimExemplar.getSurrogateKey(), fatoEmprestimoAtualizado.getSkExemplar());
		Assertions.assertEquals(dimEdicao.getSurrogateKey(), fatoEmprestimoAtualizado.getSkEdicao());
		Assertions.assertEquals(dimIdioma.getSurrogateKey(), fatoEmprestimoAtualizado.getSkIdioma());
		Assertions.assertEquals(dimEditora.getSurrogateKey(), fatoEmprestimoAtualizado.getSkEditora());
		Assertions.assertEquals(dimTitulo.getSurrogateKey(), fatoEmprestimoAtualizado.getSkTitulo());
		
		Assertions.assertEquals(StatusExemplar.PERDIDO, dimExemplar.getStatus());
	}
	
}

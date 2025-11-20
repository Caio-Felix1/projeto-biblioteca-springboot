package com.projeto.sistemabiblioteca.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.DataDevolucaoPrevistaDTO;
import com.projeto.sistemabiblioteca.DTOs.EmailDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoResponseDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoUpdateDTO;
import com.projeto.sistemabiblioteca.DTOs.PageResponseDTO;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.services.EmailService;
import com.projeto.sistemabiblioteca.services.EmprestimoService;
import com.projeto.sistemabiblioteca.services.PessoaService;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
	
	private final EmprestimoService emprestimoService;
	private final PessoaService pessoaService;
	private final EmailService emailService;
	
	public EmprestimoController(EmprestimoService emprestimoService, PessoaService pessoaService, EmailService emailService) {
		this.emprestimoService = emprestimoService;
		this.pessoaService = pessoaService;
		this.emailService = emailService;
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<PageResponseDTO<EmprestimoResponseDTO>> listarTodos(@RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	
    	Page<Emprestimo> emprestimos = emprestimoService.buscarTodos(pageable);
    	List<EmprestimoResponseDTO> emprestimosResponseDTO = emprestimos
    			.stream()
    			.map(EmprestimoResponseDTO::converterParaDTO)
    			.toList();
    	
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(
        		new PageImpl<>(
        				emprestimosResponseDTO,
        				emprestimos.getPageable(),
        				emprestimos.getTotalElements())
        		)
        		);
    }
	
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/status/{status}")
    public ResponseEntity<PageResponseDTO<EmprestimoResponseDTO>> listarPorStatus(@PathVariable String status, @RequestParam int pagina, @RequestParam int tamanho) {
    	StatusEmprestimo statusEmprestimo;
    	try {
    		statusEmprestimo = StatusEmprestimo.valueOf(status.toUpperCase());
    	}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Erro: o status informado é inválido.");
		}
    	
    	Pageable pageable = PageRequest.of(pagina, tamanho);

    	Page<Emprestimo> emprestimos = emprestimoService.buscarTodosComStatusIgualA(statusEmprestimo, pageable);
    	List<EmprestimoResponseDTO> emprestimosResponseDTO = emprestimos
    			.stream()
    			.map(EmprestimoResponseDTO::converterParaDTO)
    			.toList();
    	
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(
        		new PageImpl<>(
        				emprestimosResponseDTO,
        				emprestimos.getPageable(),
        				emprestimos.getTotalElements())
        		)
        		);
    }
    
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/buscar-por-pessoa/{id}")
    public ResponseEntity<PageResponseDTO<EmprestimoResponseDTO>> listarTodosPorPessoa(@PathVariable Long id, @RequestParam int pagina, @RequestParam int tamanho, Authentication authentication) {
		String usernameAutenticado = authentication.getName();
		Pessoa usuarioAutenticado = pessoaService.buscarPorEmail(usernameAutenticado);
		
		if (usuarioAutenticado.getFuncao() == FuncaoUsuario.CLIENTE && !usuarioAutenticado.getIdPessoa().equals(id)) {
			throw new IllegalArgumentException("Erro: um cliente não pode visualizar os empréstimos de outro cliente.");
		}
		
    	Pageable pageable = PageRequest.of(pagina, tamanho);
		
    	Page<Emprestimo> emprestimos = emprestimoService.buscarTodosPorIdPessoa(id, pageable);
    	List<EmprestimoResponseDTO> emprestimosResponseDTO = emprestimos
    			.stream()
    			.map(EmprestimoResponseDTO::converterParaDTO)
    			.toList();
    	
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(
        		new PageImpl<>(
        				emprestimosResponseDTO,
        				emprestimos.getPageable(),
        				emprestimos.getTotalElements())
        		)
        		);
    }
    
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/buscar-por-email")
    public ResponseEntity<PageResponseDTO<EmprestimoResponseDTO>> listarTodosPorEmailDoUsuario(@RequestParam String email, @RequestParam int pagina, @RequestParam int tamanho, Authentication authentication) {
    	Email emailFormatoValidado = new Email(email);
    	
		String usernameAutenticado = authentication.getName();
		Pessoa usuarioAutenticado = pessoaService.buscarPorEmail(usernameAutenticado);
		
		if (usuarioAutenticado.getFuncao() == FuncaoUsuario.CLIENTE && !usuarioAutenticado.getEmail().getEndereco().equals(emailFormatoValidado.getEndereco())) {
			throw new IllegalArgumentException("Erro: um cliente não pode visualizar os empréstimos de outro cliente.");
		}
		
    	Pageable pageable = PageRequest.of(pagina, tamanho);

    	Page<Emprestimo> emprestimos = emprestimoService.buscarTodosPorEmailDoUsuario(emailFormatoValidado.getEndereco(), pageable);
    	List<EmprestimoResponseDTO> emprestimosResponseDTO = emprestimos
    			.stream()
    			.map(EmprestimoResponseDTO::converterParaDTO)
    			.toList();
    	
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(
        		new PageImpl<>(
        				emprestimosResponseDTO,
        				emprestimos.getPageable(),
        				emprestimos.getTotalElements())
        		)
        		);
    }
    
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/buscar-por-cpf")
    public ResponseEntity<PageResponseDTO<EmprestimoResponseDTO>> listarTodosPorCpfDoUsuario(@RequestParam String cpf, @RequestParam int pagina, @RequestParam int tamanho, Authentication authentication) {
    	Cpf cpfFormatoValidado = new Cpf(cpf);
    	
		String usernameAutenticado = authentication.getName();
		Pessoa usuarioAutenticado = pessoaService.buscarPorEmail(usernameAutenticado);
		
		if (usuarioAutenticado.getFuncao() == FuncaoUsuario.CLIENTE && !usuarioAutenticado.getCpf().getValor().equals(cpfFormatoValidado.getValor())) {
			throw new IllegalArgumentException("Erro: um cliente não pode visualizar os empréstimos de outro cliente.");
		}
		
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	
    	Page<Emprestimo> emprestimos = emprestimoService.buscarTodosPorCpfDoUsuario(cpfFormatoValidado.getValor(), pageable);
    	List<EmprestimoResponseDTO> emprestimosResponseDTO = emprestimos
    			.stream()
    			.map(EmprestimoResponseDTO::converterParaDTO)
    			.toList();
    	
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(
        		new PageImpl<>(
        				emprestimosResponseDTO,
        				emprestimos.getPageable(),
        				emprestimos.getTotalElements())
        		)
        		);
    }
    
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
    	Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimo);
    }
    
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO', 'ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<List<Emprestimo>> cadastrarEmprestimos(@Valid @RequestBody EmprestimoCreateDTO emprestimoCreateDTO, Authentication authentication) {
		// fazer validacao
		
    	List<Emprestimo> emprestimos = emprestimoService.cadastrarEmprestimos(emprestimoCreateDTO);
    	return ResponseEntity.ok(emprestimos);
    }

	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Emprestimo> atualizar(@PathVariable Long id, @Valid @RequestBody EmprestimoUpdateDTO emprestimoUpdateDTO, Authentication authentication) {
    	// fazer validacao
    	
    	Emprestimo emprestimoAtualizado = emprestimoService.atualizar(id, emprestimoUpdateDTO);
        return ResponseEntity.ok(emprestimoAtualizado);
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/registrar-separacao/{id}")
    public ResponseEntity<Void> registrarSeparacaoDoExemplar(@PathVariable Long id) {
    	Emprestimo emp = emprestimoService.registrarSeparacaoDoExemplar(id);
    	
    	if (Arrays.asList(StatusConta.ATIVA, StatusConta.EM_ANALISE_EXCLUSAO).contains(emp.getPessoa().getStatusConta())) {
    		EmailDTO emailDTO = new EmailDTO(
    				emp.getPessoa().getEmail().getEndereco(),
    				"Aviso: Livro disponível para retirada",
    				"Olá, " + emp.getPessoa().getNome() + ",\n\n"
    				+ "Informamos que o livro \"" + emp.getExemplar().getEdicao().getTitulo().getNome() + "\" foi separado "
    				+ "e já está disponível para retirada.\n\n"
    				+ "O prazo para retirada é de 2 dias a partir de hoje. "
    				+ "Caso não seja retirado dentro desse prazo, o empréstimo será automaticamente cancelado.\n\n"
    				+ "Atenciosamente,\n"
    				+ "Equipe da Biblioteca");
    		
    		emailService.sendEmail(emailDTO);
    	}
    	
        return ResponseEntity.noContent().build();
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/registrar-retirada-manual/{id}")
    public ResponseEntity<Void> registrarRetiradaDoExemplar(@PathVariable Long id, @Valid @RequestBody DataDevolucaoPrevistaDTO dtDevolucaoPrevistaDTO) {
    	emprestimoService.registrarRetiradaDoExemplar(id, dtDevolucaoPrevistaDTO.dtDevolucaoPrevista());
        return ResponseEntity.noContent().build();
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/registrar-retirada/{id}")
    public ResponseEntity<Void> registrarRetiradaDoExemplar(@PathVariable Long id) {
    	emprestimoService.registrarRetiradaDoExemplar(id);
        return ResponseEntity.noContent().build();
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/renovar-data-de-devolucao-prevista/{id}")
    public ResponseEntity<Void> renovarDataDeDevolucaoPrevista(@PathVariable Long id, @Valid @RequestBody DataDevolucaoPrevistaDTO dtDevolucaoPrevistaDTO) {
    	emprestimoService.renovarDataDeDevolucaoPrevista(id, dtDevolucaoPrevistaDTO.dtDevolucaoPrevista());
        return ResponseEntity.noContent().build();
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/registrar-devolucao/{id}")
    public ResponseEntity<Void> registrarDevolucaoDoExemplar(@PathVariable Long id) {
    	emprestimoService.registrarDevolucaoDoExemplar(id);
        return ResponseEntity.noContent().build();
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/pagar-multa/{id}")
    public ResponseEntity<Void> pagarMulta(@PathVariable Long id) {
    	emprestimoService.pagarMulta(id);
        return ResponseEntity.noContent().build();
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/registrar-perda/{id}")
    public ResponseEntity<Void> registrarPerdaDoExemplar(@PathVariable Long id) {
    	Emprestimo emp = emprestimoService.registrarPerdaDoExemplar(id);
    	
    	if (Arrays.asList(StatusConta.ATIVA, StatusConta.EM_ANALISE_EXCLUSAO).contains(emp.getPessoa().getStatusConta())) {
    		EmailDTO emailDTO = new EmailDTO(
    				emp.getPessoa().getEmail().getEndereco(),
    				"Aviso: Perda de livro registrada",
    				"Olá, " + emp.getPessoa().getNome() + ",\n\n"
    				+ "Foi registrada a perda do livro \"" + emp.getExemplar().getEdicao().getTitulo().getNome() + "\" "
    				+ "referente ao seu empréstimo.\n\n"
    				+ "Nesse caso, é aplicada uma multa fixa. O valor já está lançado no sistema e precisa ser pago "
    				+ "para que sua conta fique em dia com a biblioteca.\n\n"
    				+ "Se tiver dúvidas sobre o valor ou sobre como realizar o pagamento, entre em contato com a equipe da biblioteca.\n\n"
    				+ "Atenciosamente,\n"
    				+ "Equipe da Biblioteca");
    		
    		emailService.sendEmail(emailDTO);
    	}
    	
        return ResponseEntity.noContent().build();
    }
}

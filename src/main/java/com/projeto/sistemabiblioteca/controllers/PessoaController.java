package com.projeto.sistemabiblioteca.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.EmailDTO;
import com.projeto.sistemabiblioteca.DTOs.MotivoInativacaoDoUsuarioDTO;
import com.projeto.sistemabiblioteca.DTOs.MotivoRejeicaoDeCadastroDTO;
import com.projeto.sistemabiblioteca.DTOs.MotivoSolicitacaoExclusaoDTO;
import com.projeto.sistemabiblioteca.DTOs.PageResponseDTO;
import com.projeto.sistemabiblioteca.DTOs.PessoaDTO;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.exceptions.AcessoNegadoException;
import com.projeto.sistemabiblioteca.services.EmailService;
import com.projeto.sistemabiblioteca.services.PessoaService;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class PessoaController {
	
	private final PessoaService pessoaService;
	
	private final EmailService emailService;
	
	public PessoaController(PessoaService pessoaService, EmailService emailService) {
		this.pessoaService = pessoaService;
		this.emailService = emailService;
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping
	public ResponseEntity<PageResponseDTO<Pessoa>> buscarTodos(@RequestParam int pagina, @RequestParam int tamanho) {
		Pageable pageable = PageRequest.of(pagina, tamanho);
		return ResponseEntity.ok(PageResponseDTO.converterParaDTO(pessoaService.buscarTodos(pageable)));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/status/{status}")
	public ResponseEntity<PageResponseDTO<Pessoa>> buscarTodosPorStatus(@PathVariable String status, @RequestParam int pagina, @RequestParam int tamanho) {
		StatusConta statusConta;
		try {
			statusConta = StatusConta.valueOf(status.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Erro: o status informado é inválido.");
		}
		
		Pageable pageable = PageRequest.of(pagina, tamanho);
		
		return ResponseEntity.ok(PageResponseDTO.converterParaDTO(pessoaService.buscarTodosComStatusContaIgualA(statusConta, pageable)));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/clientes")
	public ResponseEntity<PageResponseDTO<Pessoa>> buscarClientes(@RequestParam int pagina, @RequestParam int tamanho) {
		Pageable pageable = PageRequest.of(pagina, tamanho);
		return ResponseEntity.ok(PageResponseDTO.converterParaDTO(pessoaService.buscarTodosComFuncaoIgualA(FuncaoUsuario.CLIENTE, pageable)));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/funcionarios")
	public ResponseEntity<PageResponseDTO<Pessoa>> buscarFuncionarios(@RequestParam int pagina, @RequestParam int tamanho) {
		Pageable pageable = PageRequest.of(pagina, tamanho);
		return ResponseEntity.ok(PageResponseDTO.converterParaDTO(pessoaService.buscarTodosComFuncaoIgualA(FuncaoUsuario.BIBLIOTECARIO, pageable)));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/administradores")
	public ResponseEntity<PageResponseDTO<Pessoa>> buscarAdministradores(@RequestParam int pagina, @RequestParam int tamanho) {
		Pageable pageable = PageRequest.of(pagina, tamanho);
		return ResponseEntity.ok(PageResponseDTO.converterParaDTO(pessoaService.buscarTodosComFuncaoIgualA(FuncaoUsuario.ADMINISTRADOR, pageable)));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/em-analise-aprovacao")
	public ResponseEntity<PageResponseDTO<Pessoa>> buscarClientesEmAnaliseAprovacao(@RequestParam int pagina, @RequestParam int tamanho) {
		Pageable pageable = PageRequest.of(pagina, tamanho);
		return ResponseEntity.ok(PageResponseDTO.converterParaDTO(pessoaService.buscarTodosComStatusContaIgualA(StatusConta.EM_ANALISE_APROVACAO, pageable)));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/em-analise-exclusao")
	public ResponseEntity<PageResponseDTO<Pessoa>> buscarClientesEmAnaliseExclusao(@RequestParam int pagina, @RequestParam int tamanho) {
		Pageable pageable = PageRequest.of(pagina, tamanho);
		return ResponseEntity.ok(PageResponseDTO.converterParaDTO(pessoaService.buscarTodosComStatusContaIgualA(StatusConta.EM_ANALISE_EXCLUSAO, pageable)));
	}
	
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/{id}")
	public ResponseEntity<Pessoa> buscarPorId(@PathVariable Long id, Authentication authentication) {
		if (authentication == null) {
			throw new AcessoNegadoException("Erro: token ausente ou inválido.");
		}
		
		String usernameAutenticado = authentication.getName();
		Pessoa usuarioAutenticado = pessoaService.buscarPorEmail(usernameAutenticado);
		
		if (usuarioAutenticado.getFuncao() == FuncaoUsuario.CLIENTE && !usuarioAutenticado.getIdPessoa().equals(id)) {
			throw new IllegalArgumentException("Erro: um cliente não pode visualizar os dados de outro usuário.");
		}
		
		return ResponseEntity.ok(pessoaService.buscarPorId(id));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/buscar-por-email")
	public ResponseEntity<Pessoa> buscarPorEmail(@RequestParam String email) {
		Email emailFormatoValidado = new Email(email);
		return ResponseEntity.ok(pessoaService.buscarPorEmail(emailFormatoValidado.getEndereco()));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@GetMapping("/buscar-por-cpf")
	public ResponseEntity<Pessoa> buscarPorCpf(@RequestParam String cpf) {
		Cpf cpfFormatoValidado = new Cpf(cpf);
		return ResponseEntity.ok(pessoaService.buscarPorCpf(cpfFormatoValidado.getValor()));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@PostMapping
	public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody PessoaDTO pessoaDTO) {
		pessoaService.cadastrarUsuarioPorAdmin(pessoaDTO);
		return ResponseEntity.ok("Registro efetuado com sucesso.");
	}
	
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO','ADMINISTRADOR')")
	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody PessoaDTO pessoaDTO, Authentication authentication) {
		if (authentication == null) {
			throw new AcessoNegadoException("Erro: token ausente ou inválido.");
		}
		
		String usernameAutenticado = authentication.getName();
		Pessoa usuarioAutenticado = pessoaService.buscarPorEmail(usernameAutenticado);
		
		if (usuarioAutenticado.getIdPessoa().equals(id)) {
			if (usuarioAutenticado.getFuncao() != pessoaDTO.funcao()) {
				throw new IllegalArgumentException("Erro: usuário não pode alterar seu próprio nível de acesso.");
			}
		}
		
		return ResponseEntity.ok(pessoaService.atualizar(id, pessoaDTO, usuarioAutenticado));
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@PutMapping("/em-analise-aprovacao/aprovar-conta/{id}") 
	public ResponseEntity<Void> aprovarUsuario(@PathVariable Long id) {
		Pessoa usuarioAprovado = pessoaService.aprovarConta(id);
		
		EmailDTO emailDTO = new EmailDTO(
				usuarioAprovado.getEmail().getEndereco(), 
				"Cadastro aprovado – Bem-vindo(a) à Biblioteca", 
				"Olá " + usuarioAprovado.getNome() + ",\n\n"
				+ "Seu cadastro na Biblioteca foi aprovado com sucesso.\n"
				+ "Agora você já pode acessar sua conta, consultar o acervo, reservar livros e aproveitar todos os recursos disponíveis.\n\n"
				+ "Estamos felizes em tê-lo(a) conosco!\n\n"
				+ "Atenciosamente,\n"
				+ "Equipe da Biblioteca");
		
		emailService.sendEmail(emailDTO);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
	@PutMapping("/em-analise-aprovacao/rejeitar-conta/{id}") 
	public ResponseEntity<Void> rejeitarUsuario(@PathVariable Long id, @Valid @RequestBody MotivoRejeicaoDeCadastroDTO motivoRejeicaoDeCadastroDTO) {
		Pessoa usuarioRejeitado = pessoaService.rejeitarConta(id);
		
		EmailDTO emailDTO = new EmailDTO(
				usuarioRejeitado.getEmail().getEndereco(), 
				"Conta não aprovada após análise de cadastro", 
				motivoRejeicaoDeCadastroDTO.motivo());
		
		emailService.sendEmail(emailDTO);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('CLIENTE', 'BIBLIOTECARIO')")
	@PutMapping("/solicitar-exclusao-conta/{id}")
	public ResponseEntity<Void> solicitarExclusaoDeUsuario(@PathVariable Long id, @Valid @RequestBody MotivoSolicitacaoExclusaoDTO motivoSolicitacaoExclusaoDTO, Authentication authentication) {
		if (authentication == null) {
			throw new AcessoNegadoException("Erro: token ausente ou inválido.");
		}
		
		String usernameAutenticado = authentication.getName();
		Pessoa usuarioAutenticado = pessoaService.buscarPorEmail(usernameAutenticado);
		
		if (usuarioAutenticado.getFuncao() == FuncaoUsuario.CLIENTE && !usuarioAutenticado.getIdPessoa().equals(id)) {
			throw new IllegalArgumentException("Erro: um cliente não pode solicitar exclusão da conta de outro usuário.");
		}
		
		pessoaService.solicitarExclusaoConta(id, motivoSolicitacaoExclusaoDTO.motivo());
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@PutMapping("/em-analise-exclusao/rejeitar-exclusao-conta/{id}")
	public ResponseEntity<Void> rejeitarSolicitacaoDeExclusaoDeUsuario(@PathVariable Long id) {
		pessoaService.rejeitarSolicitacaoExclusao(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativarUsuario(@PathVariable Long id, @Valid @RequestBody MotivoInativacaoDoUsuarioDTO motivoInativacaoDoUsuarioDTO, Authentication authentication) {
		if (authentication == null) {
			throw new AcessoNegadoException("Erro: token ausente ou inválido.");
		}
		
		String usernameAutenticado = authentication.getName();
		Pessoa usuarioAutenticado = pessoaService.buscarPorEmail(usernameAutenticado);
		
		if (usuarioAutenticado.getIdPessoa().equals(id)) {
			throw new IllegalArgumentException("Erro: administrador não pode inativar sua própria conta.");
		}
		
		Pessoa usuarioInativado = pessoaService.inativar(id);
		
		EmailDTO emailDTO = new EmailDTO(
				usuarioInativado.getEmail().getEndereco(), 
				"Conta inativada – acesso suspenso", 
				motivoInativacaoDoUsuarioDTO.motivo());
		
		emailService.sendEmail(emailDTO);
		
		return ResponseEntity.noContent().build();
	}
}


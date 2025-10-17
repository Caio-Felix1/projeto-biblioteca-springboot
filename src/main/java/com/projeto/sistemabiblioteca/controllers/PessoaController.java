package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.services.PessoaService;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;

@RestController
@RequestMapping("/usuarios")
public class PessoaController {
	
	private final PessoaService pessoaService;
	
	public PessoaController(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	@GetMapping
	public ResponseEntity<List<Pessoa>> buscarTodos() {
		return ResponseEntity.ok(pessoaService.buscarTodos());
	}
	
	@GetMapping("/clientes")
	public ResponseEntity<List<Pessoa>> buscarClientes() {
		return ResponseEntity.ok(pessoaService.buscarTodosComFuncaoIgualA(FuncaoUsuario.CLIENTE));
	}
	
	@GetMapping("/funcionarios")
	public ResponseEntity<List<Pessoa>> buscarFuncionarios() {
		return ResponseEntity.ok(pessoaService.buscarTodosComFuncaoIgualA(FuncaoUsuario.BIBLIOTECARIO));
	}
	
	@GetMapping("/administradores")
	public ResponseEntity<List<Pessoa>> buscarAdministradores() {
		return ResponseEntity.ok(pessoaService.buscarTodosComFuncaoIgualA(FuncaoUsuario.ADMINISTRADOR));
	}
	
	@GetMapping("/em-analise-aprovacao")
	public ResponseEntity<List<Pessoa>> buscarClientesEmAnaliseAprovacao() {
		return ResponseEntity.ok(pessoaService.buscarTodosComStatusContaIgualA(StatusConta.EM_ANALISE_APROVACAO));
	}
	
	@GetMapping("/em-analise-exclusao")
	public ResponseEntity<List<Pessoa>> buscarClientesEmAnaliseExclusao() {
		return ResponseEntity.ok(pessoaService.buscarTodosComStatusContaIgualA(StatusConta.EM_ANALISE_EXCLUSAO));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pessoa> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(pessoaService.buscarPorId(id));
	}
	
	@GetMapping("/buscar-por-email")
	public ResponseEntity<Pessoa> buscarPorEmail(@RequestParam String email) {
		Email emailFormatoValidado = new Email(email);
		return ResponseEntity.ok(pessoaService.buscarPorEmail(emailFormatoValidado.getEndereco()));
	}
	
	@GetMapping("/buscar-por-cpf")
	public ResponseEntity<Pessoa> buscarPorCpf(@RequestParam String cpf) {
		Cpf cpfFormatoValidado = new Cpf(cpf);
		return ResponseEntity.ok(pessoaService.buscarPorCpf(cpfFormatoValidado.getValor()));
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> cadastrarUsuario(@RequestBody Pessoa pessoa) {
		Pessoa novaPessoa = pessoaService.inserir(pessoa);
		return ResponseEntity.ok(novaPessoa);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> atualizarUsuario(@PathVariable Long id, @RequestBody Pessoa pessoa) {
		return ResponseEntity.ok(pessoaService.atualizar(id, pessoa));
	}
	
	@PutMapping("/em-analise-aprovacao/aprovar-conta/{id}") 
	public ResponseEntity<Void> aprovarUsuario(@PathVariable Long id) {
		pessoaService.aprovarConta(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/em-analise-aprovacao/rejeitar-conta/{id}") 
	public ResponseEntity<Void> rejeitarUsuario(@PathVariable Long id) {
		pessoaService.rejeitarConta(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/solicitar-exclusao-conta/{id}")
	public ResponseEntity<Void> solicitarExclusaoDeUsuario(@PathVariable Long id) {
		pessoaService.solicitarExclusaoConta(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/em-analise-exclusao/rejeitar-exclusao-conta/{id}")
	public ResponseEntity<Void> rejeitarSolicitacaoDeExclusaoDeUsuario(@PathVariable Long id) {
		pessoaService.rejeitarSolicitacaoExclusao(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativarUsuario(@PathVariable Long id) {
		pessoaService.inativar(id);
		return ResponseEntity.noContent().build();
	}
}


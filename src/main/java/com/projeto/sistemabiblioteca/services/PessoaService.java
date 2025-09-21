package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;
import com.projeto.sistemabiblioteca.services.exceptions.EmailJaCadastradoException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PessoaService {
	
	private PessoaRepository pessoaRepository;
	
	public PessoaService(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}
	
	public List<Pessoa> buscarTodos() {
		return pessoaRepository.findAll();
	}
	
	public List<Pessoa> buscarTodosComFuncaoIgualA(FuncaoUsuario funcao) {
		return pessoaRepository.findAllByFuncaoEquals(funcao);
	}
	
	public List<Pessoa> buscarTodosComStatusContaIgualA(StatusConta statusConta) {
		return pessoaRepository.findAllByStatusContaEquals(statusConta);
	}
	
	public Pessoa buscarPorId(Long id) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(id);
		if (pessoa.isEmpty()) {
			throw new EntityNotFoundException("Erro: usuário com id correspondente não foi encontrado.");
		}
		return pessoa.get();
	}
	
	public void verificarEmailDisponivel(String email) {
		if (pessoaRepository.existsByEmailEndereco(email)) {
			throw new EmailJaCadastradoException("Erro: email já foi cadastrado.");
		}
	}
	
	public Pessoa buscarPorEmail(String email) {
		Pessoa pessoa = pessoaRepository.findByEmailEndereco(email);
		if (pessoa == null) {
			throw new EntityNotFoundException("Erro: usuário com email correspondente não foi encontrado.");
		}
		return pessoa;
	}
	
	public Pessoa buscarPorCpf(String cpf) {
		Pessoa pessoa = pessoaRepository.findByCpfValor(cpf);
		if (pessoa == null) {
			throw new EntityNotFoundException("Erro: usuário com cpf correspondente não foi encontrado.");
		}
		return pessoa;
	}
	
	public Pessoa inserir(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public void inativar(Long id) {
		Pessoa pessoa = buscarPorId(id);
		pessoa.inativarConta();
		pessoaRepository.save(pessoa);
	}
	
	public Pessoa atualizar(Long id, Pessoa pessoa2) {
		Pessoa pessoa1 = buscarPorId(id);
		atualizarDados(pessoa1, pessoa2);
		return pessoaRepository.save(pessoa1);
	}
	
	private void atualizarDados(Pessoa p1, Pessoa p2) {
		p1.setNome(p2.getNome());
		p1.setCpf(p2.getCpf());
		p1.setSexo(p2.getSexo());
		p1.setFuncao(p2.getFuncao());
		p1.setDtNascimento(p2.getDtNascimento());
		p1.setTelefone(p2.getTelefone());
		p1.setEmail(p2.getEmail());
		p1.setSenhaHash(p2.getSenhaHash());
	}
}

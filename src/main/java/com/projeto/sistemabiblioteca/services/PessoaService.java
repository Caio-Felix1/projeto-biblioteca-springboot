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
	
	public List<Pessoa> buscarPorFuncaoIgualA(FuncaoUsuario funcao) {
		return pessoaRepository.findByFuncaoEquals(funcao);
	}
	
	public List<Pessoa> buscarPorStatusContaIgualA(StatusConta statusConta) {
		return pessoaRepository.findByStatusContaEquals(statusConta);
	}
	
	public Pessoa buscarPorId(Long id) {
		Optional<Pessoa> p = pessoaRepository.findById(id);
		if (p.isEmpty()) {
			throw new EntityNotFoundException("Erro: usuário com email correspondente não foi encontrado.");
		}
		return p.get();
	}
	
	public void verificarEmailDisponivel(String email) {
		if (pessoaRepository.existsByEmailEndereco(email)) {
			throw new EmailJaCadastradoException("Erro: email já foi cadastrado.");
		}
	}
	
	public Pessoa buscarPorEmail(String email) {
		Pessoa p = pessoaRepository.findByEmailEndereco(email);
		if (p == null) {
			throw new EntityNotFoundException("Erro: usuário com email correspondente não foi encontrado.");
		}
		return p;
	}
	
	public Pessoa buscarPorCpf(String cpf) {
		Pessoa p = pessoaRepository.findByCpfValor(cpf);
		if (p == null) {
			throw new EntityNotFoundException("Erro: usuário com cpf correspondente não foi encontrado.");
		}
		return p;
	}
	
	public Pessoa inserir(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public void inativar(Long id) {
		Pessoa p = buscarPorId(id);
		p.inativarConta();
		pessoaRepository.save(p);
	}
	
	public Pessoa atualizar(Long id, Pessoa pessoa) {
		Pessoa p = buscarPorId(id);
		atualizarDados(p, pessoa);
		return pessoaRepository.save(p);
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

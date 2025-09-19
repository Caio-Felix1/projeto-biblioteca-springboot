package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.repositories.PessoaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PessoaService {
	
	private PessoaRepository pessoaRepository;
	
	public PessoaService(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}
	
	public List<Pessoa> findAll() {
		return pessoaRepository.findAll();
	}
	
	public List<Pessoa> findByFuncaoEquals(FuncaoUsuario funcao) {
		return pessoaRepository.findByFuncaoEquals(funcao);
	}
	
	public List<Pessoa> findByStatusContaEquals(StatusConta statusConta) {
		return pessoaRepository.findByStatusContaEquals(statusConta);
	}
	
	public Pessoa findById(Long id) {
		Optional<Pessoa> p = pessoaRepository.findById(id);
		if (p.isEmpty()) {
			throw new EntityNotFoundException("Erro: usuário com email correspondente não foi encontrado.");
		}
		return p.get();
	}
	
	public Pessoa findByEmail(String email) {
		Pessoa p = pessoaRepository.findByEmail(email);
		if (p == null) {
			throw new EntityNotFoundException("Erro: usuário com email correspondente não foi encontrado.");
		}
		return p;
	}
	
	public Pessoa findByCpf(String cpf) {
		Pessoa p = pessoaRepository.findByCpf(cpf);
		if (p == null) {
			throw new EntityNotFoundException("Erro: usuário com cpf correspondente não foi encontrado.");
		}
		return p;
	}
	
	public Pessoa insert(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public void delete(Long id) {
		Pessoa p = findById(id);
		p.inativarConta();
		pessoaRepository.save(p);
	}
	
	public Pessoa update(Long id, Pessoa pessoa) {
		Pessoa p = findById(id);
		updateData(p, pessoa);
		return pessoaRepository.save(p);
	}
	
	private void updateData(Pessoa p1, Pessoa p2) {
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

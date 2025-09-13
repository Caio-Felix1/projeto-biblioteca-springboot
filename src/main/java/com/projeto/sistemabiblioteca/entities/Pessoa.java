package com.projeto.sistemabiblioteca.entities;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_pessoa")
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	private String cpf;
	
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	private FuncaoUsuario funcao;
	
	private LocalDate dtNascimento;
	private String telefone;
	private String email;
	private String senhaHash;
	
	@Enumerated(EnumType.STRING)
	private StatusConta statusConta;
	
	@ManyToOne
	@JoinColumn(name = "id_endereco")
	private Endereco endereco;
	
	public Pessoa() {
		
	}

	public Pessoa(String nome, String cpf, Sexo sexo, FuncaoUsuario funcao, LocalDate dtNascimento, 
			String telefone, String email, String senhaHash, StatusConta statusConta, Endereco endereco) {
		if (statusConta != StatusConta.EM_ANALISE_APROVACAO && statusConta != StatusConta.ATIVA) {
			throw new IllegalArgumentException("Erro: cadastro com status da conta inválido.");
		}
		
		this.nome = nome;
		this.cpf = cpf;
		this.sexo = sexo;
		this.funcao = funcao;
		this.dtNascimento = dtNascimento;
		this.telefone = telefone;
		setEmail(email);
		this.senhaHash = senhaHash;
		this.statusConta = statusConta;
		this.endereco = endereco;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public FuncaoUsuario getFuncao() {
		return funcao;
	}

	public void setFuncao(FuncaoUsuario funcao) {
		this.funcao = funcao;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email.matches("[a-z0-9]+@[a-z]+\\.com(\\.br)?")) {
			throw new IllegalArgumentException("Erro: email no formato inválido.");
		}
		this.email = email;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}
	
	public StatusConta getStatusConta() {
		return statusConta;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public void inativarConta() {
		if (statusConta != StatusConta.ATIVA && statusConta != StatusConta.EM_ANALISE_EXCLUSAO) {
			throw new IllegalStateException("Erro: a conta não está ativa ou em análise para exclusão.");
		}
		
		statusConta = StatusConta.INATIVA;
	}
	
	public void aprovarConta() {
		if (statusConta != StatusConta.EM_ANALISE_APROVACAO) {
			throw new IllegalStateException("Erro: a conta não está em análise para aprovação do cadastro.");
		}
		
		statusConta = StatusConta.ATIVA;
	}
	
	public void rejeitarConta() {
		if (statusConta != StatusConta.EM_ANALISE_APROVACAO) {
			throw new IllegalStateException("Erro: a conta não está em análise para aprovação do cadastro.");
		}
		
		statusConta = StatusConta.REJEITADA;
	}
	
	public void solicitarExclusaoConta() {
		if (statusConta != StatusConta.ATIVA && statusConta != StatusConta.EM_ANALISE_EXCLUSAO) {
			throw new IllegalStateException("Erro: a conta não está ativa.");
		}
		
		statusConta = StatusConta.EM_ANALISE_EXCLUSAO;
	}
	
	public void rejeitarSolicitacaoExclusao() {
		if (statusConta != StatusConta.EM_ANALISE_EXCLUSAO) {
			throw new IllegalStateException("Erro: a conta não está em análise para exclusão.");
		}
		
		statusConta = StatusConta.ATIVA;
	}
}

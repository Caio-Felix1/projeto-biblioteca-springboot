package com.projeto.sistemabiblioteca.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

import jakarta.persistence.Embedded;
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
@Table(name = "pessoa")
public class Pessoa implements UserDetails {
	
	private static final int IDADE_MINIMA = 18;
	private static final int IDADE_MAXIMA = 120;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPessoa;
	
	private String nome;
	
	@Embedded
	private Cpf cpf;
	
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	private FuncaoUsuario funcao;
	
	private LocalDate dtNascimento;
	
	@Embedded
	private Telefone telefone;
	
	@Embedded
	private Email email;
	
	private String senhaHash;
	
	@Enumerated(EnumType.STRING)
	private StatusConta statusConta;
	
	private String motivoSolicitacaoExclusao;
	
	@ManyToOne
	@JoinColumn(name = "id_endereco")
	private Endereco endereco;
	
	protected Pessoa() {
		
	}

	public Pessoa(String nome, Cpf cpf, Sexo sexo, FuncaoUsuario funcao, LocalDate dtNascimento, LocalDate hoje,
			Telefone telefone, Email email, String senhaHash, StatusConta statusConta, Endereco endereco) {
		if (statusConta != StatusConta.EM_ANALISE_APROVACAO && statusConta != StatusConta.ATIVA) {
			throw new IllegalArgumentException("Erro: cadastro com status da conta inválido.");
		}
		validarDataNascimento(dtNascimento, hoje);

		this.nome = nome;
		this.cpf = cpf;
		this.sexo = sexo;
		this.funcao = funcao;
		this.dtNascimento = dtNascimento;
		this.telefone = telefone;
		this.email = email;
		this.senhaHash = senhaHash;
		this.statusConta = statusConta;
		this.endereco = endereco;
	}

	public Pessoa(Email email, String encryptedPassword, FuncaoUsuario funcao) {
		this.email = email;
		this.senhaHash = encryptedPassword; // campo que você usa para guardar a senha
		this.funcao = funcao;
		this.statusConta = StatusConta.EM_ANALISE_APROVACAO; // opcional, pode definir um padrão
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cpf getCpf() {
		return cpf;
	}

	public void setCpf(Cpf cpf) {
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

	private void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
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
	
	public String getMotivoSolicitacaoExclusao() {
		return motivoSolicitacaoExclusao;
	}

	public void setMotivoSolicitacaoExclusao(String motivoSolicitacaoExclusao) {
		this.motivoSolicitacaoExclusao = motivoSolicitacaoExclusao;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public void definirDataNascimento(LocalDate dtNascimento, LocalDate hoje) {
		validarDataNascimento(dtNascimento, hoje);
		setDtNascimento(dtNascimento);
	}
	
	private void validarDataNascimento(LocalDate dtNascimento, LocalDate hoje) {
		if (dtNascimento == null) {
			throw new IllegalArgumentException("Erro: a data de nascimento não pode ser nula.");
		}
		if (hoje == null) {
			throw new IllegalArgumentException("Erro: o parâmetro 'hoje' não pode ser nulo.");
		}
		if (dtNascimento.isAfter(hoje.minusYears(IDADE_MINIMA))) {
			throw new IllegalArgumentException("Erro: usuário deve ter no mínimo " + IDADE_MINIMA  + " anos de idade.");
		}
		if (dtNascimento.isBefore(hoje.minusYears(IDADE_MAXIMA))) {
			throw new IllegalArgumentException("Erro: usuário deve ter no máximo " + IDADE_MAXIMA  + " anos de idade.");
		}
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
		if (statusConta == StatusConta.EM_ANALISE_EXCLUSAO) {
			throw new IllegalStateException("Erro: já foi solicitado a exclusão da conta.");
		}
		if (statusConta != StatusConta.ATIVA) {
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.funcao == FuncaoUsuario.ADMINISTRADOR) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
		}
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return this.senhaHash;
	}

	@Override
	public String getUsername() {
		return email.getEndereco();
	}
	
	@Override
	public boolean isEnabled() {
		return this.getStatusConta() == StatusConta.ATIVA || this.getStatusConta() == StatusConta.EM_ANALISE_EXCLUSAO;
	}
}

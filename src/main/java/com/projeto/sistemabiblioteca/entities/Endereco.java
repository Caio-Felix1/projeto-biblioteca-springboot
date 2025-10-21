package com.projeto.sistemabiblioteca.entities;

import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.interfaces.Ativavel;

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
@Table(name = "endereco")
public class Endereco implements Ativavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEndereco;
	
	private String nomeLogradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	
	@Enumerated(EnumType.STRING)
	private StatusAtivo status;
	
	@ManyToOne
	@JoinColumn(name = "id_estado")
	private Estado estado;
	
	protected Endereco() {
		
	}

	public Endereco(String nomeLogradouro, String numero, String complemento, String bairro, String cep, String cidade, Estado estado) {
		this.nomeLogradouro = nomeLogradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
		ativar();
	}

	public Long getIdEndereco() {
		return idEndereco;
	}

	public String getNomeLogradouro() {
		return nomeLogradouro;
	}

	public void setNomeLogradouro(String nomeLogradouro) {
		this.nomeLogradouro = nomeLogradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	} 	
	
	@Override
	public StatusAtivo getStatusAtivo() {
		return status;
	}

	@Override
	public void ativar() {
		status = StatusAtivo.ATIVO;
	}

	@Override
	public void inativar() {
		status = StatusAtivo.INATIVO;
	}
}

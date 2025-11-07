package com.projeto.sistemabiblioteca.entities;

import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.interfaces.Ativavel;

import jakarta.persistence.Column;
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
@Table(name = "estado")
public class Estado implements Ativavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEstado;
	
	@Column(unique = true)
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private StatusAtivo status;
	
	@ManyToOne
	@JoinColumn(name = "id_pais")
	private Pais pais;
	
	protected Estado() {
		
	}
	
	public Estado(String nome, Pais pais) {
		this.nome = nome;
		this.pais = pais;
		ativar();
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
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

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
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria implements Ativavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategoria;
	
	@Column(unique = true)
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private StatusAtivo status;

	protected Categoria() {
		
	}

	public Categoria(String nome) {
		this.nome = nome;
		ativar();
	}

	public Long getIdCategoria() {
		return idCategoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

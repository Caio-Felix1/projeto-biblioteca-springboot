package com.projeto.sistemabiblioteca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estado")
public class Estado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEstado;
	
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "id_pais")
	private Pais pais;
	
	protected Estado() {
		
	}
	
	public Estado(String nome, Pais pais) {
		this.nome = nome;
		this.pais = pais;
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
}

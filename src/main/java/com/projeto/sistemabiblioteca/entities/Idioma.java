package com.projeto.sistemabiblioteca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "idioma")
public class Idioma {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idIdioma;
	
	private String nome;
	
	protected Idioma() {
		
	}

	public Idioma(String nome) {
		this.nome = nome;
	}
	
	public Long getIdIdioma() {
		return idIdioma;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}

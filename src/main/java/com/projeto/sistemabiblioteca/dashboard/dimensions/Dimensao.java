package com.projeto.sistemabiblioteca.dashboard.dimensions;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Dimensao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long surrogateKey;
	
	private Long idNatural;
	
	public Dimensao() {
		
	}
	
	public Dimensao(Long idNatural) {
		this.idNatural = idNatural;
	}

	public Long getSurrogateKey() {
		return surrogateKey;
	}

	public Long getIdNatural() {
		return idNatural;
	}

	public void setIdNatural(Long idNatural) {
		this.idNatural = idNatural;
	}
}

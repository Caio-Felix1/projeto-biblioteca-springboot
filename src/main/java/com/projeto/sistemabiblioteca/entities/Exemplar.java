package com.projeto.sistemabiblioteca.entities;

import com.projeto.sistemabiblioteca.entities.enums.EstadoExemplar;

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
@Table(name = "tb_exemplar")
public class Exemplar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private boolean alugado;
	
	@Enumerated(EnumType.STRING)
	private EstadoExemplar estado;
	
	@ManyToOne
	@JoinColumn(name = "id_edicao")
	private Edicao edicao;
	
	public Exemplar() {
		
	}
	
	public Exemplar(boolean alugado, EstadoExemplar estado, Edicao edicao) {
		this.alugado = alugado;
		this.estado = estado;
		this.edicao = edicao;
	}
	
	public Long getId() {
		return id;
	}

	public boolean getAlugado() {
		return alugado;
	}

	public void setAlugado(boolean alugado) {
		this.alugado = alugado;
	}

	public EstadoExemplar getEstado() {
		return estado;
	}

	public void setEstado(EstadoExemplar estado) {
		this.estado = estado;
	}
	
	public Edicao getEdicao() {
		return edicao;
	}

	public void setEdicao(Edicao edicao) {
		this.edicao = edicao;
	}
}

package com.projeto.sistemabiblioteca.validation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cpf extends FormatoValidation {
	
	@Column(name = "cpf")
	private String valor;
	
	public Cpf() {
		super();
	}
	
	public Cpf(String regex, String valor) {
		super(regex);
		setValor(valor);
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		if (valor == null || validarFormato(valor) == false) {
			throw new IllegalArgumentException("Erro: cpf com formato inválido.");
		}
		this.valor = valor;
	}

	@Override
	public boolean validarFormato(String entrada) {
		if (entrada == null || !entrada.matches(getRegex())) {
			return false;
		}
		return true;
	}
}

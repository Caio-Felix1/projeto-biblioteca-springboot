package com.projeto.sistemabiblioteca.validation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email extends FormatoValidation {
	
	@Column(name = "email")
	private String endereco;
	
	public Email() {
		super();
	}
	
	public Email(String regex, String endereco) {
		super(regex);
		setEndereco(endereco);
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		if (endereco == null || validarFormato(endereco) == false) {
			throw new IllegalArgumentException("Erro: email com formato inválido.");
		}
		this.endereco = endereco;
	}

	@Override
	public boolean validarFormato(String entrada) {
		if (entrada == null || !entrada.matches(getRegex())) {
			return false;
		}
		return true;
	}
}

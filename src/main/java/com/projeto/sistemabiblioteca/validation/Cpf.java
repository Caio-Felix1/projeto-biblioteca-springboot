package com.projeto.sistemabiblioteca.validation;

import java.util.Objects;
import java.util.regex.Pattern;

import com.projeto.sistemabiblioteca.exceptions.CpfInvalidoException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cpf {
	
	private static final Pattern CPF_REGEX =
			Pattern.compile("^[0-9]{11}$");
	
	@Column(name = "cpf", nullable = false, unique = true)
	private String valor;
	
	protected Cpf() {
		
	}
	
	public Cpf(String valor) {
		if (valor == null || !CPF_REGEX.matcher(valor).matches()) {
			throw new CpfInvalidoException("Erro: CPF com formato inválido.");
		}
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cpf other = (Cpf) obj;
		return Objects.equals(valor, other.valor);
	}
}

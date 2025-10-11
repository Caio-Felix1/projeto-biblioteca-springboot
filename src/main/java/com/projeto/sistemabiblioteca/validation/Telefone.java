package com.projeto.sistemabiblioteca.validation;

import java.util.Objects;
import java.util.regex.Pattern;

import com.projeto.sistemabiblioteca.exceptions.TelefoneInvalidoException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public final class Telefone {

	private static final Pattern TELEFONE_REGEX =
			Pattern.compile("^[0-9]{10,11}$");

	@Column(name = "telefone", nullable = false)
	private String numero;
	
	protected Telefone() {

	}

	public Telefone(String numero) {
		if (numero == null || !TELEFONE_REGEX.matcher(numero).matches()) {
			throw new TelefoneInvalidoException("Erro: telefone com formato inválido.");
		}
		this.numero = numero;
	}

	public String getNumero() {
		return numero;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Telefone other = (Telefone) obj;
		return Objects.equals(numero, other.numero);
	}
}

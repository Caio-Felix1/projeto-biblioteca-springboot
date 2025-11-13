package com.projeto.sistemabiblioteca.validation;

import com.projeto.sistemabiblioteca.exceptions.EmailInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public final class Email {

	private static final Pattern EMAIL_REGEX =
			Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com(\\.br)?$");

	@Column(name = "email", nullable = false, unique = true)
	private String endereco;
	
	protected Email() {

	}

	public Email(String endereco) {
		if (endereco == null || !EMAIL_REGEX.matcher(endereco).matches()) {
			throw new EmailInvalidoException("Erro: email com formato inválido.");
		}
		this.endereco = endereco;
	}

	public String getEndereco() {
		return endereco;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Email email)) return false;
		return endereco.equals(email.endereco);
	}

	@Override
	public int hashCode() {
		return endereco.hashCode();
	}

	@Override
	public String toString() {
		return endereco;
	}



}

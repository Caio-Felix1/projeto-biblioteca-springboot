package com.projeto.sistemabiblioteca.validation;

public class ValidacaoUtils {
	
	public static boolean isFormatoEmailValido(String email) {
		if (email == null || !email.matches("[a-z0-9]+@[a-z]+\\.com(\\.br)?")) {
			return false;
		}
		return true;
	}
	
	public static boolean isFormatoCpfValido(String cpf) {
		if (cpf == null || !cpf.matches("\\d{11}")) {
			return false;
		}
		return true;
	}
}

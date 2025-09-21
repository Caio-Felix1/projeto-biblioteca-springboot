package com.projeto.sistemabiblioteca.validation;

public abstract class FormatoValidation {
	
	private String regex;
	
	public FormatoValidation() {
		
	}
	
	public FormatoValidation(String regex) {
		this.regex = regex;
	}
	
	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public abstract boolean validarFormato(String entrada);
}

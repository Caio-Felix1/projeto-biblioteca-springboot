package com.projeto.sistemabiblioteca.entities.interfaces;

import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

public interface Ativavel {
	
	StatusAtivo getStatusAtivo();
	
	void ativar(); 
	
	void inativar(); 
}

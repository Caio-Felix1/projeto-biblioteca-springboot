package com.projeto.sistemabiblioteca.entities.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ArmazenamentoService {
	String salvar(MultipartFile file);
}

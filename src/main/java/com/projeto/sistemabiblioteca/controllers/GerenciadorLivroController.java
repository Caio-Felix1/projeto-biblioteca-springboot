package com.projeto.sistemabiblioteca.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.LivroDTO;
import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.services.GerenciadorLivroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("livros/gerenciamento")
public class GerenciadorLivroController {
	
	private GerenciadorLivroService gerenciadorLivroService;
	
	public GerenciadorLivroController(GerenciadorLivroService gerenciadorLivroService) {
		this.gerenciadorLivroService = gerenciadorLivroService;
	}

	@PostMapping
	public ResponseEntity<Exemplar> cadastrarLivro(@Valid @RequestBody LivroDTO livroDTO) {
		Exemplar exemplar = gerenciadorLivroService.cadastrarLivro(livroDTO);
		return ResponseEntity.ok(exemplar);
	}
}

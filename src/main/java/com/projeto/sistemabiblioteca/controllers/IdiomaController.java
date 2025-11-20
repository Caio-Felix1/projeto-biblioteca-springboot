package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.DTOs.IdiomaDTO;
import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.IdiomaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/idiomas")
public class IdiomaController {

    private final IdiomaService idiomaService;

    public IdiomaController(IdiomaService idiomaService) {
        this.idiomaService = idiomaService;
    }

	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Idioma>> listarTodos() {
        return ResponseEntity.ok(idiomaService.buscarTodos());
    }
	
    @GetMapping("/ativos")
    public ResponseEntity<List<Idioma>> listarAtivos() {
        return ResponseEntity.ok(idiomaService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO));
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/inativos")
    public ResponseEntity<List<Idioma>> listarInativos() {
        return ResponseEntity.ok(idiomaService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO));
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Idioma> buscarPorId(@PathVariable Long id) {
    	Idioma idioma = idiomaService.buscarPorId(id);
        return ResponseEntity.ok(idioma);
    }

	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<Idioma> criar(@RequestBody IdiomaDTO idiomaDTO) {
    	Idioma idioma = new Idioma(idiomaDTO.nome());
        Idioma salvo = idiomaService.inserir(idioma);
        //return new ResponseEntity<>(salvo, HttpStatus.CREATED);
        return ResponseEntity.ok(salvo);
    }

	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Idioma> atualizar(@PathVariable Long id, @RequestBody IdiomaDTO idiomaDTO) {
    	Idioma idioma = new Idioma(idiomaDTO.nome());
    	Idioma atualizado = idiomaService.atualizar(id, idioma);
        return ResponseEntity.ok(atualizado);
    }

	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
    	idiomaService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}

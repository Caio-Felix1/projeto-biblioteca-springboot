package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.PaisDTO;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.PaisService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/paises")
public class PaisController {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        this.paisService = paisService;
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Pais>> buscarTodos() {
        return ResponseEntity.ok(paisService.buscarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Pais>> buscarAtivos() {
        return ResponseEntity.ok(paisService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/inativos")
    public ResponseEntity<List<Pais>> buscarInativos() {
        return ResponseEntity.ok(paisService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Pais> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(paisService.buscarPorId(id));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<Pais> cadastrar(@Valid @RequestBody PaisDTO paisDTO) {
        Pais novoPais = paisService.inserir(new Pais(paisDTO.nome()));
        return ResponseEntity.ok(novoPais);
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Pais> atualizar(@PathVariable Long id, @Valid @RequestBody PaisDTO paisDTO) {
        return ResponseEntity.ok(paisService.atualizar(id, new Pais(paisDTO.nome())));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        paisService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}

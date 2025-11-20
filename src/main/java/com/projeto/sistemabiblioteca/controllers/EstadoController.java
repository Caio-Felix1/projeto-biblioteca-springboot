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

import com.projeto.sistemabiblioteca.DTOs.EstadoDTO;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.EstadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Estado>> buscarTodos() {
        return ResponseEntity.ok(estadoService.buscarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Estado>> buscarAtivos() {
        return ResponseEntity.ok(estadoService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/inativos")
    public ResponseEntity<List<Estado>> buscarInativos() {
        return ResponseEntity.ok(estadoService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO));
    }
    
    @GetMapping("/buscar-por-pais/{id}")
    public ResponseEntity<List<Estado>> buscarTodosFiltrandoPorPais(Long id) {
    	return ResponseEntity.ok(estadoService.buscarTodosFiltrandoPorPais(id));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estadoService.buscarPorId(id));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<Estado> cadastrar(@Valid @RequestBody EstadoDTO estadoDTO) {
        Estado novoEstado = estadoService.cadastrar(estadoDTO);
        return ResponseEntity.ok(novoEstado);
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long id, @Valid @RequestBody EstadoDTO estadoDTO) {
        return ResponseEntity.ok(estadoService.atualizar(id, estadoDTO));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        estadoService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}

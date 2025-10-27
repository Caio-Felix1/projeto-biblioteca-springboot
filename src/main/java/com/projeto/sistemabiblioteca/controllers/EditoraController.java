package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.EditoraDTO;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.EditoraService;

@RestController
@RequestMapping("/editoras")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @GetMapping
    public ResponseEntity<List<Editora>> buscarTodos(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(editoraService.buscarTodosComNomeContendo(nome));
        }
        return ResponseEntity.ok(editoraService.buscarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Editora>> buscarAtivos() {
        return ResponseEntity.ok(editoraService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO));
    }
    
    @GetMapping("/inativos")
    public ResponseEntity<List<Editora>> buscarInativos() {
        return ResponseEntity.ok(editoraService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editora> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(editoraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Editora> cadastrar(@RequestBody EditoraDTO editoraDTO) {
    	Editora editora = new Editora(editoraDTO.nome());
        Editora novaEditora = editoraService.inserir(editora);
        return ResponseEntity.ok(novaEditora);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editora> atualizar(@PathVariable Long id, @RequestBody EditoraDTO editoraDTO) {
    	Editora editora = new Editora(editoraDTO.nome());
        return ResponseEntity.ok(editoraService.atualizar(id, editora));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        editoraService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}

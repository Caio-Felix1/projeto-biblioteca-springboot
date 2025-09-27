package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.services.EditoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editoras")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @GetMapping
    public ResponseEntity<List<Editora>> getAll(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(editoraService.buscarTodosComNomeContendo(nome));
        }
        return ResponseEntity.ok(editoraService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editora> getById(@PathVariable Long id) {
        return ResponseEntity.ok(editoraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Editora> create(@RequestBody Editora editora) {
        Editora novaEditora = editoraService.inserir(editora);
        return ResponseEntity.ok(novaEditora);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editora> update(@PathVariable Long id, @RequestBody Editora editora) {
        return ResponseEntity.ok(editoraService.atualizar(id, editora));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        editoraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

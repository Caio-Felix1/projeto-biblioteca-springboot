package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.services.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(categoriaService.buscarTodosComNomeContendo(nome));
        }
        return ResponseEntity.ok(categoriaService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> create(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.inserir(categoria);
        return ResponseEntity.ok(novaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.atualizar(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
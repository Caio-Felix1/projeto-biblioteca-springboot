package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.services.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/autores")
public class AutorController {
    protected final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }


    @GetMapping
    public ResponseEntity<List<Autor>> buscarTodos(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(autorService.buscarTodosComNomeContendo(nome));
        }
        return ResponseEntity.ok(autorService.buscarTodos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Long id) {
            return ResponseEntity.ok(autorService.buscarPorId(id));
    }


    @PostMapping
    public ResponseEntity<Autor> cadastrar(@RequestBody Autor autor) {
        Autor novoAutor = autorService.inserir(autor);
        return ResponseEntity.ok(novoAutor);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizar(@PathVariable Long id, @RequestBody Autor autor) {
            return ResponseEntity.ok(autorService.atualizar(id, autor));
    }

    // Deletar autor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
            autorService.inativar(id);
            return ResponseEntity.noContent().build();
    }

}

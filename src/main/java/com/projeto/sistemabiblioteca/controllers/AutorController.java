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

import com.projeto.sistemabiblioteca.DTOs.AutorDTO;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.services.AutorService;

import jakarta.validation.Valid;


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
    public ResponseEntity<Autor> cadastrar(@Valid @RequestBody AutorDTO autorDTO) {
    	Autor autor = new Autor(autorDTO.nome());
        Autor novoAutor = autorService.inserir(autor);
        return ResponseEntity.ok(novoAutor);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizar(@PathVariable Long id, @Valid @RequestBody AutorDTO autorDTO) {
    	Autor autor = new Autor(autorDTO.nome());
    	return ResponseEntity.ok(autorService.atualizar(id, autor));
    }

    // Deletar autor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
            autorService.inativar(id);
            return ResponseEntity.noContent().build();
    }

}

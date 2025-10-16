package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.IdiomaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/idiomas")
public class IdiomaController {

    private final IdiomaService idiomaService;

    public IdiomaController(IdiomaService idiomaService) {
        this.idiomaService = idiomaService;
    }


    @GetMapping
    public List<Idioma> listarTodos() {
        return idiomaService.buscarTodos();
    }


    @GetMapping("/ativos")
    public List<Idioma> listarAtivos() {
        return idiomaService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Idioma> buscarPorId(@PathVariable Long id) {
        try {
            Idioma idioma = idiomaService.buscarPorId(id);
            return ResponseEntity.ok(idioma);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Idioma> criar(@RequestBody Idioma idioma) {
        Idioma salvo = idiomaService.inserir(idioma);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Idioma> atualizar(@PathVariable Long id, @RequestBody Idioma idioma) {
        try {
            Idioma atualizado = idiomaService.atualizar(id, idioma);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            idiomaService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

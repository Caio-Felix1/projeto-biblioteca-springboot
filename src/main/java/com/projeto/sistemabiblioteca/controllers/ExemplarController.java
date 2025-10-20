package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

import com.projeto.sistemabiblioteca.DTOs.ExemplarCreateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;
import com.projeto.sistemabiblioteca.services.ExemplarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/exemplares")
public class ExemplarController {

    private final ExemplarService exemplarService;

    public ExemplarController(ExemplarService exemplarService) {
        this.exemplarService = exemplarService;
    }


    @GetMapping
    public List<Exemplar> listarTodos() {
        return exemplarService.buscarTodos();
    }


    @GetMapping("/status/{status}")
    public List<Exemplar> listarPorStatus(@PathVariable StatusExemplar status) {
        return exemplarService.buscarTodosComStatusIgualA(status);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Exemplar> buscarPorId(@PathVariable Long id) {
        try {
            Exemplar exemplar = exemplarService.buscarPorId(id);
            return ResponseEntity.ok(exemplar);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Exemplar> criar(@Valid @RequestBody ExemplarCreateDTO dto) {
        Exemplar novoExemplar = exemplarService.inserir(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoExemplar);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Exemplar> atualizar(@PathVariable Long id, @Valid @RequestBody Exemplar exemplar) {
        try {
            Exemplar atualizado = exemplarService.atualizar(id, exemplar);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            exemplarService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

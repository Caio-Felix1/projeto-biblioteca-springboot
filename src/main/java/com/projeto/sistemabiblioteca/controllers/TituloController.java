package com.projeto.sistemabiblioteca.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.TituloDTO;
import com.projeto.sistemabiblioteca.DTOs.TituloUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.TituloService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/titulos")
public class TituloController {

    private final TituloService tituloService;

    public TituloController(TituloService tituloService) {
        this.tituloService = tituloService;
    }


    @GetMapping
    public ResponseEntity<List<Titulo>> listarTodos() {
        return ResponseEntity.ok(tituloService.buscarTodos());
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<Titulo>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(tituloService.buscarTodosComNomeContendo(nome));
    }


    @GetMapping("/ativos")
    public ResponseEntity<List<Titulo>> listarAtivos() {
        return ResponseEntity.ok(tituloService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Titulo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tituloService.buscarPorId(id));
    }


    @PostMapping
    public ResponseEntity<Titulo> criar(@Valid @RequestBody TituloDTO tituloDTO) {
        Titulo novo = tituloService.cadastrarTitulo(tituloDTO);
        return ResponseEntity.ok(novo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Titulo> atualizar(@PathVariable Long id, @Valid @RequestBody TituloUpdateDTO tituloUpdateDTO) {
        Titulo atualizado = tituloService.atualizar(id, tituloUpdateDTO);
        return ResponseEntity.ok(atualizado);
    }


    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        tituloService.inativar(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{idTitulo}/categorias/{idCategoria}")
    public ResponseEntity<Void> adicionarCategoria(@PathVariable Long idTitulo, @PathVariable Long idCategoria) {
        tituloService.adicionarCategoria(idTitulo, idCategoria);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{idTitulo}/categorias")
    public ResponseEntity<Void> adicionarCategorias(@PathVariable Long idTitulo, @RequestBody Set<Long> idsCategorias) {
        tituloService.adicionarCategorias(idTitulo, idsCategorias);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{idTitulo}/categorias/{idCategoria}")
    public ResponseEntity<Void> removerCategoria(@PathVariable Long idTitulo, @PathVariable Long idCategoria) {
        tituloService.removerCategoria(idTitulo, idCategoria);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{idTitulo}/categorias")
    public ResponseEntity<Void> removerCategorias(@PathVariable Long idTitulo, @RequestBody Set<Long> idsCategorias) {
        tituloService.removerCategorias(idTitulo, idsCategorias);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{idTitulo}/autores/{idAutor}")
    public ResponseEntity<Void> adicionarAutor(@PathVariable Long idTitulo, @PathVariable Long idAutor) {
        tituloService.adicionarAutor(idTitulo, idAutor);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{idTitulo}/autores")
    public ResponseEntity<Void> adicionarAutores(@PathVariable Long idTitulo, @RequestBody Set<Long> idsAutores) {
        tituloService.adicionarAutores(idTitulo, idsAutores);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idTitulo}/autores/{idAutor}")
    public ResponseEntity<Void> removerAutor(@PathVariable Long idTitulo, @PathVariable Long idAutor) {
        tituloService.removerAutor(idTitulo, idAutor);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{idTitulo}/autores")
    public ResponseEntity<Void> removerAutores(@PathVariable Long idTitulo, @RequestBody Set<Long> idsAutores) {
        tituloService.removerAutores(idTitulo, idsAutores);
        return ResponseEntity.noContent().build();
    }
}

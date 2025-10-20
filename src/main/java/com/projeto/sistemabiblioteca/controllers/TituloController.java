package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.TituloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Titulo> criar(@RequestBody Titulo titulo) {
        Titulo novo = tituloService.inserir(titulo);
        return ResponseEntity.ok(novo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Titulo> atualizar(@PathVariable Long id, @RequestBody Titulo titulo) {
        Titulo atualizado = tituloService.atualizar(id, titulo);
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


    @DeleteMapping("/{idTitulo}/categorias/{idCategoria}")
    public ResponseEntity<Void> removerCategoria(@PathVariable Long idTitulo, @PathVariable Long idCategoria) {
        tituloService.removerCategoria(idTitulo, idCategoria);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{idTitulo}/autores/{idAutor}")
    public ResponseEntity<Void> adicionarAutor(@PathVariable Long idTitulo, @PathVariable Long idAutor) {
        tituloService.adicionarAutor(idTitulo, idAutor);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{idTitulo}/autores/{idAutor}")
    public ResponseEntity<Void> removerAutor(@PathVariable Long idTitulo, @PathVariable Long idAutor) {
        tituloService.removerAutor(idTitulo, idAutor);
        return ResponseEntity.noContent().build();
    }
}

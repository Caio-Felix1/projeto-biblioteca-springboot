package com.projeto.sistemabiblioteca.controllers;

import static com.projeto.sistemabiblioteca.entities.enums.StatusAtivo.ATIVO;
import static com.projeto.sistemabiblioteca.entities.enums.StatusAtivo.INATIVO;

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

import com.projeto.sistemabiblioteca.DTOs.EdicaoDTO;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.services.EdicaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/edicoes")
public class EdicaoController {
	
    private final EdicaoService edicaoService;

    public EdicaoController(EdicaoService edicaoService) {
        this.edicaoService = edicaoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Edicao>> listarTodos() {
        return ResponseEntity.ok(edicaoService.buscarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Edicao>> listarAtivos() {
        List<Edicao> ativos = edicaoService.buscarTodosComStatusIgualA(ATIVO);
        return ResponseEntity.ok(ativos);
    }
    
    @GetMapping("/inativos")
    public ResponseEntity<List<Edicao>> listarInativos() {
        List<Edicao> inativos = edicaoService.buscarTodosComStatusIgualA(INATIVO);
        return ResponseEntity.ok(inativos);
    }
    
    @GetMapping("/buscar-por-autor")
    public ResponseEntity<List<Edicao>> listarTodosPeloAutor(@RequestParam String nomeAutor) {
        List<Edicao> edicoes = edicaoService.buscarTodosComAutorComNomeContendo(nomeAutor);
        return ResponseEntity.ok(edicoes);
    }
    
    @GetMapping("/buscar-por-titulo")
    public ResponseEntity<List<Edicao>> listarTodosPeloTitulo(@RequestParam String nomeTitulo) {
        List<Edicao> edicoes = edicaoService.buscarTodosComTituloComNomeContendo(nomeTitulo);
        return ResponseEntity.ok(edicoes);
    }
    
    @GetMapping("/buscar-por-categoria/{id}")
    public ResponseEntity<List<Edicao>> listarTodosPelaCategoria(@PathVariable Long id) {
        List<Edicao> edicoes = edicaoService.buscarTodosComCategoriaComIdIgualA(id);
        return ResponseEntity.ok(edicoes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Edicao> buscar(@PathVariable Long id) {
        Edicao e = edicaoService.buscarPorId(id);
        return ResponseEntity.ok(e);
    }
    
    @PostMapping
    public ResponseEntity<Edicao> criar(@Valid @RequestBody EdicaoDTO edicaoDTO) {
        Edicao nova = edicaoService.cadastrarEdicao(edicaoDTO);
        return ResponseEntity.ok(nova);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Edicao> atualizar(@PathVariable Long id, @Valid @RequestBody EdicaoDTO edicaoDTO) {
        Edicao atualizada = edicaoService.atualizar(id, edicaoDTO);
        return ResponseEntity.ok(atualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
    	edicaoService.inativar(id);
    	return ResponseEntity.noContent().build();
    }
}

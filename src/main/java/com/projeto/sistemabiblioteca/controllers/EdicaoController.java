package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.DTOs.EdicaoCreateDTO;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.services.EdicaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.projeto.sistemabiblioteca.entities.enums.StatusAtivo.ATIVO;

@RestController
@RequestMapping("/edicoes")
public class EdicaoController {
    private final EdicaoService edicaoService;

    public EdicaoController(EdicaoService edicaoService) {
        this.edicaoService = edicaoService;
    }
    @GetMapping("/ativos")
    public ResponseEntity<List<Edicao>> listarAtivos() {
        List<Edicao> ativos = edicaoService.buscarTodosComStatusIgualA(ATIVO);
        return ResponseEntity.ok(ativos);
    }
    @PostMapping
    public ResponseEntity<Edicao> criar( @RequestBody  EdicaoCreateDTO edicao) {
        Edicao nova = edicaoService.inserir(edicao);
        return ResponseEntity.ok(nova);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Edicao> atualizar(@PathVariable Long id, @RequestBody Edicao edicao) {
        Edicao atualizada = edicaoService.atualizar(id, edicao);
        return ResponseEntity.ok(atualizada);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Edicao> buscar(@PathVariable Long id) {
        Edicao e = edicaoService.buscarPorId(id);
        return ResponseEntity.ok(e);
    }
}

package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

import com.projeto.sistemabiblioteca.DTOs.ExemplarDTO;
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
    public ResponseEntity<List<Exemplar>> listarTodos() {
    	List<Exemplar> exemplares = exemplarService.buscarTodos();
        return ResponseEntity.ok(exemplares);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Exemplar>> listarPorStatus(@PathVariable StatusExemplar status) {
    	List<Exemplar> exemplares = exemplarService.buscarTodosComStatusIgualA(status);
        return ResponseEntity.ok(exemplares);
    }
    
    @GetMapping("/buscar-por-edicao/{id}")
    public ResponseEntity<List<Exemplar>> listarPorEdicao(@PathVariable Long idEdicao) {
    	List<Exemplar> exemplares = exemplarService.buscarTodosComEdicaoComIdIgualA(idEdicao);
        return ResponseEntity.ok(exemplares);
    }
    
    @GetMapping("/buscar-primeiro-exemplar-por-edicao/{idEdicao}/status/{status}")
    public ResponseEntity<Exemplar> listarPrimeiroDisponivelPorEdicao(@PathVariable Long idEdicao, @PathVariable StatusExemplar status) {
        Exemplar exemplar = exemplarService.buscarPrimeiroComEdicaoComIdIgualA(idEdicao, status);
        return ResponseEntity.ok(exemplar);
    }
    
    @GetMapping("/em-analise-exclusao")
    public ResponseEntity<List<Exemplar>> listarTodosEmAnaliseExclusao() {
    	List<Exemplar> exemplares = exemplarService.buscarTodosComStatusIgualA(StatusExemplar.EM_ANALISE_EXCLUSAO);
        return ResponseEntity.ok(exemplares);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exemplar> buscarPorId(@PathVariable Long id) {
    	Exemplar exemplar = exemplarService.buscarPorId(id);
        return ResponseEntity.ok(exemplar);
    }


    @PostMapping
    public ResponseEntity<Exemplar> criar(@Valid @RequestBody ExemplarDTO exemplarDTO) {
        Exemplar novoExemplar = exemplarService.cadastrarExemplar(exemplarDTO);
        // return ResponseEntity.status(HttpStatus.CREATED).body(novoExemplar);
        return ResponseEntity.ok(novoExemplar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exemplar> atualizar(@PathVariable Long id, @Valid @RequestBody ExemplarDTO exemplarDTO) {
        Exemplar atualizado = exemplarService.atualizar(id, exemplarDTO);
        return ResponseEntity.ok(atualizado);
    }
    
    @PutMapping("/em-analise-exclusao/rejeitar-exclusao-exemplar/{id}")
    public ResponseEntity<Void> rejeitarSolicitacaoDeExclusaoDeExemplar(@PathVariable Long id) {
    	exemplarService.rejeitarSolicitacaoDeExclusaoDoExemplar(id);
    	return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
    	exemplarService.remover(id);
        return ResponseEntity.noContent().build();

    }
}

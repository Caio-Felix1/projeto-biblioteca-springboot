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
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.ExemplarCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.ExemplarUpdateDTO;
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
    public ResponseEntity<List<Exemplar>> listarPorStatus(@PathVariable String status) {
    	StatusExemplar statusExemplar;
    	try {
    		statusExemplar = StatusExemplar.valueOf(status.toUpperCase());
    	}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Erro: o status informado é inválido.");
		}
    	
    	List<Exemplar> exemplares = exemplarService.buscarTodosComStatusIgualA(statusExemplar);
        return ResponseEntity.ok(exemplares);
    }
    
    @GetMapping("/buscar-por-edicao/{idEdicao}")
    public ResponseEntity<List<Exemplar>> listarPorEdicao(@PathVariable Long idEdicao) {
    	List<Exemplar> exemplares = exemplarService.buscarTodosComEdicaoComIdIgualA(idEdicao);
        return ResponseEntity.ok(exemplares);
    }
    
    @GetMapping("/buscar-primeiro-exemplar-por-edicao/{idEdicao}/status/{status}")
    public ResponseEntity<Exemplar> listarPrimeiroDisponivelPorEdicao(@PathVariable Long idEdicao, @PathVariable String status) {
    	StatusExemplar statusExemplar;
    	try {
    		statusExemplar = StatusExemplar.valueOf(status.toUpperCase());
    	}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Erro: o status informado é inválido.");
		}
    	
        Exemplar exemplar = exemplarService.buscarPrimeiroExemplarPorEdicaoEStatus(idEdicao, statusExemplar);
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
    public ResponseEntity<List<Exemplar>> cadastrarExemplares(@Valid @RequestBody ExemplarCreateDTO exemplarCreateDTO) {
        List<Exemplar> novoExemplares = exemplarService.cadastrarExemplares(exemplarCreateDTO);
        // return ResponseEntity.status(HttpStatus.CREATED).body(novoExemplar);
        return ResponseEntity.ok(novoExemplares);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exemplar> atualizar(@PathVariable Long id, @Valid @RequestBody ExemplarUpdateDTO exemplarUpdateDTO) {
        Exemplar atualizado = exemplarService.atualizar(id, exemplarUpdateDTO);
        return ResponseEntity.ok(atualizado);
    }
    
    @PutMapping("/solicitar-exclusao-exemplar/{id}")
    public ResponseEntity<Void> solicitarExclusaoDoExemplar(@PathVariable Long id) {
    	exemplarService.solicitarExclusaoDoExemplar(id);
    	return ResponseEntity.noContent().build();
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

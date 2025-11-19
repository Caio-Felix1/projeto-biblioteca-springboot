package com.projeto.sistemabiblioteca.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.projeto.sistemabiblioteca.DTOs.EditoraDTO;
import com.projeto.sistemabiblioteca.DTOs.PageResponseDTO;
import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.EditoraService;

@RestController
@RequestMapping("/editoras")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<Editora>> buscarTodos(@RequestParam(required = false) String nome, @RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	
    	if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(PageResponseDTO.converterParaDTO(editoraService.buscarTodosComNomeContendo(nome, pageable)));
        }
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(editoraService.buscarTodos(pageable)));
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<PageResponseDTO<Editora>> buscarAtivos(@RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(editoraService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO, pageable)));
    }
    
    @GetMapping("/inativos")
    public ResponseEntity<PageResponseDTO<Editora>> buscarInativos(@RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(editoraService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editora> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(editoraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Editora> cadastrar(@RequestBody EditoraDTO editoraDTO) {
    	Editora editora = new Editora(editoraDTO.nome());
        Editora novaEditora = editoraService.inserir(editora);
        return ResponseEntity.ok(novaEditora);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editora> atualizar(@PathVariable Long id, @RequestBody EditoraDTO editoraDTO) {
    	Editora editora = new Editora(editoraDTO.nome());
        return ResponseEntity.ok(editoraService.atualizar(id, editora));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        editoraService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}

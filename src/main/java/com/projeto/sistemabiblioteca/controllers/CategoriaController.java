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

import com.projeto.sistemabiblioteca.DTOs.CategoriaDTO;
import com.projeto.sistemabiblioteca.DTOs.PageResponseDTO;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<Categoria>> buscarTodos(@RequestParam(required = false) String nome, @RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	
    	if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(PageResponseDTO.converterParaDTO(categoriaService.buscarTodosComNomeContendo(nome, pageable)));
        }
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(categoriaService.buscarTodos(pageable)));
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<PageResponseDTO<Categoria>> buscarAtivos(@RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(categoriaService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO, pageable)));
    }
    
    @GetMapping("/inativos")
    public ResponseEntity<PageResponseDTO<Categoria>> buscarInativos(@RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(categoriaService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> cadastrar(@Valid @RequestBody CategoriaDTO categoriaDTO) {
    	Categoria categoria = new Categoria(categoriaDTO.nome());
        Categoria novaCategoria = categoriaService.inserir(categoria);
        return ResponseEntity.ok(novaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
    	Categoria categoria = new Categoria(categoriaDTO.nome());
    	return ResponseEntity.ok(categoriaService.atualizar(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        categoriaService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
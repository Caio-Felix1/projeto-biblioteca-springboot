package com.projeto.sistemabiblioteca.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.AutorDTO;
import com.projeto.sistemabiblioteca.DTOs.PageResponseDTO;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.AutorService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/autores")
public class AutorController {
    protected final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<PageResponseDTO<Autor>> buscarTodos(@RequestParam(required = false) String nome, @RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	
    	if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(PageResponseDTO.converterParaDTO(autorService.buscarTodosComNomeContendo(nome, pageable)));
        }
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(autorService.buscarTodos(pageable)));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/ativos")
    public ResponseEntity<PageResponseDTO<Autor>> buscarAtivos(@RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(autorService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO, pageable)));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/inativos")
    public ResponseEntity<PageResponseDTO<Autor>> buscarInativos(@RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(autorService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO, pageable)));
    }

    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Long id) {
            return ResponseEntity.ok(autorService.buscarPorId(id));
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<Autor> cadastrar(@Valid @RequestBody AutorDTO autorDTO) {
    	Autor autor = new Autor(autorDTO.nome());
        Autor novoAutor = autorService.inserir(autor);
        return ResponseEntity.ok(novoAutor);
    }
    
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizar(@PathVariable Long id, @Valid @RequestBody AutorDTO autorDTO) {
    	Autor autor = new Autor(autorDTO.nome());
    	return ResponseEntity.ok(autorService.atualizar(id, autor));
    }

    // Deletar autor
    @PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
            autorService.inativar(id);
            return ResponseEntity.noContent().build();
    }
}

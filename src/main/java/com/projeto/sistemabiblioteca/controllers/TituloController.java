package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

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

import com.projeto.sistemabiblioteca.DTOs.PageResponseDTO;
import com.projeto.sistemabiblioteca.DTOs.TituloAutoresDTO;
import com.projeto.sistemabiblioteca.DTOs.TituloCategoriasDTO;
import com.projeto.sistemabiblioteca.DTOs.TituloCreateDTO;
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
    public ResponseEntity<PageResponseDTO<Titulo>> listarTodos(@RequestParam(required = false) String nome, @RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	
    	if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(PageResponseDTO.converterParaDTO(tituloService.buscarTodosComNomeContendo(nome, pageable)));
        }
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(tituloService.buscarTodos(pageable)));
    }

    @GetMapping("/ativos")
    public ResponseEntity<PageResponseDTO<Titulo>> listarAtivos(@RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(tituloService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO, pageable)));
    }
    
    @GetMapping("/inativos")
    public ResponseEntity<PageResponseDTO<Titulo>> listarInativos(@RequestParam int pagina, @RequestParam int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(tituloService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Titulo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tituloService.buscarPorId(id));
    }


    @PostMapping
    public ResponseEntity<Titulo> criar(@Valid @RequestBody TituloCreateDTO tituloCreateDTO) {
        Titulo novo = tituloService.cadastrarTitulo(tituloCreateDTO);
        return ResponseEntity.ok(novo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Titulo> atualizar(@PathVariable Long id, @Valid @RequestBody TituloCreateDTO tituloCreateDTO) {
        Titulo atualizado = tituloService.atualizarTituloCompleto(id, tituloCreateDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        tituloService.inativar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{idTitulo}/categorias")
    public ResponseEntity<Void> adicionarCategorias(@PathVariable Long idTitulo, @Valid @RequestBody TituloCategoriasDTO tituloCategoriasDTO) {
        tituloService.adicionarCategorias(idTitulo, tituloCategoriasDTO.idsCategorias());
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{idTitulo}/categorias")
    public ResponseEntity<Void> removerCategorias(@PathVariable Long idTitulo, @Valid @RequestBody TituloCategoriasDTO tituloCategoriasDTO) {
        tituloService.removerCategorias(idTitulo, tituloCategoriasDTO.idsCategorias());
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{idTitulo}/autores")
    public ResponseEntity<Void> adicionarAutores(@PathVariable Long idTitulo, @Valid @RequestBody TituloAutoresDTO tituloAutoresDTO) {
        tituloService.adicionarAutores(idTitulo, tituloAutoresDTO.idsAutores());
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{idTitulo}/autores")
    public ResponseEntity<Void> removerAutores(@PathVariable Long idTitulo, @Valid @RequestBody TituloAutoresDTO tituloAutoresDTO) {
        tituloService.removerAutores(idTitulo, tituloAutoresDTO.idsAutores());
        return ResponseEntity.noContent().build();
    }



}

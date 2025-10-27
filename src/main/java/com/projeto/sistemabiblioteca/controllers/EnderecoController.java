package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.services.EnderecoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> buscarTodos() {
        return ResponseEntity.ok(enderecoService.buscarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Endereco>> buscarAtivos() {
        return ResponseEntity.ok(enderecoService.buscarTodosComStatusIgualA(StatusAtivo.ATIVO));
    }
    
    @GetMapping("/inativos")
    public ResponseEntity<List<Endereco>> buscarInativos() {
        return ResponseEntity.ok(enderecoService.buscarTodosComStatusIgualA(StatusAtivo.INATIVO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.buscarPorId(id));
    }
    
    /*
    @PostMapping
    public ResponseEntity<Endereco> cadastrar(@RequestBody Endereco endereco) {
        Endereco novaEndereco = enderecoService.inserir(endereco);
        return ResponseEntity.ok(novaEndereco);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable Long id, @RequestBody Endereco endereco) {
        return ResponseEntity.ok(enderecoService.atualizar(id, endereco));
    }
	
	
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        enderecoService.inativar(id);
        return ResponseEntity.noContent().build();
    }
    */
}

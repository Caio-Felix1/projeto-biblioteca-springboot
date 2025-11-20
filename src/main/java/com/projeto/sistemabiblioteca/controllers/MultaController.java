package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.entities.Multa;
import com.projeto.sistemabiblioteca.entities.enums.StatusPagamento;
import com.projeto.sistemabiblioteca.services.MultaService;

@RestController
@RequestMapping("/multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Multa>> listarTodos() {
        return ResponseEntity.ok(multaService.buscarTodos());
    }
	
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Multa>> listarPorStatus(@PathVariable String status) {
    	StatusPagamento statusPagamento;
    	try {
    		statusPagamento = StatusPagamento.valueOf(status.toUpperCase());
    	}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Erro: o status informado é inválido.");
		}
    	
        return ResponseEntity.ok(multaService.buscarTodosComStatusPagamentoIgualA(statusPagamento));
    }
    
	@PreAuthorize("hasAnyRole('BIBLIOTECARIO','ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Multa> buscarPorId(@PathVariable Long id) {
    	Multa multa = multaService.buscarPorId(id);
        return ResponseEntity.ok(multa);
    }
	
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PutMapping("/perdoar-multa/{id}")
    public ResponseEntity<Multa> perdoarMulta(@PathVariable Long id) {
    	Multa multaPerdoada = multaService.perdoarMulta(id);
        return ResponseEntity.ok(multaPerdoada);
    }
}

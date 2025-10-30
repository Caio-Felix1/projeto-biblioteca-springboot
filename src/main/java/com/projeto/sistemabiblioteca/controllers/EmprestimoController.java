package com.projeto.sistemabiblioteca.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.DataDevolucaoPrevistaDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.EmprestimoUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;
import com.projeto.sistemabiblioteca.services.EmprestimoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
	
	private final EmprestimoService emprestimoService;
	
	public EmprestimoController(EmprestimoService emprestimoService) {
		this.emprestimoService = emprestimoService;
	}
	
    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        return ResponseEntity.ok(emprestimoService.buscarTodos());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Emprestimo>> listarPorStatus(@PathVariable String status) {
    	StatusEmprestimo statusEmprestimo;
    	try {
    		statusEmprestimo = StatusEmprestimo.valueOf(status.toUpperCase());
    	}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Erro: o status informado é inválido.");
		}
    	
        return ResponseEntity.ok(emprestimoService.buscarTodosComStatusIgualA(statusEmprestimo));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
    	Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimo);
    }
    
    @PostMapping
    public ResponseEntity<List<Emprestimo>> cadastrarEmprestimos(@Valid @RequestBody EmprestimoCreateDTO emprestimoCreateDTO) {
    	List<Emprestimo> emprestimos = emprestimoService.cadastrarEmprestimos(emprestimoCreateDTO);
    	return ResponseEntity.ok(emprestimos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emprestimo> atualizar(@PathVariable Long id, @Valid @RequestBody EmprestimoUpdateDTO emprestimoUpdateDTO) {
    	Emprestimo emprestimoAtualizado = emprestimoService.atualizar(id, emprestimoUpdateDTO);
        return ResponseEntity.ok(emprestimoAtualizado);
    }
    
    @PutMapping("/registrar-separacao/{id}")
    public ResponseEntity<Void> registrarSeparacaoDoExemplar(@PathVariable Long id) {
    	emprestimoService.registrarSeparacaoDoExemplar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/registrar-retirada-manual/{id}")
    public ResponseEntity<Void> registrarRetiradaDoExemplar(@PathVariable Long id, @Valid @RequestBody DataDevolucaoPrevistaDTO dtDevolucaoPrevistaDTO) {
    	emprestimoService.registrarRetiradaDoExemplar(id, dtDevolucaoPrevistaDTO.dtDevolucaoPrevista());
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/registrar-retirada/{id}")
    public ResponseEntity<Void> registrarRetiradaDoExemplar(@PathVariable Long id) {
    	emprestimoService.registrarRetiradaDoExemplar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/renovar-data-de-devolucao-prevista/{id}")
    public ResponseEntity<Void> renovarDataDeDevolucaoPrevista(@PathVariable Long id, @Valid @RequestBody DataDevolucaoPrevistaDTO dtDevolucaoPrevistaDTO) {
    	emprestimoService.renovarDataDeDevolucaoPrevista(id, dtDevolucaoPrevistaDTO.dtDevolucaoPrevista());
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/registrar-devolucao/{id}")
    public ResponseEntity<Void> registrarDevolucaoDoExemplar(@PathVariable Long id) {
    	emprestimoService.registrarDevolucaoDoExemplar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/pagar-multa/{id}")
    public ResponseEntity<Void> pagarMulta(@PathVariable Long id) {
    	emprestimoService.pagarMulta(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/registrar-perda/{id}")
    public ResponseEntity<Void> registrarPerdaDoExemplar(@PathVariable Long id) {
    	emprestimoService.registrarPerdaDoExemplar(id);
        return ResponseEntity.noContent().build();
    }
}

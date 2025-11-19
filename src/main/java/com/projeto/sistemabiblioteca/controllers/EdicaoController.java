package com.projeto.sistemabiblioteca.controllers;

import static com.projeto.sistemabiblioteca.entities.enums.StatusAtivo.ATIVO;
import static com.projeto.sistemabiblioteca.entities.enums.StatusAtivo.INATIVO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projeto.sistemabiblioteca.DTOs.EdicaoDTO;
import com.projeto.sistemabiblioteca.DTOs.PageResponseDTO;
import com.projeto.sistemabiblioteca.entities.Edicao;
import com.projeto.sistemabiblioteca.entities.interfaces.ArmazenamentoService;
import com.projeto.sistemabiblioteca.services.EdicaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/edicoes")
public class EdicaoController {
	
    private final EdicaoService edicaoService;
    private final ArmazenamentoService armazenamentoService;

    public EdicaoController(EdicaoService edicaoService, ArmazenamentoService armazenamentoService) {
        this.edicaoService = edicaoService;
        this.armazenamentoService = armazenamentoService;
    }
    
    @GetMapping
    public ResponseEntity<PageResponseDTO<Edicao>> listarTodos(@RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	return ResponseEntity.ok(PageResponseDTO.converterParaDTO(edicaoService.buscarTodos(pageable)));
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<PageResponseDTO<Edicao>> listarAtivos(@RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	Page<Edicao> ativos = edicaoService.buscarTodosComStatusIgualA(ATIVO, pageable);
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(ativos));
    }
    
    @GetMapping("/inativos")
    public ResponseEntity<PageResponseDTO<Edicao>> listarInativos(@RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	Page<Edicao> inativos = edicaoService.buscarTodosComStatusIgualA(INATIVO, pageable);
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(inativos));
    }
    
    @GetMapping("/buscar-por-autor")
    public ResponseEntity<PageResponseDTO<Edicao>> listarTodosPeloAutor(@RequestParam String nomeAutor, @RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	Page<Edicao> edicoes = edicaoService.buscarTodosComAutorComNomeContendo(nomeAutor, pageable);
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(edicoes));
    }
    
    @GetMapping("/buscar-por-titulo")
    public ResponseEntity<PageResponseDTO<Edicao>> listarTodosPeloTitulo(@RequestParam String nomeTitulo, @RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	Page<Edicao> edicoes = edicaoService.buscarTodosComTituloComNomeContendo(nomeTitulo, pageable);
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(edicoes));
    }
    
    @GetMapping("/buscar-por-categoria/{id}")
    public ResponseEntity<PageResponseDTO<Edicao>> listarTodosPelaCategoria(@PathVariable Long id, @RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	Page<Edicao> edicoes = edicaoService.buscarTodosComCategoriaComIdIgualA(id, pageable);
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(edicoes));
    }
    
    @GetMapping("/buscar-por-editora/{id}")
    public ResponseEntity<PageResponseDTO<Edicao>> listarTodosPelaEditora(@PathVariable Long id, @RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
    	Page<Edicao> edicoes = edicaoService.buscarTodosComEditoraComIdIgualA(id, pageable);
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(edicoes));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Edicao> buscarPorId(@PathVariable Long id) {
        Edicao edicao = edicaoService.buscarPorId(id);
        return ResponseEntity.ok(edicao);
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Edicao> criar(
    		@Valid @RequestPart("edicao") EdicaoDTO edicao, 
    		@RequestPart(value = "imagem") MultipartFile imagem) {
    	
    	if (imagem == null || imagem.isEmpty()) {
    		throw new IllegalArgumentException("Erro: imagem deve ser informada.");
    	}
    	
    	String imagemUrl = armazenamentoService.salvar(imagem); 
    	
        Edicao nova = edicaoService.cadastrarEdicao(edicao, imagemUrl);
        return ResponseEntity.ok(nova);
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Edicao> atualizar(
    		@PathVariable Long id, 
    		@Valid @RequestPart("edicao") EdicaoDTO edicao, 
    		@RequestPart(value = "imagem", required = false) MultipartFile imagem) {
    	
    	String imagemUrl = null;
    	if (imagem != null && !imagem.isEmpty()) {
    		imagemUrl = armazenamentoService.salvar(imagem); 
    	}
    	
        Edicao atualizada = edicaoService.atualizar(id, edicao, imagemUrl);
        return ResponseEntity.ok(atualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
    	edicaoService.inativar(id);
    	return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar")
   // public String buscar(@RequestParam("q") String termo) {
    public ResponseEntity<PageResponseDTO<Edicao>> buscar(@RequestParam("q") String termo, @RequestParam int pagina, @RequestParam int tamanho) {
    	Pageable pageable = PageRequest.of(pagina, tamanho);
        return ResponseEntity.ok(PageResponseDTO.converterParaDTO(edicaoService.buscarPorTituloOuAutor(termo, pageable)));
    }

}

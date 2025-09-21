package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;

import jakarta.persistence.EntityNotFoundException;

public class TituloService {

	private TituloRepository TituloRepository;
	
	private CategoriaService categoriaService;
	
	private AutorService autorService;

	public TituloService(TituloRepository TituloRepository, CategoriaService categoriaService, AutorService autorService) {
		this.TituloRepository = TituloRepository;
		this.categoriaService = categoriaService;
		this.autorService = autorService;
	}
	
	public List<Titulo> buscarTodos() {
		return TituloRepository.findAll();
	}
	
	public List<Titulo> buscarTodosComNomeContendo(String nome) {
		return TituloRepository.findAllByNomeContainingIgnoreCase(nome);
	}
	
	public Titulo buscarPorId(Long id) {
		Optional<Titulo> titulo = TituloRepository.findById(id);
		if (titulo.isEmpty()) {
			throw new EntityNotFoundException("Erro: título com id correspondente não foi encontrado.");
		}
		return titulo.get();
	}
	
	public Titulo inserir(Titulo Titulo) {
		return TituloRepository.save(Titulo);
	}
	
	public void deletar(Long id) {
		TituloRepository.deleteById(id);
	}
	
	public Titulo atualizar(Long id, Titulo Titulo2) {
		Titulo titulo1 = buscarPorId(id);
		atualizarDados(titulo1, Titulo2);
		return TituloRepository.save(titulo1);
	}
	
	private void atualizarDados(Titulo t1, Titulo t2) {
		t1.setNome(t2.getNome());
		t1.setDescricao(t2.getDescricao());
	}
	
	public void adicionarCategoria(Long idTitulo, Long idCategoria) {
		Titulo titulo = buscarPorId(idTitulo);
		Categoria categoria = categoriaService.buscarPorId(idCategoria);
		titulo.adicionarCategoria(categoria);
		TituloRepository.save(titulo);
	}
	
	public void removerCategoria(Long idTitulo, Long idCategoria) {
		Titulo titulo = buscarPorId(idTitulo);
		Categoria categoria = categoriaService.buscarPorId(idCategoria);
		titulo.removerCategoria(categoria);
		TituloRepository.save(titulo);
	}
	
	public void adicionarAutor(Long idTitulo, Long idAutor) {
		Titulo titulo = buscarPorId(idTitulo);
		Autor autor = autorService.buscarPorId(idAutor);
		titulo.adicionarAutor(autor);
		TituloRepository.save(titulo);
	}
	
	public void removerAutor(Long idTitulo, Long idAutor) {
		Titulo titulo = buscarPorId(idTitulo);
		Autor autor = autorService.buscarPorId(idAutor);
		titulo.removerAutor(autor);
		TituloRepository.save(titulo);
	}
}

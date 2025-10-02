package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TituloService {

	private TituloRepository tituloRepository;
	
	private CategoriaService categoriaService;
	
	private AutorService autorService;

	public TituloService(TituloRepository tituloRepository, CategoriaService categoriaService, AutorService autorService) {
		this.tituloRepository = tituloRepository;
		this.categoriaService = categoriaService;
		this.autorService = autorService;
	}
	
	public List<Titulo> buscarTodos() {
		return tituloRepository.findAll();
	}
	
	public List<Titulo> buscarTodosComNomeContendo(String nome) {
		return tituloRepository.findAllByNomeContainingIgnoreCase(nome);
	}
	
	public List<Titulo> buscarTodosComStatusIgualA(StatusAtivo status) {
		return tituloRepository.findAllByStatusEquals(status);
	}
	
	public Titulo buscarPorId(Long id) {
		Optional<Titulo> titulo = tituloRepository.findById(id);
		if (titulo.isEmpty()) {
			throw new EntityNotFoundException("Erro: título com id correspondente não foi encontrado.");
		}
		return titulo.get();
	}
	
	public Titulo inserir(Titulo Titulo) {
		return tituloRepository.save(Titulo);
	}
	
	public void inativar(Long id) {
		Titulo titulo = buscarPorId(id);
		if (titulo.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: título já está inativo.");
		}
		titulo.inativar();
		tituloRepository.save(titulo);
	}
	
	public Titulo atualizar(Long id, Titulo Titulo2) {
		Titulo titulo1 = buscarPorId(id);
		atualizarDados(titulo1, Titulo2);
		return tituloRepository.save(titulo1);
	}
	
	private void atualizarDados(Titulo titulo1, Titulo titulo2) {
		titulo1.setNome(titulo2.getNome());
		titulo1.setDescricao(titulo2.getDescricao());
	}
	
	public void adicionarCategoria(Long idTitulo, Long idCategoria) {
		Titulo titulo = buscarPorId(idTitulo);
		Categoria categoria = categoriaService.buscarPorId(idCategoria);
		titulo.adicionarCategoria(categoria);
		tituloRepository.save(titulo);
	}
	
	public void removerCategoria(Long idTitulo, Long idCategoria) {
		Titulo titulo = buscarPorId(idTitulo);
		Categoria categoria = categoriaService.buscarPorId(idCategoria);
		titulo.removerCategoria(categoria);
		tituloRepository.save(titulo);
	}
	
	public void adicionarAutor(Long idTitulo, Long idAutor) {
		Titulo titulo = buscarPorId(idTitulo);
		Autor autor = autorService.buscarPorId(idAutor);
		titulo.adicionarAutor(autor);
		tituloRepository.save(titulo);
	}
	
	public void removerAutor(Long idTitulo, Long idAutor) {
		Titulo titulo = buscarPorId(idTitulo);
		Autor autor = autorService.buscarPorId(idAutor);
		titulo.removerAutor(autor);
		tituloRepository.save(titulo);
	}
}

package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.TituloCreateDTO;
import com.projeto.sistemabiblioteca.DTOs.TituloUpdateDTO;
import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.Categoria;
import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.TituloRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

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
	
	@Transactional
	public Titulo cadastrarTitulo(TituloCreateDTO tituloCreateDTO) {
		List<Autor> autores = autorService.buscarTodosPorId(tituloCreateDTO.idsAutores());
		boolean temAutorInativo = autores.stream().anyMatch(a -> a.getStatusAtivo() == StatusAtivo.INATIVO);
		
		if (temAutorInativo) {
			throw new IllegalArgumentException("Erro: não é possível associar um título a um autor com status inativo.");
		}
		
		List<Categoria> categorias = categoriaService.buscarTodosPorId(tituloCreateDTO.idsCategorias());
		boolean temCategoriaInativa = categorias.stream().anyMatch(c -> c.getStatusAtivo() == StatusAtivo.INATIVO);
		
		if (temCategoriaInativa) {
			throw new IllegalArgumentException("Erro: não é possível associar um título a uma categoria com status inativo.");
		}
		
		Titulo titulo = new Titulo(tituloCreateDTO.nome(), tituloCreateDTO.descricao());
		
		autores.forEach(titulo::adicionarAutor);
		categorias.forEach(titulo::adicionarCategoria);
		
		return inserir(titulo);
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
	
	public Titulo atualizar(Long id, TituloUpdateDTO tituloUpdateDTO) {
		Titulo titulo1 = buscarPorId(id);
		
		Titulo titulo2 = new Titulo(tituloUpdateDTO.nome(), tituloUpdateDTO.descricao());
		
		atualizarDados(titulo1, titulo2);
		return tituloRepository.save(titulo1);
	}
	
	private void atualizarDados(Titulo titulo1, Titulo titulo2) {
		titulo1.setNome(titulo2.getNome());
		titulo1.setDescricao(titulo2.getDescricao());
	}
	
	@Transactional
	public void adicionarCategorias(Long idTitulo, Set<Long> idsCategorias) {
		Titulo titulo = buscarPorId(idTitulo);
		List<Categoria> categorias = categoriaService.buscarTodosPorId(idsCategorias);
		boolean temInativo = categorias.stream().anyMatch(c -> c.getStatusAtivo() == StatusAtivo.INATIVO);
		
		if (temInativo) {
			throw new IllegalArgumentException("Erro: não é possível associar um título a uma categoria com status inativo ao atualizar.");
		}
		
		categorias.forEach(titulo::adicionarCategoria);
		tituloRepository.save(titulo);
	}

	@Transactional
	public void removerCategorias(Long idTitulo, Set<Long> idsCategorias) {
		Titulo titulo = buscarPorId(idTitulo);
		List<Categoria> categorias = categoriaService.buscarTodosPorId(idsCategorias);
		categorias.forEach(titulo::removerCategoria);
		tituloRepository.save(titulo);
	}

	@Transactional
	public void adicionarAutores(Long idTitulo, Set<Long> idsAutores) {
		Titulo titulo = buscarPorId(idTitulo);
		List<Autor> autores = autorService.buscarTodosPorId(idsAutores);
		boolean temInativo = autores.stream().anyMatch(a -> a.getStatusAtivo() == StatusAtivo.INATIVO);
		
		if (temInativo) {
			throw new IllegalArgumentException("Erro: não é possível associar um título a um autor com status inativo ao atualizar.");
		}
		
		autores.forEach(titulo::adicionarAutor);
		tituloRepository.save(titulo);
	}

	@Transactional
	public void removerAutores(Long idTitulo, Set<Long> idsAutores) {
		Titulo titulo = buscarPorId(idTitulo);
		List<Autor> autores = autorService.buscarTodosPorId(idsAutores);
		autores.forEach(titulo::removerAutor);
		tituloRepository.save(titulo);
	}
}

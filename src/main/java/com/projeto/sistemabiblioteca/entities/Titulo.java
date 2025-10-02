package com.projeto.sistemabiblioteca.entities;

import java.util.HashSet;
import java.util.Set;

import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.entities.interfaces.Ativavel;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "titulo")
public class Titulo implements Ativavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTitulo;
	
	private String nome;
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	private StatusAtivo status;
	
	@ManyToMany
	@JoinTable(name = "titulo_categoria",        
	joinColumns = @JoinColumn(name = "id_titulo"),         
	inverseJoinColumns = @JoinColumn(name = "id_categoria"))
	private Set<Categoria> categorias = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "titulo_autor",        
	joinColumns = @JoinColumn(name = "id_titulo"),         
	inverseJoinColumns = @JoinColumn(name = "id_autor"))
	private Set<Autor> autores = new HashSet<>();
	
	protected Titulo() {
		
	}
	
	public Titulo(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
		ativar();
	}
	
	public Long getIdTitulo() {
		return idTitulo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Set<Categoria> getCategorias() {
		return new HashSet<>(categorias);
	}

	public Set<Autor> getAutores() {
		return new HashSet<>(autores);
	}

	public void removerCategoria(Categoria categoria) {
		categorias.remove(categoria);
	}
	
	public void adicionarCategoria(Categoria categoria) {
		categorias.add(categoria);
	}
	
	public void removerAutor(Autor autor) {
		autores.remove(autor);
	}
	
	public void adicionarAutor(Autor autor) {
		autores.add(autor);
	}
	
	@Override
	public StatusAtivo getStatusAtivo() {
		return status;
	}

	@Override
	public void ativar() {
		status = StatusAtivo.ATIVO;
	}

	@Override
	public void inativar() {
		status = StatusAtivo.INATIVO;
	}
}

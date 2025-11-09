package com.projeto.sistemabiblioteca.dashboard.dimensions;

import java.util.HashSet;
import java.util.Set;

import com.projeto.sistemabiblioteca.entities.Titulo;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "dim_titulo")
@AttributeOverrides({
	@AttributeOverride(name = "surrogateKey", column = @Column(name = "sk_titulo")),
	@AttributeOverride(name = "idNatural", column = @Column(name = "id_natural_titulo"))
})
public class DimTitulo extends Dimensao {
	
	private String nome;
	private StatusAtivo status;
	
	@ManyToMany
	@JoinTable(name = "dim_titulo_categoria",        
	joinColumns = @JoinColumn(name = "sk_titulo"),         
	inverseJoinColumns = @JoinColumn(name = "sk_categoria"))
	private Set<DimCategoria> categorias = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "dim_titulo_autor",        
	joinColumns = @JoinColumn(name = "sk_titulo"),         
	inverseJoinColumns = @JoinColumn(name = "sk_autor"))
	private Set<DimAutor> autores = new HashSet<>();
	
	protected DimTitulo() {
		super();
	}
	
	public DimTitulo(Titulo titulo) {
		nome = titulo.getNome();
		status = titulo.getStatusAtivo();
		
		titulo.getAutores().forEach(autor -> this.adicionarAutor(new DimAutor(autor)));
		titulo.getCategorias().forEach(categoria -> this.adicionarCategoria(new DimCategoria(categoria)));
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public StatusAtivo getStatus() {
		return status;
	}

	public Set<DimCategoria> getCategorias() {
		return new HashSet<>(categorias);
	}

	public Set<DimAutor> getAutores() {
		return new HashSet<>(autores);
	}

	public void setStatus(StatusAtivo status) {
		this.status = status;
	}
	
	public void removerCategoria(DimCategoria dimCategoria) {
		categorias.remove(dimCategoria);
	}
	
	public void adicionarCategoria(DimCategoria dimCategoria) {
		categorias.add(dimCategoria);
	}
	
	public void removerAutor(DimAutor dimAutor) {
		autores.remove(dimAutor);
	}
	
	public void adicionarAutor(DimAutor dimAutor) {
		autores.add(dimAutor);
	}
}

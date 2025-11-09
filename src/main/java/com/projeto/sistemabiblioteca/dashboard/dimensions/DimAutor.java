package com.projeto.sistemabiblioteca.dashboard.dimensions;

import com.projeto.sistemabiblioteca.entities.Autor;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "dim_autor")
@AttributeOverrides({
	@AttributeOverride(name = "surrogateKey", column = @Column(name = "sk_autor")),
	@AttributeOverride(name = "idNatural", column = @Column(name = "id_natural_autor"))
})
public class DimAutor extends Dimensao {
	
	private String nome;
	private StatusAtivo status;
	
	protected DimAutor() {
		super();
	}
	
	public DimAutor(Autor autor) {
		nome = autor.getNome();
		status = autor.getStatusAtivo();
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

	public void setStatus(StatusAtivo status) {
		this.status = status;
	}
}

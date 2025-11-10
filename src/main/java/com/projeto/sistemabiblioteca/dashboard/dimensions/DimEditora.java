package com.projeto.sistemabiblioteca.dashboard.dimensions;

import com.projeto.sistemabiblioteca.entities.Editora;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "dim_editora")
@AttributeOverrides({
	@AttributeOverride(name = "surrogateKey", column = @Column(name = "sk_editora")),
	@AttributeOverride(name = "idNatural", column = @Column(name = "id_natural_editora"))
})
public class DimEditora extends Dimensao {
	
	private String nome;
	private StatusAtivo status;
	
	protected DimEditora() {
		super();
	}
	
	public DimEditora(Editora editora) {
		super(editora.getIdEditora());
		nome = editora.getNome();
		status = editora.getStatusAtivo();
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

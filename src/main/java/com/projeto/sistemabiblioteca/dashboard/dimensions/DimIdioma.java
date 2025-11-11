package com.projeto.sistemabiblioteca.dashboard.dimensions;

import com.projeto.sistemabiblioteca.entities.Idioma;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "dim_idioma")
@AttributeOverrides({
	@AttributeOverride(name = "surrogateKey", column = @Column(name = "sk_idioma")),
	@AttributeOverride(name = "idNatural", column = @Column(name = "id_natural_idioma"))
})
public class DimIdioma extends Dimensao {
	
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private StatusAtivo status;
	
	protected DimIdioma() {
		super();
	}
	
	public DimIdioma(Idioma idioma) {
		super(idioma.getIdIdioma());
		nome = idioma.getNome();
		status = idioma.getStatusAtivo();
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

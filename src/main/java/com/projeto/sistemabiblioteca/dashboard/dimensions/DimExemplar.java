package com.projeto.sistemabiblioteca.dashboard.dimensions;

import com.projeto.sistemabiblioteca.entities.Exemplar;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import com.projeto.sistemabiblioteca.entities.enums.StatusExemplar;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "dim_exemplar")
@AttributeOverrides({
	@AttributeOverride(name = "surrogateKey", column = @Column(name = "sk_exemplar")),
	@AttributeOverride(name = "idNatural", column = @Column(name = "id_natural_exemplar"))
})
public class DimExemplar extends Dimensao {
	
	@Enumerated(EnumType.STRING)
	private EstadoFisico estadoFisico;
	
	@Enumerated(EnumType.STRING)
	private StatusExemplar status;
	
	protected DimExemplar() {
		super();
	}
	
	public DimExemplar(Exemplar exemplar) {
		super(exemplar.getIdExemplar());
		estadoFisico = exemplar.getEstadoFisico();
		status = exemplar.getStatus();
	}

	public EstadoFisico getEstadoFisico() {
		return estadoFisico;
	}

	public void setEstadoFisico(EstadoFisico estadoFisico) {
		this.estadoFisico = estadoFisico;
	}

	public StatusExemplar getStatus() {
		return status;
	}

	public void setStatus(StatusExemplar status) {
		this.status = status;
	}
}

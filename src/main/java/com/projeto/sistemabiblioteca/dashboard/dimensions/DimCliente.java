package com.projeto.sistemabiblioteca.dashboard.dimensions;

import java.time.LocalDate;

import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "dim_cliente")
@AttributeOverrides({
	@AttributeOverride(name = "surrogateKey", column = @Column(name = "sk_cliente")),
	@AttributeOverride(name = "idNatural", column = @Column(name = "id_natural_cliente"))
})
public class DimCliente extends Dimensao {
	
	private Sexo sexo;
	private LocalDate dtNascimento;
	private StatusConta statusConta;
	private String cidade;
	private String estado;
	private String pais;
	
	protected DimCliente() {
		super();
	}
	
	public DimCliente(Pessoa cliente) {
		super(cliente.getIdPessoa());
		sexo = cliente.getSexo();
		dtNascimento = cliente.getDtNascimento();
		statusConta = cliente.getStatusConta();
		cidade = cliente.getEndereco().getCidade();
		estado = cliente.getEndereco().getEstado().getNome();
		pais = cliente.getEndereco().getEstado().getPais().getNome();
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public StatusConta getStatusConta() {
		return statusConta;
	}

	public void setStatusConta(StatusConta statusConta) {
		this.statusConta = statusConta;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
}

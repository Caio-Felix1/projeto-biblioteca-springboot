package com.projeto.sistemabiblioteca.DTOs;

import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;

public record EmprestimoResponseDTO(
		Long idEmprestimo,
		StatusEmprestimo status,
		Long idPessoa,
		String nomePessoa,
		MultaDTO multa) {
	
	public static EmprestimoResponseDTO converterParaDTO(Emprestimo emp) {
		MultaDTO multaDTO = new MultaDTO(
				emp.getMulta().getIdMulta(),
				emp.getMulta().getValor(),
				emp.getMulta().getStatusPagamento());
		
		return new EmprestimoResponseDTO(
				emp.getIdEmprestimo(),
				emp.getStatus(),
				emp.getPessoa().getIdPessoa(),
				emp.getPessoa().getNome(),
				multaDTO);
	}
}

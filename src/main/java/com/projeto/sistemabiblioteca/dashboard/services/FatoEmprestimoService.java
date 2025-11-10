package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import com.projeto.sistemabiblioteca.dashboard.facts.FatoEmprestimo;
import com.projeto.sistemabiblioteca.dashboard.repositories.FatoEmprestimoRepository;
import com.projeto.sistemabiblioteca.entities.Emprestimo;
import com.projeto.sistemabiblioteca.entities.enums.StatusEmprestimo;

import jakarta.transaction.Transactional;

public class FatoEmprestimoService {
	
	private FatoEmprestimoRepository fatoEmprestimoRepository;

	public FatoEmprestimoService(FatoEmprestimoRepository fatoEmprestimoRepository) {
		this.fatoEmprestimoRepository = fatoEmprestimoRepository;
	}
	
	@Transactional
	public void atualizar(Emprestimo emprestimoReal, Long skCliente, Long skExemplar, Long skEdicao,
			Long skIdioma, Long skEditora, Long skTitulo) {
		Optional<FatoEmprestimo> fatoEmprestimoExistente = fatoEmprestimoRepository.findByIdNatural(emprestimoReal.getIdEmprestimo());
		
		if (fatoEmprestimoExistente.isPresent()) {
			FatoEmprestimo fatoEmprestimo = fatoEmprestimoExistente.get();
			atualizarDados(fatoEmprestimo, emprestimoReal, skCliente, skExemplar, skEdicao, skIdioma, skEditora, skTitulo);
			fatoEmprestimoRepository.save(fatoEmprestimo);
		}
		else {
			FatoEmprestimo dimNovo = new FatoEmprestimo(emprestimoReal, skCliente, skExemplar, skEdicao, skIdioma, skEditora, skTitulo);
			fatoEmprestimoRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(FatoEmprestimo fatoEmprestimo, Emprestimo emprestimoReal, Long skCliente, 
			Long skExemplar, Long skEdicao, Long skIdioma, Long skEditora, Long skTitulo) {
		fatoEmprestimo.setDtInicioEmprestimo(emprestimoReal.getDtInicioEmprestimo());
		fatoEmprestimo.setDtSeparacaoExemplar(emprestimoReal.getDtSeparacaoExemplar());
		fatoEmprestimo.setDtRetiradaExemplar(emprestimoReal.getDtRetiradaExemplar());
		fatoEmprestimo.setDtDevolucaoPrevista(emprestimoReal.getDtDevolucaoPrevista());
		fatoEmprestimo.setDtDevolvidoExemplar(emprestimoReal.getDtDevolvidoExemplar());
		fatoEmprestimo.setStatus(emprestimoReal.getStatus());
		fatoEmprestimo.setDiasAtraso((emprestimoReal.getStatus() == StatusEmprestimo.DEVOLVIDO) ? emprestimoReal.calcularDiasDeAtraso(emprestimoReal.getDtDevolvidoExemplar()) : 0);
		fatoEmprestimo.setValorMulta(emprestimoReal.getMulta().getValor());
		fatoEmprestimo.setStatusPagamento(emprestimoReal.getMulta().getStatusPagamento());
		
		fatoEmprestimo.setSkCliente(skCliente);
		fatoEmprestimo.setSkExemplar(skExemplar);
		fatoEmprestimo.setSkEdicao(skEdicao);
		fatoEmprestimo.setSkIdioma(skIdioma);
		fatoEmprestimo.setSkEditora(skEditora);
		fatoEmprestimo.setSkTitulo(skTitulo);
	}
}

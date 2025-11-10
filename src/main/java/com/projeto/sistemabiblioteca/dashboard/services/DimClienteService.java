package com.projeto.sistemabiblioteca.dashboard.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCliente;
import com.projeto.sistemabiblioteca.dashboard.repositories.DimClienteRepository;
import com.projeto.sistemabiblioteca.entities.Pessoa;

import jakarta.transaction.Transactional;

@Service
public class DimClienteService {
	
	private DimClienteRepository dimClienteRepository;

	public DimClienteService(DimClienteRepository dimClienteRepository) {
		this.dimClienteRepository = dimClienteRepository;
	}
	
	@Transactional
	public DimCliente atualizar(Pessoa clienteReal) {
		Optional<DimCliente> dimClienteExistente = dimClienteRepository.findByIdNatural(clienteReal.getIdPessoa());
		
		if (dimClienteExistente.isPresent()) {
			DimCliente dimCliente = dimClienteExistente.get();
			atualizarDados(dimCliente, clienteReal);
			return dimClienteRepository.save(dimCliente);
		}
		else {
			DimCliente dimNovo = new DimCliente(clienteReal);
			return dimClienteRepository.save(dimNovo);
		}
	}
	
	private void atualizarDados(DimCliente dimCliente, Pessoa clienteReal) {
		dimCliente.setSexo(clienteReal.getSexo());
		dimCliente.setDtNascimento(clienteReal.getDtNascimento());
		dimCliente.setStatusConta(clienteReal.getStatusConta());
		dimCliente.setCidade(clienteReal.getEndereco().getCidade());
		dimCliente.setEstado(clienteReal.getEndereco().getEstado().getNome());
		dimCliente.setPais(clienteReal.getEndereco().getEstado().getPais().getNome());
	}
}

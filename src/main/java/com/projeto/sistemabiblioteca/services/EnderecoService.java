package com.projeto.sistemabiblioteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.DTOs.EnderecoDTO;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.enums.StatusAtivo;
import com.projeto.sistemabiblioteca.repositories.EnderecoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnderecoService {
	
	private EnderecoRepository enderecoRepository;
	
	private EstadoService estadoService;

	public EnderecoService(EnderecoRepository enderecoRepository, EstadoService estadoService) {
		this.enderecoRepository = enderecoRepository;
		this.estadoService = estadoService;
	}
	
	public List<Endereco> buscarTodos() {
		return enderecoRepository.findAll();
	}
	
	public List<Endereco> buscarTodosComStatusIgualA(StatusAtivo status) {
		return enderecoRepository.findAllByStatusEquals(status);
	}
	
	public Endereco buscarPorId(Long id) {
		Optional<Endereco> endereco = enderecoRepository.findById(id);
		if (endereco.isEmpty()) {
			throw new EntityNotFoundException("Erro: endereço com id correspondente não foi encontrado.");
		}
		return endereco.get();
	}
	
	public Endereco inserir(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}
	
	public void inativar(Long id) {
		Endereco endereco = buscarPorId(id);
		if (endereco.getStatusAtivo() == StatusAtivo.INATIVO) {
			throw new IllegalStateException("Erro: endereço já está inativo.");
		}
		endereco.inativar();
		enderecoRepository.save(endereco);
	}
	
	public Endereco atualizar(Long id, EnderecoDTO enderecoDTO) {
		Endereco endereco1 = buscarPorId(id);
		
		Estado estado = estadoService.buscarPorId(enderecoDTO.idEstado());
		
		Endereco endereco2 = new Endereco(
				enderecoDTO.nomeLogradouro(), 
				enderecoDTO.numero(), 
				enderecoDTO.complemento(), 
				enderecoDTO.bairro(), 
				enderecoDTO.cep(), 
				enderecoDTO.cidade(), 
				estado);
		
		atualizarDados(endereco1, endereco2);
		return enderecoRepository.save(endereco1);
	}
	
	private void atualizarDados(Endereco endereco1, Endereco endereco2) {
		endereco1.setNomeLogradouro(endereco2.getNomeLogradouro());
		endereco1.setNumero(endereco2.getNumero());
		endereco1.setComplemento(endereco2.getComplemento());
		endereco1.setBairro(endereco2.getBairro());
		endereco1.setCep(endereco2.getCep());
		endereco1.setCidade(endereco2.getCidade());
		endereco1.setEstado(endereco2.getEstado());
	}
}

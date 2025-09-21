package com.projeto.sistemabiblioteca.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Pessoa;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AutorizacaoService implements UserDetailsService {

    private PessoaService pessoaService;
    
    public AutorizacaoService(PessoaService pessoaService) {
    	this.pessoaService = pessoaService;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	try {
        	Pessoa p = pessoaService.buscarPorEmail(username);
        	return p;
        }
        catch (EntityNotFoundException e) {
        	throw new UsernameNotFoundException("Erro: usuário não foi encontrado.");
        }
    }
}
